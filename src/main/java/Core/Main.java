package Core;

import Auto.ServerStatistics;
import EventListeners.*;
import Games.ActivityPoints.Core.UserTimeOut;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MiscUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static final ArrayList<JDA> Shards = new ArrayList<>();
    private static final int shardCount = 1;

    public static void main(String[] args) throws Exception {

        File tokenFile = new File("TOKEN");
        Scanner sc = new Scanner(tokenFile);
        String token = sc.nextLine();

        JDABuilder shardBuilder = JDABuilder.createDefault(token);
        ListenerAdapter[] listeners = new ListenerAdapter[]{new NewGuild(), new MessageReceived(), new NewMember(), new GuildInviteCreated(), new EntersVoiceChannel(), new MessageReaction()};
        shardBuilder.addEventListeners((Object[]) listeners);
        shardBuilder.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_INVITES);

        for (int i = 0; i < shardCount; i++) {
            Shards.add(shardBuilder.useSharding(i, shardCount)
                    .build());
        }

        for (JDA Shard : Shards){
            Shard.getPresence().setPresence(OnlineStatus.ONLINE, Activity.watching("Your DISCORD server to keep you safe :)"));
            Shard.awaitReady();
        }

        UserTimeOut.timer();
        ServerStatistics.timer();

    }

    /**
     * This gets the shard with the least servers on it
     * @return shard
     */
    public static JDA getLightestShard(){

        JDA lowest = getShards().get(0);
        int count = lowest.getGuilds().size();

        for (JDA shard : getShards()){
            int size = shard.getGuilds().size();
            if (size < count){
                lowest = shard;
                count = size;
            }
        }

        return lowest;

    }

    /**
     * This gets the shard a guild is currently on
     * @param guild find its shard
     * @return shard
     */
    public static JDA getCurrentShard(Guild guild){
        int shardNumber = MiscUtil.getShardForGuild(guild.getIdLong(), shardCount);
        return Shards.get(shardNumber);
    }

    /**
     * Gets a list of all the running shards
     * @return List of shards
     */
    public static ArrayList<JDA> getShards(){
        return Shards;
    }

    /**
     * Gets the amount of shards running
     * @return count
     */
    public static int getShardsCount(){
        return shardCount;
    }

}
