package Core;

import ErrorMessages.BadCode.GuildJoinedError;
import net.dv8tion.jda.api.entities.Guild;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class SettingCreator{

    // This creates default settings when the bot joins a new guild or when a feature that uses settings is used.

    public static void CreateSettings(Guild g,String guildID, Connection con){

        try {
            PreparedStatement ps;
            String SQL = "INSERT INTO settings(" +
                    "GuildID, " +
                    "Prefix, " +
                    "GuildColour," +
                    "ClearRoles, " +
                    "KickRoles, " +
                    "BanRoles, " +
                    "WarnRoles, " +
                    "ModCommands, " +
                    "KickLog, " +
                    "BanLog, " +
                    "WarnLog, " +
                    "LogModActions, " +
                    "Coins," +
                    "SendCoins," +
                    "GuildWelcome," +
                    "GuildWelcomeMessage," +
                    "GuildWelcomeIMAGE," +
                    "AutoRole," +
                    "AutoRoleRole," +
                    "Poll," +
                    "PollRole," +
                    "InviteLogging," +
                    "InviteLog," +
                    "PrivateChannel," +
                    "PrivateChannelCreator," +
                    "PrivateChannelCategory," +
                    "ServerStats," +
                    "StatsChannel," +
                    "ChatFilter," +
                    "Filter," +
                    "ReactionRoleRoles" +
                    ")VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = con.prepareStatement(SQL);
            ps.setString(1, guildID);
            ps.setString(2, "!"); //Prefix
            ps.setString(3, "#000000"); //GuildColour
            ps.setString(4, "");//ClearRoles
            ps.setString(5, ""); //KickRoles
            ps.setString(6, "");//BanRoles
            ps.setString(7,"");//WarnRoles
            ps.setString(8, "1"); //ModCommands
            ps.setString(9,  "");//KickLog
            ps.setString(10, "");//BanLog
            ps.setString(11, "");//WarnLog
            ps.setString(12, "0");//LogModActions
            ps.setString(13, "1");//Coins
            ps.setString(14, "1");//SendCoins
            ps.setString(15, "0");//GuildWelcome
            ps.setString(16, "");//GuildWelcomeMessage
            ps.setString(17, "");//GuildWelcomeImage
            ps.setString(18, "0");//AutoRole
            ps.setString(19, "");//AutoRoleRole
            ps.setString(20, "0");//Poll
            ps.setString(21, "");//PollRole
            ps.setString(22, "0");//InviteLogging
            ps.setString(23, "");//InviteLog
            ps.setString(24, "0");//PrivateChannel
            ps.setString(25, "");//PrivateChannelCreator
            ps.setString(26, "");//PrivateChannelCategory
            ps.setString(27, "0");//ServerStats
            ps.setString(28, "");//StatsChannel
            ps.setString(29, "0");//ChatFilter
            ps.setString(30, "");//Filter
            ps.setString(31, "");//ReactionRoleRoles
            ps.executeUpdate();
            ps.close();

        } catch (Exception x){
            GuildJoinedError.DMOwner(x, g);
        }

    }

    public static void check(Guild g){

        String guildID = g.getId();

        try {
            Connection con = Database.connect();
            Statement stmt = con.createStatement();
            String SQL = "SELECT * FROM Settings WHERE GuildID='" + guildID + "'";
            ResultSet rs = stmt.executeQuery(SQL);

            if (!rs.next()) {

                CreateSettings(g, guildID, con);

            }
            con.close();
            stmt.close();
            rs.close();

        } catch (Exception x){
            GuildJoinedError.DMOwner(x, g);
        }
    }

}
