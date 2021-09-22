package commands.mod;

import Core.Database;
import Core.Embed;
import Core.SettingGetter;
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
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

public class WarnsAmount {

    private static final String example = "`warns <@user>";
    private static final String info = "Shows you how many warns the specified user has";
    private static final String set = "`set roles WarnRoles <@role(S)>`";
    private static final String toggle = "`set module ModCommands 1/0`";

    private static void Execute(String guildID, TextChannel txt, User mentioned, Connection con){

        String tag = mentioned.getAsTag();
        String pfp = mentioned.getAvatarUrl();
        String userID = mentioned.getId();

        EmbedBuilder em = Embed.em(mentioned, txt);
        em.setAuthor(tag, null, pfp);

        try {

            Statement stmt = con.createStatement();
            String SQL = "SELECT * FROM '" + guildID + "' WHERE userID='" + userID + "'";
            ResultSet rs = stmt.executeQuery(SQL);

            if (rs.next()) {
                int warns = rs.getInt("warns");
                em.addField("Has:", warns + " warns", true);
            } else {
                em.addField("Has:", "no warns", true);
            }

            con.close();
            rs.close();

            txt.sendMessageEmbeds(em.build()).queue();

        } catch (Exception x){
            SQLError.TextChannel(txt, x, toggle);
        }

    }

    public static void check(String guildID, Message msg, TextChannel textChannel, Guild guild, User user, String contentRaw, Member member){
        Connection con = Database.warns();
        Warn.checkDatabse(con, guildID, textChannel, guild, user, msg, member);

        String[] requiredRoles = SettingGetter.ChannelFriendlySet("WarnRoles", textChannel).split(",");
        List<Role> userRoles = member.getRoles();
        List<String> usersRoles = GetRoleIDs.get(userRoles);
        String[] args = contentRaw.split("\\s+");
        User mentioned;

        if (SettingGetter.ChannelFriendlySet("ModCommands", textChannel).equals("1")) {
            if (args.length > 1) {
                if (RoleChecker.areRolesValid(requiredRoles, guild) == 1) {
                    if ((mentioned = GetMentioned.get(msg, args[1], guild)) != null) {
                        if (CollectionUtils.containsAny(Arrays.asList(requiredRoles), usersRoles)) {

                            Execute(guildID, textChannel, mentioned, con);

                        } else {
                            NoPerms.Send("warn", RoleFormatter.format(requiredRoles, guild), textChannel, user);
                        }
                    } else {
                        WrongCommandUsage.send(textChannel, example, "You haven't mentioned any members", user);
                    }
                } else {
                    RolesNotSet.ChannelFriendly(textChannel, "warns", set, user, toggle);
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

    public static String getToggle() {
        return toggle;
    }
}
