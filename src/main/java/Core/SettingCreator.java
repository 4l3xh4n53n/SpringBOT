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
                    "GuildColour, " +
                    "KickRoles, " +
                    "BanRoles, " +
                    "WarnRoles, " +
                    "ModCommands, " +
                    "KickLog, " +
                    "BanLog, " +
                    "WarnLog, " +
                    "LogModActions " +
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
                    "PrivateChannelCategory" +
                    ")VALUES(?,?,?,?,?,?,?,?,?,?,?)";
            ps = con.prepareStatement(SQL);
            ps.setString(1, guildID);
            ps.setString(2, "!"); //Prefix
            ps.setString(3, "#000000"); //GuildColour
            ps.setString(7, "1"); //ModCommands
            ps.setString(11, "0");//LogModActions
            ps.setString(12, "1");//Coins
            ps.setString(13, "1");//SendCoins
            ps.setString(14, "0");//GuildWelcome
            ps.setString(17, "0");//AutoRole
            ps.setString(19, "0");//Poll
            ps.setString(21, "0");//InviteLogging
            ps.setString(23, "0");//PrivateChannel
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
