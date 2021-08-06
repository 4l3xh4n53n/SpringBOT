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
            PreparedStatement ps = null;
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
                    ")VALUES(?,?,?,?,?,?,?,?,?,?,?)";
            ps = con.prepareStatement(SQL);
            ps.setString(1, guildID);
            ps.setString(2, "!"); //prefix
            ps.setString(3, "#000000"); //defaultcolour
            ps.setString(7, "1"); //modcommands
            ps.setString(11, "1");//moderationlogs
            ps.executeUpdate();
            con.close();
            ps.close();

        } catch (Exception x){
            GuildJoinedError.DMOwner(x, g);
        }
    }

    public static void check(Guild g){ // something in here is broken

        String guildID = g.getId();

        try {
            Connection con = Database.connect();
            Statement stmt = con.createStatement();
            String SQL = "SELECT * FROM Settings WHERE GuildID='" + guildID + "'";
            ResultSet rs = stmt.executeQuery(SQL);

            if (rs.next()) {

                con.close();
                stmt.close();
                rs.close();

            } else {
                CreateSettings(g ,guildID, con);
                stmt.close();
                rs.close();
            }

        } catch (Exception x){
            GuildJoinedError.DMOwner(x, g);
        }
    }

}
