package Games.ActivityPoints.Core;

import Core.Database;
import Core.Settings.SettingGetter;
import ErrorMessages.BadCode.SQLError;
import Games.ActivityPoints.Commands.Shop;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class PointsHandler {

    private static void Create(String guildID, TextChannel txt, Connection con){

        try {
            Statement stmt = con.createStatement();
            String sql = "CREATE TABLE `" + guildID + "` (userID TEXT, coins INTEGER, CoinMultiplier INTEGER , MaxCoins INTEGER, CoinExtraPercent INTEGER, Messages INTEGER)";
            stmt.executeUpdate(sql);
            con.close();
            stmt.close();

        } catch (Exception x){
            SQLError.TextChannel(txt, x, Shop.getToggle());
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
            SQLError.TextChannel(txt, x, Shop.getToggle());
        }

    }

    private static void checkUser(String userID, String guildID, TextChannel txt){
        try {
            Connection con = Database.coins();
            Statement stmt = con.createStatement();
            String SQL = "SELECT * FROM `" + guildID + "` WHERE userID='" + userID + "'";
            ResultSet rs = stmt.executeQuery(SQL);

            if (!rs.next()) {

                String insert = "INSERT INTO `" + guildID + "`(userID,coins, CoinMultiplier, MaxCoins, CoinExtraPercent, Messages) VALUES(?,?,?,?,?,?)";
                PreparedStatement ps = con.prepareStatement(insert);
                ps.setString(1, userID);
                ps.setInt(2, 100);
                ps.setInt(3, 100);
                ps.setInt(4,10000);
                ps.setDouble(5, 1.0);
                ps.setInt(6,1);
                ps.executeUpdate();
                ps.close();

            } else {

                try {

                    int coinsPerMessage = rs.getInt("CoinMultiplier");
                    int extraPercentage = rs.getInt("CoinExtraPercent");

                    int oldcoins = rs.getInt("coins");
                    int newcoins = Math.round(oldcoins + coinsPerMessage * extraPercentage);

                    if (newcoins > rs.getInt("MaxCoins")) {
                        newcoins = rs.getInt("MaxCoins");
                    }

                    int oldmessages = rs.getInt("Messages");
                    int newmessages = oldmessages + 1;

                    String update = "UPDATE `" + guildID + "` SET coins = ?, Messages = ? WHERE userID ='" + userID + "'";
                    PreparedStatement ud = con.prepareStatement(update);
                    ud.setInt(1, newcoins);
                    ud.setInt(2, newmessages);
                    ud.executeUpdate();
                    ud.close();


                } catch (Exception x){
                    SQLError.TextChannel(txt, x, Shop.getToggle());
                }

            }

            stmt.close();
            con.close();

        } catch (Exception x){
            x.printStackTrace();
            SQLError.TextChannel(txt, x, Shop.getToggle());
        }
    }

    public static void add(User user, Guild guild, TextChannel txt){
        if (SettingGetter.ChannelFriendlyGet("Coins", txt).equals("1")) {
            String guildID = guild.getId();
            String userID = user.getId();

            checkGuild(guildID, txt);
            checkUser(userID, guildID, txt);
        }
    }

}
