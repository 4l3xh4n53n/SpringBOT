package Utility;

import Core.Main;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

public class GetMentioned {

    public static User get(Message message, String expectedIDPosition, Guild guild){
        User mentionedUser = null;

        User fromAt = fromAt(message);
        User fromId = fromID(expectedIDPosition, guild);

        if (fromAt != null){
            mentionedUser = fromAt;
        } else if (fromId != null){
            mentionedUser = fromId;
        }

        return mentionedUser;
    }

    private static User fromID(String expectedIDPosition, Guild guild){
        User mentionedUser = null;

        try {
            mentionedUser = Main.getCurrentShard(guild).retrieveUserById(expectedIDPosition).complete();
        } catch (Exception ignored) {
        }

        return mentionedUser;

    }

    private static User fromAt(Message message){

        User mentionedUser = null;

        try {
            mentionedUser = message.getMentionedUsers().get(0);
        } catch (Exception ignored) {
        }

        return mentionedUser;

    }

}
