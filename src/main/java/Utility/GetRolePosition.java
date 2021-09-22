package Utility;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

public class GetRolePosition {

    public static int get(Guild guild, User mentionedUser){
        int userRolePos = -1;

        try {
            userRolePos = guild.retrieveMemberById(mentionedUser.getId()).complete().getRoles().get(0).getPosition();
        } catch (Exception ignored) {
        }

        return userRolePos;
    }

}
