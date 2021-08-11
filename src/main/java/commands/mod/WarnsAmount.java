package commands.mod;

import Core.Database;
import Core.Embed;
import Core.SettingGetter;
import ErrorMessages.BadCode.SQLError;
import ErrorMessages.UserError.NoPerms;
import ErrorMessages.UserError.RolesNotSet;
import ErrorMessages.UserError.WrongCommandUsage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import org.apache.commons.collections4.CollectionUtils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import java.util.List;

public class WarnsAmount {

    public static String example = "`warns <@user>";
    public static String info = "Shows you how many warns the specified user has";
    public static String set = "`set roles WarnRoles <@role(S)>`";

    public static void Execute(String guildID, TextChannel txt, Member mentioned, Connection con){

        User user = mentioned.getUser();
        String tag = user.getAsTag();
        String pfp = user.getAvatarUrl();
        String userID = user.getId();

        EmbedBuilder em = Embed.em(user, txt);
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

            txt.sendMessage(em.build()).queue();
        } catch (Exception x){
            SQLError.TextChannel(txt, x);
        }

    }

    public static void Create(Connection con, String guildID, TextChannel txt, Message msg, Guild guild, User user, String contentRaw){
        try {
            Statement stmt = con.createStatement();
            String sql = "CREATE TABLE '" + guildID + "' (userID TEXT NOT NULL, warns INTEGER PRIMARY KEY)";
            stmt.executeUpdate(sql);
            stmt.close();

            checkCommand(guildID, msg, txt, guild, user, contentRaw, con);

        } catch (Exception x){
            SQLError.TextChannel(txt, x);
        }
    }

    public static void checkCommand(String GuildID, Message msg, TextChannel txt, Guild guild, User user, String contentRaw, Connection con){

        String[] roles = SettingGetter.ChannelFriendlySet("WarnRoles", txt).split(",");
        List<Role> userroles = guild.getMember(user).getRoles();
        List<String> usersRoles = new ArrayList<>();
        String[] args = contentRaw.split("\\s+");
        Member mentioned;
        String req = "";

        int check = 0;
        int rolecheck = RoleChecker.CheckRoles(roles, guild);

        if (SettingGetter.ChannelFriendlySet("ModCommands", txt).equals("1")) {
            if (args.length > 1) {
                if (rolecheck == 1) {
                    try {
                        guild.retrieveMemberById(args[1]).complete();
                        check = 1;
                    } catch (Exception ignored) {
                    }

                    for (int i = 0; userroles.size() > i; i++) {
                        usersRoles.add(userroles.get(i).getId());
                    }

                    if (check == 1 || msg.getMentionedMembers().size() > 0) {
                        if (check == 1) {
                            mentioned = guild.retrieveMemberById(args[1]).complete();
                        } else {
                            mentioned = msg.getMentionedMembers().get(0);
                        }

                        if (CollectionUtils.containsAny(Arrays.asList(roles), usersRoles)) {
                            Execute(GuildID, txt, mentioned, con);
                        } else {
                            for (int i = 0; roles.length > i; i++) {
                                Role role = guild.getRoleById(roles[i]);
                                req = req + "@" + role.getName() + " ";
                            }
                            NoPerms.Send("warn", req, txt, user);
                        }

                    } else {
                        WrongCommandUsage.send(txt, example, "You haven't mentioned any members", user);
                    }
                } else {
                    RolesNotSet.ChannelFriendly(txt, "warns", set, user);
                }
            } else {
                WrongCommandUsage.send(txt, example, "Wrong amount of args", user);
            }
        }

    }

    public static void check(String guildID, Message msg, TextChannel txt, Guild guild, User user, String contentRaw){
        try {
            Connection con = Database.warns();
            DatabaseMetaData dbm = con.getMetaData();
            ResultSet tables = dbm.getTables(null, null, guildID, null);
            if (tables.next()) {
                tables.close();
                checkCommand(guildID, msg, txt, guild, user, contentRaw, con);
            }
            else {
                tables.close();
                Create(con, guildID, txt, msg, guild, user, contentRaw);
            }
        } catch (Exception x){
            SQLError.TextChannel(txt, x);
        }
    }

}
