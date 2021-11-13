package Auto;

import Core.Main;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;

import java.util.*;

public class ServerStatistics {

    private static final String info = "This module creates a counter for how many users are in your guild";
    private static final String set = "`set StatsTotal 1/0 \nset StatsBot 1/0\nset StatsMember 1/0`";
    private static final String toggle = "`set ServerStats 1/0`";

    public static void timer(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateCounters();
            }
        }, 0, 600000);
    }

    private static final HashMap<JDA, List<Guild>> guilds= new HashMap<>();

    private static void updateCounters(){

        String[] channelTypes = {"statsTotal", "statsBot", "statsMember"};

        for (JDA shard : Main.getShards()){
            if (!guilds.containsKey(shard)){
                guilds.put(shard, shard.getGuilds());
            }



        }
    }
}
