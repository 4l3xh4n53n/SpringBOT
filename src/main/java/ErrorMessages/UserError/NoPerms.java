package ErrorMessages.UserError;

import Core.Embed;
import Core.MessageRemover;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class NoPerms {

    public static void Send(String command, String req, TextChannel txt, User user){
        EmbedBuilder em = Embed.em(user, txt);
        em.setTitle("You don't have the required perms for this command");
        em.addField("Command: " + command, "Required Roles: " + req, false);
        txt.sendMessage(em.build()).queue();
    }

    public static void Bot(String req, TextChannel txt, User user){
        EmbedBuilder em = Embed.em(user, txt);
        em.setTitle("Sorry but I don't have the correct perms for this.");
        em.addField("Please give my roles this permission to use this command:", "Required Permissions: " + req, false);
        txt.sendMessage(em.build()).queue(MessageRemover::deleteAfter);
    }

}
