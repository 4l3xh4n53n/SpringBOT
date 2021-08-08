package Games.ActivityPoints.Commands;

import Core.Database;
import Core.SettingGetter;
import ErrorMessages.BadCode.SQLError;
import ErrorMessages.UserError.WrongCommandUsage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/** TODO
 * Make sure you test this and that it works as intended, make sure you also can upgrade everything and that there is no conflict between the bank size and the amount of money per ting
 * Make it so it checks to make sure your bank isn't full because that could be really really bad yeah innit and kinda would make the bank thing useless anyway
 *  Make a max level because then you can be more complete or something yeah innit, also maybe definitely add some more upgrades don't know what like though.
 *  Also link this ot the main Event Listener
 */

public class Shop {

    public static String example = "shop / shop <item> <-- just shop will show you what you can buy.";
    public static String info = "Allows you ot buy upgrades to get more coins";

    public static void NotEnoughMoney(TextChannel txt, int bal, int price){
        EmbedBuilder em = new EmbedBuilder();
        em.setColor(Color.decode(SettingGetter.ChannelFriendlySet("GuildColour", txt)));
        em.setTitle("You don't have enough coins to get this.");
        em.addField("Price:", "How much you got: " + bal + "\nHow much you need: " + price, false);
        txt.sendMessage(em.build()).queue();
    }

    public static void CoinsPerMessage(TextChannel txt, String guildID, String userID){

        try {
            Connection con = Database.coins();
            Statement stmt = con.createStatement();
            String SQL = "SELECT * FROM '" + guildID + "' WHERE userID='" + userID + "'";
            ResultSet rs = stmt.executeQuery(SQL);
            int bal = rs.getInt(2);
            int old = rs.getInt("CoinMultiplier");
            int price = old * 2;

            if (bal >= price) {
                String update = "UPDATE '" + guildID + "' SET coins = ? WHERE userID ='" + userID + "'";
                PreparedStatement ud = con.prepareStatement(update);
                ud.setInt(3, old + 1);
                ud.setInt(2, bal - price);
                ud.executeUpdate();
                ud.close();

            } else {
                NotEnoughMoney(txt, bal, price);
            }

        } catch (Exception x){
            SQLError.TextChannel(txt, x);
        }

    }

    public static void BankSize(TextChannel txt, String guildID, String userID){

        try {
            Connection con = Database.coins();
            Statement stmt = con.createStatement();
            String SQL = "SELECT * FROM '" + guildID + "' WHERE userID='" + userID + "'";
            ResultSet rs = stmt.executeQuery(SQL);
            int bal = rs.getInt(2);
            int old = rs.getInt("CoinMultiplier");
            int price = old / 2;

            if (bal >= price) {

                String update = "UPDATE '" + guildID + "' SET coins = ? WHERE userID ='" + userID + "'";
                PreparedStatement ud = con.prepareStatement(update);
                ud.setInt(4, old + 100);
                ud.setInt(2, bal - price);
                ud.executeUpdate();
                ud.close();

            } else {
                NotEnoughMoney(txt, bal, price);
            }

        } catch (Exception x){
            SQLError.TextChannel(txt, x);
        }

    }

    public static void check(TextChannel txt, String[] args, String guildID, String userID){

        if (args.length > 1){
            switch (args[1]){
                case "CoinsPerMessage":
                    CoinsPerMessage(txt, guildID, userID);
                    break;
                case "BankSize":
                    BankSize(txt, guildID, userID);
                    break;
                default:
                    WrongCommandUsage.send(txt, example, "You didn't specify an actual product");
                    break;
            }


        } else {
            EmbedBuilder em = new EmbedBuilder();
            em.setColor(Color.decode(SettingGetter.ChannelFriendlySet("GuildColour", txt)));
            em.setTitle("Shop");
            em.addField("CoinsPerMessage", "Gives you more coins every message you send", false);
            txt.sendMessage(em.build()).queue();
        }



    }

}
