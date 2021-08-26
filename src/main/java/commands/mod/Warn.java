package commands.mod;

import Core.Database;
import Core.Embed;
import Core.ModLogger;
import Core.SettingGetter;
import ErrorMessages.BadCode.SQLError;
import ErrorMessages.UserError.NoPerms;
import ErrorMessages.UserError.RolesNotSet;
import ErrorMessages.UserError.WrongCommandUsage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import org.apache.commons.collections4.CollectionUtils;

import java.sql.*;
import java.util.*;
import java.util.List;

public class Warn {

    private static final String example = "`warn <@user/userID> <reason>` <-- (optional)";
    private static final String info = "Warns the specified user.";
    private static final String set = "`set roles WarnRoles <@role(S)>`";
    private static final String log = "`set channel WarnLog <channelID>`";
    private static final String toggle = "`set module ModCommands 1/0`";

    public static void Execute(String guildID, TextChannel txt, Member mentioned, Connection con, Message msg){

        String contentraw = msg.getContentRaw();
        String[] args = contentraw.split("\\s+");
        String reason = contentraw.replace(args[0] + " " + args[1], "");
        String userID = mentioned.getId();
        User user = mentioned.getUser();
        User executor = msg.getMember().getUser();

        try {
            Statement stmt = con.createStatement();
            String SQL = "SELECT * FROM '" + guildID + "' WHERE userID='" + userID + "'";
            ResultSet rs = stmt.executeQuery(SQL);
            if (rs.next()) {

                int oldwarns = rs.getInt("warns");
                int newwarns = oldwarns + 1;

                String update = "UPDATE '" + guildID + "' SET warns = ? WHERE userID ='" + userID + "'";
                PreparedStatement ud = con.prepareStatement(update);
                ud.setInt(1, newwarns);
                ud.executeUpdate();
                ud.close();


            } else {
                String insert = "INSERT INTO '" + guildID + "'(userID,warns) VALUES(?,?)";
                PreparedStatement ps = con.prepareStatement(insert);
                ps.setString(1, userID);
                ps.setInt(2, 1);
                ps.executeUpdate();
                ps.close();
            }
            stmt.close();
            rs.close();
            con.close();

            EmbedBuilder em = Embed.em(user, txt);
            em.setTitle("Warned " + user.getAsTag());
            txt.sendMessage(em.build()).queue();

            ModLogger.log(txt, user, "WarnLog", reason, log, "was warned", executor);

        } catch (Exception x){
            SQLError.TextChannel(txt, x, toggle);
        }

    }

    public static void Create(String guildID, TextChannel txt, Guild guild, User user, Message msg, Connection con){

        //apparently this just refuses to make one even if its already in there and this really wasn't needed

        try {
            Statement stmt = con.createStatement();
            String sql = "CREATE TABLE '" + guildID + "' (userID TEXT NOT NULL, warns INTEGER PRIMARY KEY)";
            stmt.executeUpdate(sql);
            con.close();
            stmt.close();

            checkCommand(guild, guildID, user, txt, msg, con);

        } catch (Exception x){
            SQLError.TextChannel(txt, x, toggle);
        }
    }

    public static void checkCommand(Guild guild, String guildID, User user, TextChannel txt, Message msg, Connection con){

        String[] args = msg.getContentRaw().split("\\s+");
        String[] roles = SettingGetter.ChannelFriendlySet("WarnRoles", txt).split(",");
        List<Role> userroles = guild.getMember(user).getRoles();
        List<String> usersRoles = new ArrayList<>();
        StringBuilder req = new StringBuilder();
        int check = 0;
        Member mentioned;

        int rolecheck = RoleChecker.CheckRoles(roles, guild);

        if (SettingGetter.ChannelFriendlySet("ModCommands", txt).equals("1")) {
            if (args.length > 1) {
                if (rolecheck == 1) {

                    try {
                        guild.retrieveMemberById(args[1]).complete();
                        check = 1;
                    } catch (Exception ignored) {
                    }

                    if (check == 1 || msg.getMentionedMembers().size() > 0) {
                        if (check == 1) {
                            mentioned = guild.retrieveMemberById(args[1]).complete();
                        } else {
                            mentioned = msg.getMentionedMembers().get(0);
                        }

                        for (Role userrole : userroles) {
                            usersRoles.add(userrole.getId());
                        }

                        if (CollectionUtils.containsAny(Arrays.asList(roles), usersRoles)) {
                            Execute(guildID, txt, mentioned, con, msg);
                        } else {
                            for (String s : roles) {
                                Role role = guild.getRoleById(s);
                                req.append("@").append(role.getName()).append(" ");
                            }
                            NoPerms.Send("warn", req.toString(), txt, user);
                        }


                    } else {
                        WrongCommandUsage.send(txt, example, "You haven't mentioned any members", user);
                    }
                } else {
                    RolesNotSet.ChannelFriendly(txt, "warn", set, user, toggle);
                }
            } else {
                WrongCommandUsage.send(txt, example, "Wrong amount of args", user);
            }
        }

    }

    public static void check(Guild guild, User user, TextChannel txt, Message msg){
        String guildID = guild.getId();
        try {
            Connection con = Database.warns();
            DatabaseMetaData dbm = con.getMetaData();
            ResultSet tables = dbm.getTables(null, null, guildID, null);
            if (tables.next()) {
                tables.close();
                checkCommand(guild, guildID,user, txt, msg, con);
            }
            else {
                tables.close();
                Create(guildID, txt, guild, user, msg, con);
            }

        } catch (Exception x){
            SQLError.TextChannel(txt, x, toggle);
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
