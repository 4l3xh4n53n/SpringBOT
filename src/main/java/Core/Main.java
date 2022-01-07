package Core;

import Auto.ServerStatistics;
import EventListeners.*;
import Games.ActivityPoints.Core.UserTimeOut;
import Utility.IsNumeric;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.util.Scanner;

public class Main {

    private static final int shardCount = 1;
    private static JDA shard = null;

    /**
     * NOTE ** This is currently set up to only run one shard of the discord bot per instance, a few minor changes will be required to run the bot normally **
     */
    public static void main(String[] args) throws Exception {

        File mariaPass = new File("MARIA");
        Scanner sc = new Scanner(mariaPass);
        Database.setMariaDBpass(sc.nextLine());
        sc.close();

        File tokenFile = new File("TOKEN");
        Scanner scc = new Scanner(tokenFile);
        String token = scc.nextLine();
        scc.close();

        Connection con = Database.connect();
        while (con == null){

            con = Database.connect();

            try{
                if (con == null){
                    Thread.sleep(10000);
                }
            } catch (Exception x){
                x.printStackTrace();
            }

        }
        con.close();

        // API that sends a shard to the instance

        /* Remove this if you want to run without scaling and replace the for loop
         * remove for no scaling start */

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://192.168.1.108:5000/getShard"))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int i = Integer.parseInt(response.body());

         /* remove for no scaling end */

        JDABuilder shardBuilder = JDABuilder.createDefault(token);
        ListenerAdapter[] listeners = new ListenerAdapter[]{new NewGuild(), new MessageReceived(), new NewMember(), new GuildInviteCreated(), new EntersVoiceChannel(), new MessageReaction()};
        shardBuilder.addEventListeners((Object[]) listeners);
        shardBuilder.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_INVITES);

        //for (int i = 0; i < shardCount; i++) {
            shard = shardBuilder.useSharding(i, shardCount)
                    .build();
        //}

        shard.getPresence().setPresence(OnlineStatus.ONLINE, Activity.watching("Your DISCORD server to keep you safe :)"));
        shard.awaitReady();

        UserTimeOut.timer();
        ServerStatistics.timer();

    }

    /**
     * Gets the shard that this instance of the discord bot is running
     * @return shard
     */
    public static JDA getShard(){
        return shard;
    }

    /**
     * Gets the amount of shards running
     * @return count
     */
    public static int getShardsCount(){
        return shardCount;
    }

}
