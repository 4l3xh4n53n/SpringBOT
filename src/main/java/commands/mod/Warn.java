package commands.mod;

import Core.Database;
import Core.Embed;
import Core.ModLogger;
import Core.Settings.SettingGetter;
import ErrorMessages.BadCode.SQLError;
import ErrorMessages.UserError.NoPerms;
import ErrorMessages.UserError.RolesNotSet;
import ErrorMessages.UserError.WrongCommandUsage;
import Utility.GetMentioned;
import Utility.GetRoleIDs;
import Utility.RoleChecker;
import Utility.RoleFormatter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import org.apache.commons.collections4.CollectionUtils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

public class Warn {

    private static final String example = "`warn <@user/userID> <reason>` <-- (optional)";
    private static final String info = "Warns the specified user.";
    private static final String set = "`set WarnRoles <@role(S)>`";
    private static final String log = "`set WarnLog <channelID>`";
    private static final String toggle = "`set ModCommands 1/0`";

    private static void Execute(String guildID, TextChannel textChannel, User mentioned, Connection con, Message msg){

        String contentRaw = msg.getContentRaw();
        String[] args = contentRaw.split("\\s+");
        String reason = contentRaw.replace(args[0] + " " + args[1], "");
        String userID = mentioned.getId();
        User executor = msg.getMember().getUser();

        try {
            Statement stmt = con.createStatement();
            String SQL = "SELECT * FROM `" + guildID + "` WHERE userID='" + userID + "'";
            ResultSet rs = stmt.executeQuery(SQL);

            if (rs.next()) {

                int oldwarns = rs.getInt("warns");
                int newwarns = oldwarns + 1;

                String update = "UPDATE `" + guildID + "` SET warns = ? WHERE userID ='" + userID + "'";
                PreparedStatement ud = con.prepareStatement(update);
                ud.setInt(1, newwarns);
                ud.executeUpdate();
                ud.close();

            } else {

                String insert = "INSERT INTO `" + guildID + "`(userID,warns) VALUES(?,?)";
                PreparedStatement ps = con.prepareStatement(insert);
                ps.setString(1, userID);
                ps.setInt(2, 1);
                ps.executeUpdate();
                ps.close();

            }

            stmt.close();
            rs.close();
            con.close();

            EmbedBuilder em = Embed.em(mentioned, textChannel);
            em.setTitle("Warned " + mentioned.getAsTag());
            textChannel.sendMessageEmbeds(em.build()).queue();

            ModLogger.log(textChannel, mentioned, "WarnLog", reason, log, "was warned", executor);

        } catch (Exception x){
            SQLError.TextChannel(textChannel, x, toggle);
        }

    }

    private static void Create(String guildID, TextChannel textChannel, Connection con){
        try {
            Statement stmt = con.createStatement();
            String sql = "CREATE TABLE " + guildID + " (userID TEXT NOT NULL, warns INTEGER PRIMARY KEY)";
            stmt.executeUpdate(sql);
            con.close();
            stmt.close();

        } catch (Exception x){
            SQLError.TextChannel(textChannel, x, toggle);
        }
    }

    public static void checkDatabse(Connection con, String guildID, TextChannel textChannel){
        try {

            DatabaseMetaData dbm = con.getMetaData();
            ResultSet tables = dbm.getTables(null, null, guildID, null);

            if (tables.next()) {
                tables.close();
            }

            else {
                tables.close();
                Create(guildID, textChannel, con);
            }

        } catch (Exception x){
            SQLError.TextChannel(textChannel, x, toggle);
        }
    }

    public static void check(Guild guild, User user, TextChannel textChannel, Message msg, Member member, String guildID){
        Connection con = Database.warns();
        checkDatabse(con, guildID, textChannel);

        User mentionedUser;
        String[] args = msg.getContentRaw().split("\\s+");

        String[] requiredRoles = SettingGetter.ChannelFriendlyGet("WarnRoles", textChannel).split(",");
        List<Role> userRoles = member.getRoles();
        List<String> usersRolesIDs = GetRoleIDs.get(userRoles);

        if (SettingGetter.ChannelFriendlyGet("ModCommands", textChannel).equals("1")) {
            if (args.length > 1) {
                if (RoleChecker.areRolesValid(requiredRoles, guild) == 1) {
                    if ((mentionedUser = GetMentioned.get(msg, args[1], guild)) != null) {
                        if (CollectionUtils.containsAny(Arrays.asList(requiredRoles), usersRolesIDs)) {

                            Execute(guildID, textChannel, mentionedUser, con, msg);

                        } else {
                            NoPerms.Send("warn", RoleFormatter.format(requiredRoles, guild), textChannel, user);
                        }
                    } else {
                        WrongCommandUsage.send(textChannel, example, "You haven't mentioned any members", user);
                    }
                } else {
                    RolesNotSet.ChannelFriendly(textChannel, "warn", set, user, toggle);
                }
            } else {
                WrongCommandUsage.send(textChannel, example, "Wrong amount of args", user);
            }
        }
    }

    public static String getExample() {
        return example;
    }

    public static String getInfo() {
        return info;
    }

    public static String getSet() {
        return set;
    }

    public static String getLog() {
        return log;
    }

    public static String getToggle() {
        return toggle;
    }
}
