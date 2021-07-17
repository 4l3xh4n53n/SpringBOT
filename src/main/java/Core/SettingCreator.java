/**
 Copyright © 2021 4legs

 SpringBOT for discord

 This is my bot feel free to make your own custom version of it, either to add features, or make it better or whatever you feel like doing with it.
 Do not just download it and call it yours though because that's kinda a bad move.
**/
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
        //todo you may want to keep on updating this

        try {
            PreparedStatement ps = null;
            String SQL = "INSERT INTO settings(GuildID, Prefix, GuildColour) VALUES(?,?,?)";
            ps = con.prepareStatement(SQL);
            ps.setString(1, guildID);
            ps.setString(2, "!");
            ps.setString(3, "#000000");
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
