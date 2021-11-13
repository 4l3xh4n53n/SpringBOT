package Utility;

import net.dv8tion.jda.api.entities.Guild;

public class CheckChannel {

    // Checks if either an id is a legitimate text channel voice channel or category. Because there are multiple objects they can't be returned

    public static boolean check(Guild guild, String id) {

        boolean textChannel = textChannel(guild, id);
        boolean voiceChannel = voiceChannel(guild, id);
        boolean category = category(guild, id);

        if (textChannel || voiceChannel || category) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean textChannel(Guild guild, String id) {
        boolean exists = false;
        try {
            guild.getTextChannelById(id);
            exists = true;
        } catch (Exception ignored) {}
        return exists;
    }

    private static boolean voiceChannel(Guild guild, String id){
        boolean exists = false;
        try{
            guild.getVoiceChannelById(id);
            exists = true;
        } catch (Exception ignored) {}
        return exists;
    }

    private static boolean category(Guild guild, String id){
        boolean exists = false;
        try{
            guild.getCategoryById(id);
            exists = true;
        } catch (Exception ignored){}
        return exists;
    }

}
