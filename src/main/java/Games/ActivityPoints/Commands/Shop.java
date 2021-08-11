package Games.ActivityPoints.Commands;

import Core.Database;
import Core.Embed;
import Core.SettingGetter;
import ErrorMessages.BadCode.SQLError;
import ErrorMessages.UserError.WrongCommandUsage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Shop {

    public static String example = "shop / shop <item> <-- just shop will show you what you can buy.";
    public static String info = "Allows you ot buy upgrades to get more coins";

    public static void NotEnoughMoney(TextChannel txt, int bal, int price, User user){
        EmbedBuilder em = Embed.em(user, txt);
        em.setTitle("You don't have enough coins to get this.");
        em.addField("Price:", "How much you got: " + bal + "\nHow much you need: " + price, false);
        txt.sendMessage(em.build()).queue();
    }

    public static void CoinsPerMessage(TextChannel txt, String guildID, String userID, User user){

        try {
            Connection con = Database.coins();
            Statement stmt = con.createStatement();
            String SQL = "SELECT * FROM '" + guildID + "' WHERE userID='" + userID + "'";
            ResultSet rs = stmt.executeQuery(SQL);
            int bal = rs.getInt(2);
            int old = rs.getInt("CoinMultiplier");
            int price = old * 2;
            if (bal >= price) {
                String update = "UPDATE '" + guildID + "' SET coins = ?, CoinMultiplier = ? WHERE userID ='" + userID + "'";
                PreparedStatement ud = con.prepareStatement(update);
                ud.setInt(2, old + 1);
                ud.setInt(1, bal - price);
                ud.executeUpdate();
                ud.close();

            } else {
                NotEnoughMoney(txt, bal, price, user);
            }

        } catch (Exception x){
            SQLError.TextChannel(txt, x);
        }

    }

    public static void BankSize(TextChannel txt, String guildID, String userID, User user){

        try {
            Connection con = Database.coins();
            Statement stmt = con.createStatement();
            String SQL = "SELECT * FROM '" + guildID + "' WHERE userID='" + userID + "'";
            ResultSet rs = stmt.executeQuery(SQL);
            int bal = rs.getInt(2);
            int old = rs.getInt("CoinMultiplier");
            int price = old / 2;

            if (bal >= price) {

                String update = "UPDATE '" + guildID + "' SET coins = ? , MaxCoins = ? WHERE userID ='" + userID + "'";
                PreparedStatement ud = con.prepareStatement(update);
                ud.setInt(2, old + 100);
                ud.setInt(1, bal - price);
                ud.executeUpdate();
                ud.close();

            } else {
                NotEnoughMoney(txt, bal, price, user);
            }

        } catch (Exception x){
            SQLError.TextChannel(txt, x);
        }

    }

    public static void check(TextChannel txt, String content, String guildID, String userID, User user){

        String[] args = content.split("\\s+");

        if (args.length > 1){
            switch (args[1]){
                case "CoinsPerMessage":
                    CoinsPerMessage(txt, guildID, userID, user);
                    break;
                case "BankSize":
                    BankSize(txt, guildID, userID, user);
                    break;
                default:
                    WrongCommandUsage.send(txt, example, "You didn't specify an actual product", user);
                    break;
            }


        } else {
            EmbedBuilder em = Embed.em(user, txt);
            em.setTitle("Shop");
            em.addField("CoinsPerMessage", "Gives you more coins every message you send", false);
            em.addField("BankSize", "Allows you to store more coins", false);
            txt.sendMessage(em.build()).queue();
        }



    }

}
