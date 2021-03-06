package Games.ActivityPoints.Commands;

import Core.Database;
import Core.Embed;
import Core.Settings.SettingGetter;
import ErrorMessages.BadCode.SQLError;
import ErrorMessages.UserError.WrongCommandUsage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import org.codehaus.plexus.util.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class Send {

    private static final String example = "`send <@user> <amount>`";
    private static final String info = "Send users coins.";
    private static final String toggle = "`set SendCoins 1/0`";

    private static int CheckUser(String userID, String guildID, TextChannel txt){
        int coins = 0;

        try {
            Connection con = Database.coins();
            Statement stmt = con.createStatement();
            String SQL = "SELECT * FROM `" + guildID + "` WHERE userID='" + userID + "'";
            ResultSet rs = stmt.executeQuery(SQL);
            if (!rs.next()) {

                String insert = "INSERT INTO `" + guildID + "`(userID, coins, CoinMultiplier, MaxCoins, CoinExtraPercent) VALUES(?,?,?,?,?)";
                PreparedStatement ps = con.prepareStatement(insert);
                ps.setString(1, userID);
                ps.setInt(2, 0);
                ps.setInt(3, 100);
                ps.setInt(4, 1000);
                ps.setDouble(5, 1.0);
                ps.executeUpdate();
                ps.close();

            } else {
                coins  = rs.getInt("coins");
            }

            rs.close();
            stmt.close();
            con.close();

        } catch (Exception x){
            SQLError.TextChannel(txt, x, toggle);
        }

        return coins;

    }

    public static void Check(User user, Message message, String guildID, TextChannel txt, String content) {

        List<User> mentions = message.getMentionedUsers();
        String[] args = content.split("\\s+");

        if (SettingGetter.ChannelFriendlyGet("Coins", txt).equals("1") && SettingGetter.ChannelFriendlyGet("SendCoins", txt).equals("1")) {
            if (args.length > 2) {
                if (mentions.size() > 0) {
                    if (StringUtils.isNumeric(args[2]) && Integer.parseInt(args[2]) > 0) {

                        int sum = Integer.parseInt(args[2]);

                        User mentioned = mentions.get(0);
                        String donorID = user.getId();
                        String recipientID = mentioned.getId();
                        String tag = mentioned.getAsTag();

                        int donorBal = CheckUser(donorID, guildID, txt);
                        int recipientBal = CheckUser(recipientID, guildID, txt);

                        if (donorBal > sum) {

                            try {

                                // DONOR

                                Connection con = Database.coins();
                                String update = "UPDATE `" + guildID + "` SET coins = ? WHERE userID ='" + donorID + "'";
                                PreparedStatement ud = con.prepareStatement(update);
                                ud.setInt(1, donorBal - sum);
                                ud.executeUpdate();
                                ud.close();

                                // RECIPIENT

                                String update1 = "UPDATE `" + guildID + "` SET coins = ? WHERE userID ='" + recipientID + "'";
                                PreparedStatement ud1 = con.prepareStatement(update1);
                                ud1.setInt(1, recipientBal + sum);
                                ud1.executeUpdate();
                                ud1.close();

                                EmbedBuilder em = Embed.em(user, txt);
                                em.addField("You paid " + tag + " " + sum, "You new balance: " + (donorBal - sum), false);
                                txt.sendMessageEmbeds(em.build()).queue();

                            } catch (Exception x) {
                                SQLError.TextChannel(txt, x, toggle);
                            }

                        } else {
                            Shop.NotEnoughMoney(txt, donorBal, sum, user);
                        }
                    } else {
                        WrongCommandUsage.send(txt, example, "You didn't enter a number", user);
                    }
                } else {
                    WrongCommandUsage.send(txt, example, "You didn't mention anyone", user);
                }
            } else {
                WrongCommandUsage.send(txt, example, "You didn't include enough args", user);
            }

        }
    }

    public static String getExample() {
        return example;
    }

    public static String getInfo() {
        return info;
    }

    public static String getToggle() {
        return toggle;
    }
}
