package commands.mod;

import Core.Database;
import Core.ModLogger;
import Core.SettingGetter;
import ErrorMessages.BadCode.SQLError;
import ErrorMessages.UserError.NoPerms;
import ErrorMessages.UserError.WrongCommandUsage;
import net.dv8tion.jda.api.entities.*;
import org.apache.commons.collections4.CollectionUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Warn {

    public static String example(){
        String example = "`warn <@user/userID> <reason>` <-- (optional)";
        return example;
    }

    public static String info(){
        String info = "Warns a user";
        return info;
    }

    public static String set(){
        String set = "`set roles WarnRoles <@role(S)>`";
        return set;
    }

    public static String log(){
        String log = "`set channel WarnLog <channelID>`";
        return log;
    }

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

            ModLogger.log(txt, user, "WarnLog", reason, log(), "was warned", executor);

        } catch (Exception x){
            SQLError.TextChannel(txt, x); // this is buggin
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
            SQLError.TextChannel(txt, x);
        }
    }

    public static void checkCommand(Guild guild, String guildID, User user, TextChannel txt, Message msg, Connection con){

        String[] args = msg.getContentRaw().split("\\s+");
        String[] roles = SettingGetter.ChannelFriendlySet("WarnRoles", txt).split(",");
        List<Role> userroles = guild.getMember(user).getRoles();
        List<String> usersRoles = new ArrayList<>();
        String req = "";
        int check = 0;
        Member mentioned = null;

        if (args.length > 1){

            try {
                guild.retrieveMemberById(args[1]).complete();
                check = 1;
            } catch (Exception ignored){}

            if (check == 1 || msg.getMentionedMembers().size() > 0){
                if (check == 1){
                    mentioned = guild.retrieveMemberById(args[1]).complete();
                } else {
                    mentioned = msg.getMentionedMembers().get(0);
                }

                for (int i = 0; userroles.size() > i; i++) {
                    usersRoles.add(userroles.get(i).getId());
                }

                if (CollectionUtils.containsAny(Arrays.asList(roles), usersRoles)){
                    Execute(guildID, txt, mentioned, con, msg);
                } else {
                    for (int i = 0; roles.length > i; i++) {
                        Role role = guild.getRoleById(roles[i]);
                        req = req + "@" + role.getName() + " ";
                    }
                    NoPerms.Send("warn", req, txt);
                }


            } else {
                WrongCommandUsage.send(txt, example(), "You haven't mentioned any members", "warn");
            }
        } else {
            WrongCommandUsage.send(txt, example(), "Wrong amount of args", "warn");
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
            SQLError.TextChannel(txt, x);
        }
    }

}