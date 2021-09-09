package Games.ActivityPoints.Core;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.*;


public class UserTimeOut {

    public static void timer(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timeout.clear();
            }
        }, 0, 60000);
    }

    public static List<String> timeout = new ArrayList<>();

    public static void check(String userID, User user, Guild guild, TextChannel channel){

        while (!timeout.contains(userID)) {
            timeout.add(userID);
            PointsHandler.add(user, guild, channel);
        }
    }


}

