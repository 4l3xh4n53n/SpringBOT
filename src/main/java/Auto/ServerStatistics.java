package Auto;

import Core.Main;
import Core.Settings.SettingGetter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ServerStatistics {

    private static void timer(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateCounters();
            }
        }, 0, 600000);
    }

    private static HashMap<JDA, List<Guild>> guilds= new HashMap<>();

    private static void updateCounters(){

        for (JDA shard : Main.getShards()){
            if (!guilds.containsKey(shard)){
                guilds.put(shard, shard.getGuilds());
            }

            // UPDATE

            for (Guild guild : guilds.get(shard)){

                if (SettingGetter.GuildFriendlyGet("ServerStats", guild).equals("1")){

                    String guildID = guild.getId();
                    VoiceChannel statsChannel = guild.getVoiceChannelById(SettingGetter.GuildFriendlyGet("StatsChannel", guild));



                }

            }

        }

    }

}
