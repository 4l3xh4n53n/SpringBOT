package Utility;

import Core.Main;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

public class GetMentioned {

    public static User get(Message message, String expectedIDPosition, Guild guild){
        User MentionedUser = null;

        User fromAt = fromAt(message);
        User fromId = fromID(expectedIDPosition, guild);

        if (fromAt != null){
            MentionedUser = fromAt;
        } else if (fromId != null){
            MentionedUser = fromId;
        }

        return MentionedUser;
    }

    private static User fromID(String expectedIDPosition, Guild guild){
        User MentionedUser = null;

        try {
            MentionedUser = Main.getCurrentShard(guild).retrieveUserById(expectedIDPosition).complete();
        } catch (Exception ignored) {
        }

        return MentionedUser;

    }

    private static User fromAt(Message message){

        User MentionedUser = null;

        try {
            MentionedUser = message.getMentionedUsers().get(0);
        } catch (Exception ignored) {
        }

        return MentionedUser;

    }

}
