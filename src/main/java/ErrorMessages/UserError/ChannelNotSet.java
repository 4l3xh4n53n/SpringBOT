package ErrorMessages.UserError;

import Core.Embed;
import Core.MessageRemover;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class ChannelNotSet {

    public static void Send(TextChannel txt, String set, User user){
        EmbedBuilder em = Embed.em(user, txt);
        em.setTitle("Bruh momento!");
        em.addField("You don't have a channel setup for this:", set, false);
        txt.sendMessage(em.build()).queue(MessageRemover::deleteAfter);
    }

}
