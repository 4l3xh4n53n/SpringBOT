package Games.ActivityPoints.Commands;

import Core.Database;
import Core.Embed;
import Core.SettingGetter;
import ErrorMessages.BadCode.SQLError;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class UsersStats {

    private static final String example = "`user <@user>` <-- just user will do yourself.";
    private static final String info = "Shows a users stats.";
    private static final String toggle = "`set module SendCoins 1/0`";

    private static void CheckUser(String userID, String guildID, TextChannel txt){

        try {
            Connection con = Database.coins();
            Statement stmt = con.createStatement();
            String SQL = "SELECT * FROM '" + guildID + "' WHERE userID='" + userID + "'";
            ResultSet rs = stmt.executeQuery(SQL);

            if (!rs.next()) {

                String insert = "INSERT INTO '" + guildID + "'(userID, coins, CoinMultiplier, MaxCoins, CoinExtraPercent, Messages) VALUES(?,?,?,?,?,?)";
                PreparedStatement ps = con.prepareStatement(insert);
                ps.setString(1, userID);
                ps.setInt(2, 0);
                ps.setInt(3, 100);
                ps.setInt(4, 1000);
                ps.setDouble(5, 1.0);
                ps.setInt(6,0);
                ps.executeUpdate();
                ps.close();

            }

            rs.close();
            stmt.close();
            con.close();

        } catch (Exception x){
            SQLError.TextChannel(txt, x, toggle);
        }

    }

    public static void Send(User user, Message message, TextChannel txt, String guildID){
        if (SettingGetter.ChannelFriendlySet("Coins", txt).equals("1")) {

            List<User> mentioned = message.getMentionedUsers();
            User get;

            if (mentioned.size() > 0) {
                get = mentioned.get(0);
            } else {
                get = user;
            }

            String tag = get.getAsTag();
            String av = get.getAvatarUrl();
            String userID = get.getId();

            CheckUser(userID, guildID, txt);

            int coins = 0;
            int CoinMultiplier = 0;
            int MaxCoins = 0;
            int CoinExtraPercent = 0;
            int Messages = 0;

            try {
                Connection con = Database.coins();
                Statement stmt = con.createStatement();
                String SQL = "SELECT * FROM '" + guildID + "' WHERE userID='" + userID + "'";
                ResultSet rs = stmt.executeQuery(SQL);

                coins = rs.getInt("coins");
                CoinMultiplier = rs.getInt("CoinMultiplier");
                MaxCoins = rs.getInt("MaxCoins");
                CoinExtraPercent = rs.getInt("CoinExtraPercent");
                Messages = rs.getInt("Messages");

                rs.close();

            } catch (Exception x) {
                SQLError.TextChannel(txt, x, toggle);
            }

            EmbedBuilder em = Embed.em(user, txt);
            em.setAuthor(tag, null, av);
            em.setTitle("Messages sent: " + Messages);
            em.addField("Coins:", String.valueOf(coins), true);
            em.addField("Coins Per Message:", String.valueOf(CoinMultiplier), true);
            em.addBlankField(false);
            em.addField("Bank Size:", String.valueOf(MaxCoins), true);
            em.addField("Coin Boost:", String.valueOf(CoinExtraPercent), true);
            txt.sendMessageEmbeds(em.build()).queue();

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
