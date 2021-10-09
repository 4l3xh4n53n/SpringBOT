package Core;

import ErrorMessages.BadCode.SQLError;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SettingGetter {

    // TODO make something that changes stuff innit mate borger

    private static HashMap<String, Map<String,String>> settings = new HashMap<>();

    public static void UpdateSetting(String guildID, String setting, String value){
        settings.get(guildID).put(setting, value);
    }

    public static String GuildFriendlySet(String setting, Guild guild){
        String set = "";
        String guildID = guild.getId();

        if (settings.containsKey(guildID)){

            set = settings.get(guildID).get(setting.toLowerCase(Locale.ROOT));

        } else {
            cacheInMap(guildID, null, guild);
        }

        return set;
    }

    public static String ChannelFriendlySet(String setting, TextChannel textChannel){
        Guild guild = textChannel.getGuild();
        String guildID = guild.getId();

        if (!settings.containsKey(guildID)) {
            cacheInMap(guildID, textChannel, guild);
        }

        return settings.get(guildID).get(setting.toLowerCase(Locale.ROOT));
    }

    private static void cacheInMap(String guildID, TextChannel textChannel, Guild guild){

        Map<String, String> results = getFromDatabase(guildID);

        if (results == null) {
            if (textChannel == null) {
                SQLError.GuildFriendly(guild, exception, "Can't turn this off soz.");
            } else {
                SQLError.TextChannel(textChannel, exception, "Cant' turn this off soz.");
            }
        } else {

            settings.put(guildID, results);

        }

    }

    private static Exception exception = null;

    private static Map<String, String> getFromDatabase(String guildID){

        try {
            Connection con = Database.connect();
            Statement stmt = con.createStatement();
            String get = "SELECT * FROM Settings WHERE GuildID= '" + guildID + "'";
            ResultSet rs = stmt.executeQuery(get);

            Map<String, String> map = new HashMap<>();
            for (int i = 0; rs.getMetaData().getColumnCount() > i; i++) {

                String key = rs.getMetaData().getColumnName(i + 1);
                String value = rs.getString(i + 1);

                map.put(key, value);
            }

            con.close();
            stmt.close();
            rs.close();

            return map;

        } catch(Exception x) {
            exception = x;
            return null;
        }

    }

    /**
    public static String ChannelFriendlySet(String setting, TextChannel textChannel){
        String set = null;
        String guild = textChannel.getGuild().getId();
        setting = setting.toLowerCase(Locale.ROOT);

        SettingCreator.check(textChannel.getGuild());

        try {
            Connection con = Database.connect();
            Statement stmt = con.createStatement();
            String get = "SELECT " + setting + " FROM Settings WHERE GuildID= '" + guild + "'";
            ResultSet rs = stmt.executeQuery(get);
            set = rs.getString(setting);
            con.close();
            rs.close();
            stmt.close();

        } catch(Exception x) {
            SQLError.TextChannel(textChannel, x, "Can't turn this off sorry.");
        }

        return set;
    }

    public static String GuildFriendlySet(String setting, Guild guild){
        String set = null;
        String guildID = guild.getId();
        setting = setting.toLowerCase(Locale.ROOT);


        SettingCreator.check(guild);

        try {
            Connection con = Database.connect();
            Statement stmt = con.createStatement();
            String get = "SELECT " + setting + " FROM Settings WHERE GuildID= '" + guildID + "'";
            ResultSet rs = stmt.executeQuery(get);
            set = rs.getString(setting);
            con.close();
            rs.close();
            stmt.close();

        } catch(Exception x) {
            GuildJoinedError.SendInMain(x, guild);
        }

        return set;
    }
     **/
}
