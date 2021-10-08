package Games.ActivityPoints.Core;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class UserTimeOut {

    public static void timer(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timeOut.clear();
            }
        }, 0, 60000);
    }

    private static final HashMap<String, String> timeOut = new HashMap<>();

    public static void check(String userID, User user, Guild guild, TextChannel channel){

        String guildID = guild.getId();

        if (!timeOut.containsKey(guildID)){
            timeOut.put(guildID, "");
        }

        if (!Arrays.asList(timeOut.get(guildID).split(",")).contains(userID)){

            String content = timeOut.get(guildID);
            content = content + "," + userID;
            timeOut.put(guildID, content);

            PointsHandler.add(user, guild, channel);
        }

    }

}

