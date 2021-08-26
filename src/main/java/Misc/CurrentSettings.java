package Misc;

import Core.Database;
import Core.Embed;
import ErrorMessages.BadCode.SQLError;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class CurrentSettings {

    public static void send(String guildID, TextChannel txt, User user){

        EmbedBuilder em = Embed.em(user, txt);

        try{
            Connection con = Database.connect();
            Statement stmt = con.createStatement();
            String get = "SELECT ModCommands," +
                    "LogModActions," +
                    "Coins," +
                    "SendCoins," +
                    "GuildWelcome," +
                    "AutoRole," +
                    "Poll," +
                    "InviteLogging," +
                    "PrivateChannel," +
                    "ChatFilter," +
                    "GameCommands" +
                    " FROM Settings WHERE GuildID = '" + guildID + "'";
            ResultSet rs = stmt.executeQuery(get);
            ResultSetMetaData rsMetaData = rs.getMetaData();
            int count = rsMetaData.getColumnCount();

            for (int i = 1; i < count; i++){
                String status = rs.getString(i);
                if (status.equals("1")){
                    em.addField(rsMetaData.getColumnName(i), ":green_square:",false);
                } else {
                    em.addField(rsMetaData.getColumnName(i), ":red_square:",false);
                }


            }

            con.close();
            rs.close();
            stmt.close();
        } catch (Exception x){
            SQLError.TextChannel(txt, x, "Just don't use this command");
        }

        txt.sendMessage(em.build()).queue();

    }

}
