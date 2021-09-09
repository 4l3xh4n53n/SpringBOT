package ErrorMessages.UserError;

import Core.Embed;
import Core.MessageRemover;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class RoleTooHigh {

    public static void send(TextChannel txt, String module, User user){
        EmbedBuilder em = Embed.em(user, txt);
        em.setTitle("The member your trying to " + module + " has a role higher than mine");
        txt.sendMessageEmbeds(em.build()).queue(MessageRemover::deleteAfter);
    }

}
