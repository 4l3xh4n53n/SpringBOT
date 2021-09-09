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

    private static final String example = "`warns <@user>";
    private static final String info = "Shows you how many warns the specified user has";
    private static final String set = "`set roles WarnRoles <@role(S)>`";
    private static final String toggle = "`set module ModCommands 1/0`";

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

            txt.sendMessageEmbeds(em.build()).queue();
        } catch (Exception x){
            SQLError.TextChannel(txt, x, toggle);
        }

    }

    public static void Create(Connection con, String guildID, TextChannel txt, Message msg, Guild guild, User user, String contentRaw, Member member){
        try {
            Statement stmt = con.createStatement();
            String sql = "CREATE TABLE '" + guildID + "' (userID TEXT NOT NULL, warns INTEGER PRIMARY KEY)";
            stmt.executeUpdate(sql);
            stmt.close();

            checkCommand(guildID, msg, txt, guild, user, contentRaw, con, member);

        } catch (Exception x){
            SQLError.TextChannel(txt, x, toggle);
        }
    }

    public static void checkCommand(String GuildID, Message msg, TextChannel txt, Guild guild, User user, String contentRaw, Connection con, Member member){

        String[] roles = SettingGetter.ChannelFriendlySet("WarnRoles", txt).split(",");
        List<Role> userroles = member.getRoles();
        List<String> usersRoles = new ArrayList<>();
        String[] args = contentRaw.split("\\s+");
        Member mentioned;
        StringBuilder req = new StringBuilder();

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

                    for (Role userrole : userroles) {
                        usersRoles.add(userrole.getId());
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
                    RolesNotSet.ChannelFriendly(txt, "warns", set, user, toggle);
                }
            } else {
                WrongCommandUsage.send(txt, example, "Wrong amount of args", user);
            }
        }

    }

    public static void check(String guildID, Message msg, TextChannel txt, Guild guild, User user, String contentRaw, Member member){
        try {
            Connection con = Database.warns();
            DatabaseMetaData dbm = con.getMetaData();
            ResultSet tables = dbm.getTables(null, null, guildID, null);
            if (tables.next()) {
                tables.close();
                checkCommand(guildID, msg, txt, guild, user, contentRaw, con, member);
            }
            else {
                tables.close();
                Create(con, guildID, txt, msg, guild, user, contentRaw, member);
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

    public static String getToggle() {
        return toggle;
    }
}
