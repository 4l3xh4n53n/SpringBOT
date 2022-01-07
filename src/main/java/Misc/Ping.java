package Misc;

import Core.Main;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.time.temporal.ChronoUnit;

public class Ping {

    private static final String info = "Shows the bots ping.";

    public static void pong(TextChannel textChannel, MessageReceivedEvent e){

        JDA currentShard = Main.getShard();

        long gateWayPing = currentShard.getGatewayPing();
        long restPing = currentShard.getRestPing().complete();

        textChannel.sendMessage("Ping...").queue(message -> {

            long ping = e.getMessage().getTimeCreated().until(message.getTimeCreated(), ChronoUnit.MILLIS);
            message.editMessage("PONG! :ping_pong: " + ping  + "ms \n **Websocket:** " + gateWayPing + "ms \n **Rest:** " + restPing + "ms").queue();
        });

    }

    public static String getInfo(){
        return info;
    }

}
