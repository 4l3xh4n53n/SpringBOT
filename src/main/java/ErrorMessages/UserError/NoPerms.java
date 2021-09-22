package ErrorMessages.UserError;

import Core.Embed;
import Core.MessageRemover;
import Core.SettingGetter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.awt.Color;

public class NoPerms {

    public static void Send(String command, String req, TextChannel txt, User user){
        EmbedBuilder em = Embed.em(user, txt);
        em.setTitle("You don't have the required perms for this command");
        em.addField("Command: " + command, "Required Roles: " + req, false);
        txt.sendMessageEmbeds(em.build()).queue();
    }

    public static void Bot(String req, TextChannel txt, User user){
        EmbedBuilder em = Embed.em(user, txt);
        em.setTitle("Sorry but I don't have the correct perms for this.");
        em.addField("Please give my roles this permission to use this command:", "Required Permissions: " + req, false);
        txt.sendMessageEmbeds(em.build()).queue(MessageRemover::deleteAfter);
    }

    public static void GuildBot(String req, User user, Guild guild){
        user.openPrivateChannel().queue(txt ->{
            EmbedBuilder em = new EmbedBuilder();
            em.setColor(Color.decode(SettingGetter.GuildFriendlySet("GuildColour", guild)));
            em.setTitle("Sorry but I don't have the correct perms for this.");
            em.addField("Please give my roles this permission to use this command:", "Required Permissions: " + req, false);
            txt.sendMessageEmbeds(em.build()).queue(MessageRemover::deleteAfter);
        });
    }

}
