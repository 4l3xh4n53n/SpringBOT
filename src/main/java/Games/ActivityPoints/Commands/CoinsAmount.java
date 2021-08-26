package Games.ActivityPoints.Commands;

import Core.Database;
import Core.Embed;
import Core.Main;
import Core.SettingGetter;
import ErrorMessages.BadCode.SQLError;
import Games.ActivityPoints.Core.PointsHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class CoinsAmount {

    private static final String example = "`coins <@user/userID> `";
    private static final String info = "Gets the coins that a user has";
    private static final String set = "`set roles CheckCoins <@role(S)>`";
    private static final String toggle = "`set module Coins 1/0`";

    public static void send(TextChannel txt, User user, int amount){

        String tag = user.getAsTag();
        String pfp = user.getAvatarUrl();

        EmbedBuilder em = Embed.em(user, txt);
        em.setAuthor(tag, null, pfp);
        em.addField("Has this many coins:", String.valueOf(amount), false);
        txt.sendMessage(em.build()).queue();

    }

    public static void checkUser(String userID, String guildID, TextChannel txt, User user){
        try {
            Connection con = Database.coins();
            Statement stmt = con.createStatement();
            String SQL = "SELECT * FROM '" + guildID + "' WHERE userID='" + userID + "'";
            ResultSet rs = stmt.executeQuery(SQL);
            if (!rs.next()) {

                String insert = "INSERT INTO '" + guildID + "'(userID, coins, CoinMultiplier, MaxCoins, CoinExtraPercent) VALUES(?,?,?,?,?)";
                PreparedStatement ps = con.prepareStatement(insert);
                ps.setString(1, userID);
                ps.setInt(2, 0);
                ps.setInt(3, 100);
                ps.setInt(4, 1000);
                ps.setDouble(5, 1.0);
                ps.executeUpdate();
                ps.close();
                send(txt, user, 0);

            } else {

                int amount = rs.getInt("coins");
                send(txt, user, amount);

            }

            stmt.close();
            con.close();

        } catch (Exception x){
            SQLError.TextChannel(txt, x, toggle);
        }
    }

    public static void get(User user, Guild guild, TextChannel txt, Message msg, String content){

        if (SettingGetter.ChannelFriendlySet("Coins", txt).equals("1")) {

            String guildID = guild.getId();
            String userID = null;
            String[] args = content.split("\\s+");
            JDA jda = Main.getCurrentShard(guild);
            int check = 0;
            User mentioned;

            //Decides whether their mentioned or have used ID

            try {
                userID = msg.getMentions().get(0).getId();
                check = 1;
            } catch (Exception ignored) {
            }

            try {
                jda.retrieveUserById(args[1]);
                userID = args[1];
                check = 1;
            } catch (Exception ignored) {
            }

            if (check == 1) {

                mentioned = jda.retrieveUserById(userID).complete();
                PointsHandler.checkGuild(guildID, txt);
                checkUser(userID, guildID, txt, mentioned);

            } else {
                mentioned = user;
                userID = user.getId();
                PointsHandler.checkGuild(guildID, txt);
                checkUser(userID, guildID, txt, mentioned);

            }

        }

    }

    public static String getExample() {
        return example;
    }

    public static String getInfo() {
        return info;
    }

    public static String getSet() {
        return set;
    }

    public static String getToggle() {
        return toggle;
    }
}
