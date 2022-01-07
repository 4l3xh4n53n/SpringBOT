package EventListeners;

import Auto.ReactionRoles;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * Listens for Members reacting to messages ( this is for reaction roles )
 */
public class MessageReaction extends ListenerAdapter {

    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent e){
        Member member = e.getMember();
        String messageID = e.getMessageId();
        TextChannel textChannel = e.getTextChannel();
        Guild guild = e.getGuild();
        try {

            // If emote isn't nitro

            String emoji = e.getReaction().getReactionEmote().getEmoji();

            ReactionRoles.removeRole(messageID, textChannel, guild, member, emoji);

        } catch(Exception ignored){

        }

    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent e){

        Member member = e.getMember();
        String messageID = e.getMessageId();
        TextChannel textChannel = e.getTextChannel();
        Guild guild = e.getGuild();
        try {

            // If emote isn't nitro

            String emoji = e.getReaction().getReactionEmote().getEmoji();

            ReactionRoles.addRole(messageID, textChannel, guild, member, emoji);

        } catch(Exception ignored){

        }

    }

}
