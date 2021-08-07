package Games.ActivityPoints.Core;

import Core.Database;
import ErrorMessages.BadCode.SQLError;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.sql.*;

public class PointsHandler {

    public static void Create(String guildID, TextChannel txt, Connection con){

        //apparently this just refuses to make one even if its already in there and this really wasn't needed

        try {
            Statement stmt = con.createStatement();
            String sql = "CREATE TABLE '" + guildID + "' (userID TEXT NOT NULL, coins INTEGER PRIMARY KEY)";
            stmt.executeUpdate(sql);
            con.close();
            stmt.close();

        } catch (Exception x){
            System.out.println("bruh " + x);
            SQLError.TextChannel(txt, x);
        }
    }

    public static void checkGuild(String guildID, TextChannel txt){

        try {
            Connection con = Database.coins();
            DatabaseMetaData dbm = con.getMetaData();
            ResultSet tables = dbm.getTables(null, null, guildID, null);
            if (!tables.next()) {
                Create(guildID, txt, con);
            }
            tables.close();
            con.close();

        } catch (Exception x){
            SQLError.TextChannel(txt, x);
        }

    }

    public static void checkUser(String userID, String guildID, TextChannel txt){
        try {
            Connection con = Database.coins();
            Statement stmt = con.createStatement();
            String SQL = "SELECT * FROM '" + guildID + "' WHERE userID='" + userID + "'";
            ResultSet rs = stmt.executeQuery(SQL);
            if (!rs.next()) {

                String insert = "INSERT INTO '" + guildID + "'(userID,coins, CoinMultiplier) VALUES(?,?,?)";
                PreparedStatement ps = con.prepareStatement(insert);
                ps.setString(1, userID);
                ps.setInt(2, 1);
                ps.setInt(3, 0);
                ps.executeUpdate();
                ps.close();
            } else {

                try {
                    int oldcoins = rs.getInt("coins");
                    int newcoins = oldcoins + 1;

                    String update = "UPDATE '" + guildID + "' SET coins = ? WHERE userID ='" + userID + "'";
                    PreparedStatement ud = con.prepareStatement(update);
                    ud.setInt(1, newcoins);
                    ud.executeUpdate();
                    ud.close();

                } catch (Exception x){
                    SQLError.TextChannel(txt, x);
                }

            }
            stmt.close();
            con.close();
        } catch (Exception x){
            SQLError.TextChannel(txt, x);
        }
    }

    public static void add(User user, Guild guild, TextChannel txt){
        String guildID = guild.getId();
        String userID = user.getId();

        checkGuild(guildID, txt);
        checkUser(userID, guildID, txt);

    }

}
