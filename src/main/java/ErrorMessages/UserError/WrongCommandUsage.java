package ErrorMessages.UserError;

import Core.Embed;
import Core.MessageRemover;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class WrongCommandUsage {

    public static void send(TextChannel txt, String exampleCommand, String reason, User user){

        EmbedBuilder em = Embed.em(user, txt);
        em.setTitle("That isn't how you use this command");
        em.addField("Example:\n" + exampleCommand,"Reason: " + reason, false);
        txt.sendMessage(em.build()).queue(MessageRemover::deleteAfter);
    }

}
