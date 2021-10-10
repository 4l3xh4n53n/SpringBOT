package Auto;

import Core.Database;
import Core.Settings.SettingGetter;
import ErrorMessages.BadCode.SQLError;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static Core.Settings.SettingSetter.SettingChanged;

public class ChatSensor {

    private static final String info = "This deletes messages with specified words in";
    private static final String set = "`addFilter <word(s)>` \n`removeFilter <word(s)>`\n`setFilter <word(s)>`";
    private static final String toggle = "`set ChatFilter 1/0`";

    public static void check(String contentRaw, TextChannel txt, Message message){

        if (SettingGetter.ChannelFriendlyGet("ChatFilter", txt).equals("1")){

            String[] filter = SettingGetter.ChannelFriendlyGet("Filter", txt).toLowerCase().split(",");
            String better = contentRaw.toLowerCase().replaceAll("[^\\p{IsAlphabetic}]", "");

            if (filter.length >= 1 && !filter[0].equals("")){
                for (String blackListedWord : filter) {
                    if (better.contains(blackListedWord)) {
                        message.delete().queue();
                        break;
                    }
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

    public static void SetFilter(TextChannel txt, String contentraw, String guildID, Member member){

        if (member.getPermissions().contains(Permission.ADMINISTRATOR)) {

            String[] words = contentraw.toLowerCase().replace("setfilter ", "").split(" ");
            StringBuilder current = new StringBuilder();

            for (String word : words) {

                current.append(word).append(",");

            }

            Insert(guildID, current.toString(), txt);

        }
    }

    public static void AppendFilter(TextChannel txt, String contentraw, String guildID, Member member){

        if (member.getPermissions().contains(Permission.ADMINISTRATOR)) {

            StringBuilder current = new StringBuilder(SettingGetter.ChannelFriendlyGet("Filter", txt));
            String[] words = contentraw.toLowerCase().replace("addfilter ", "").split(" ");

            for (String word : words) {

                current.append(word).append(",");

            }

            Insert(guildID, current.toString(), txt);

        }

    }

    public static void RemoveFilter(TextChannel txt, String contentraw, String guildID, Member member) {

        if (member.getPermissions().contains(Permission.ADMINISTRATOR)){

            String current = SettingGetter.ChannelFriendlyGet("Filter", txt);
            String[] words = contentraw.toLowerCase().replace("removefilter ", "").split(" ");

            for (String word : words) {

                current = current.replace(word + ",", "");

            }

            Insert(guildID, current, txt);

        }

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
