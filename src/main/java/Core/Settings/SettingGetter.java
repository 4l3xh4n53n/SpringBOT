package Core.Settings;

import Core.Database;
import Core.Main;
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

    private static final HashMap<String, Map<String,String>> settings = new HashMap<>();

    protected static void UpdateSetting(String guildID, String setting, String value){
        settings.get(guildID).put(setting.toLowerCase(Locale.ROOT), value);
    }

    public static String GuildFriendlyGet(String setting, Guild guild){
        String guildID = guild.getId();

        if (!settings.containsKey(guildID)){
            cacheInMap(guildID, null, guild);
        }

        return settings.get(guildID).get(setting.toLowerCase(Locale.ROOT));
    }

    public static String ChannelFriendlyGet(String setting, TextChannel textChannel){
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

        SettingCreator.check(Main.getShard().getGuildById(guildID));

        try {

            Connection con = Database.connect();
            Statement stmt = con.createStatement();
            String get = "SELECT * FROM Settings WHERE GuildID= '" + guildID + "'";
            ResultSet rs = stmt.executeQuery(get);

            Map<String, String> map = new HashMap<>();
            rs.next();
            for (int i = 0; rs.getMetaData().getColumnCount() > i; i++) {

                String key = rs.getMetaData().getColumnName(i + 1).toLowerCase(Locale.ROOT);
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

}
