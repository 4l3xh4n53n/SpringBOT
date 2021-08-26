package Auto;

import Core.Database;
import Core.SettingGetter;
import ErrorMessages.BadCode.SQLError;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static Core.SettingSetter.SettingChanged;

public class ChatSensor {

    private static final String info = "This command automatically gives people roles on arrival.";
    private static final String set = "`addFilter <word/s>` \n`removeFilter <word/s>`\n`setFilter <word/s>`";
    private static final String toggle = "`set module ChatFilter 1/0`";

    public static void check(String contentRaw, TextChannel txt, Message message){

        if (SettingGetter.ChannelFriendlySet("ChatFilter", txt).equals("1")){

            String[] filter = SettingGetter.ChannelFriendlySet("Filter", txt).toLowerCase().split(",");
            String better = contentRaw.toLowerCase().replaceAll("[^\\p{IsAlphabetic}]", "");

            for (String blackListedWord : filter){
                if (better.contains(blackListedWord)){

                    message.delete().queue();
                    break;

                }
            }
        }
    }

    public static void Insert(String guildID, String filter, TextChannel txt){

        try{

            Connection con = Database.connect();
            String update = "UPDATE Settings SET Filter = ? WHERE GuildID='" + guildID + "'";
            PreparedStatement ud = con.prepareStatement(update);
            SettingChanged(txt);
            ud.setString(1, filter);
            ud.executeUpdate();
            con.close();
            ud.close();

        } catch (Exception x){
            SQLError.TextChannel(txt, x, toggle);
        }

    }

    public static void SetFilter(TextChannel txt, String contentraw, String guildID){

        String[] words = contentraw.toLowerCase().replace("setfilter ", "").split(" ");
        StringBuilder current = new StringBuilder();

        for (String word : words){

            current.append(word).append(",");

        }

        Insert(guildID, current.toString(), txt);

    }

    public static void AppendFilter(TextChannel txt, String contentraw, String guildID){

        StringBuilder current = new StringBuilder(SettingGetter.ChannelFriendlySet("Filter", txt));
        String[] words = contentraw.toLowerCase().replace("addfilter ", "").split(" ");

        for (String word : words){

            current.append(word).append(",");

        }

        Insert(guildID, current.toString(), txt);

    }

    public static void RemoveFilter(TextChannel txt, String contentraw, String guildID){

        String current = SettingGetter.ChannelFriendlySet("Filter", txt);
        String[] words = contentraw.toLowerCase().replace("removefilter ", "").split(" ");

        for (String word : words){

            current = current.replace(word + "," , "");

        }

        Insert(guildID, current, txt);

    }

    public static String getInfo() {
        return info;
    }

    public static String getToggle() {
        return toggle;
    }

    public static String getSet() {
        return set;
    }

}
