package Games.ActivityPoints.Commands;

import Core.Database;
import Core.SettingGetter;
import ErrorMessages.BadCode.SQLError;
import ErrorMessages.UserError.WrongCommandUsage;
import Games.ActivityPoints.Core.PointsHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class CoinsAmount {

    public static String example = "`coins <@user/userID> `";
    public static String info = "Gets the coins that a user has";
    public static String set = "`set roles CheckCoins <@role(S)>`";

    public static void send(TextChannel txt, User user, int amount){

        String tag = user.getAsTag();
        String pfp = user.getAvatarUrl();

        EmbedBuilder em = new EmbedBuilder();
        em.setAuthor(tag, null, pfp);
        em.setColor(Color.decode(SettingGetter.ChannelFriendlySet("GuildColour", txt)));
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

                String insert = "INSERT INTO '" + guildID + "'(userID,coins, CoinMultiplier) VALUES(?,?,?)";
                PreparedStatement ps = con.prepareStatement(insert);
                ps.setString(1, userID);
                ps.setInt(2, 1);
                ps.setInt(3, 1);
                ps.setInt(4, 100);
                ps.executeUpdate();
                ps.close();

            } else {

                int amount = rs.getInt("coins");
                send(txt, user, amount);

            }

            stmt.close();
            con.close();

        } catch (Exception x){
            SQLError.TextChannel(txt, x);
        }
    }

    public static void get(User user, Guild guild, TextChannel txt, Message msg, String content){
        String guildID = guild.getId();
        String userID = null;
        String[] args = content.split("\\s+");
        JDA jda = guild.getJDA();
        int check = 0;
        User mentioned = null;

        //Decides whether their mentioned or have used ID

        try {
            userID = msg.getMentions().get(0).getId();
            check = 1;
        } catch (Exception ignored) {}

        try {
            jda.retrieveUserById(args[1]);
            userID = args[1];
            check = 1;
        } catch (Exception ignored){}

        if (check == 1){

            mentioned = jda.retrieveUserById(userID).complete();
            PointsHandler.checkGuild(guildID, txt);
            checkUser(userID, guildID, txt, mentioned);

        } else {
            WrongCommandUsage.send(txt, example, "You didn't mention a user");
        }
    }

}
