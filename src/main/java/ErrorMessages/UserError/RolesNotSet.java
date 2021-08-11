package ErrorMessages.UserError;

import Core.Embed;
import Core.MessageRemover;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class RolesNotSet {

    public static void ChannelFriendly(TextChannel txt, String notset, String command, User user){
        EmbedBuilder em = Embed.em(user, txt);
        em.setTitle("You haven't set up roles that can use this command");
        em.addField("You haven't set: " + notset + " Please try:",command, false);
        txt.sendMessage(em.build()).queue(MessageRemover::deleteAfter);
    }

}
