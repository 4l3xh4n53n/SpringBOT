package ErrorMessages.UserError;

import Core.MessageRemover;
import Core.SettingGetter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public class NoPerms {

    public static void Send(String command, String req, TextChannel txt){
        EmbedBuilder em = new EmbedBuilder();
        em.setColor(Color.decode(SettingGetter.ChannelFriendlySet("GuildColour", txt)));
        em.setTitle("You don't have the required perms for this command");
        em.addField("Command: " + command, "Required Roles: " + req, false);
        txt.sendMessage(em.build()).queue();
    }

    public static void Bot(String req, TextChannel txt){
        EmbedBuilder em = new EmbedBuilder();
        em.setColor(Color.decode(SettingGetter.ChannelFriendlySet("GuildColour", txt)));
        em.setTitle("Sorry but I don't have the correct perms for this.");
        em.addField("Please give my roles this permission to use this command:", "Required Permissions: " + req, false);
        txt.sendMessage(em.build()).queue(MessageRemover::deleteAfter);
    }

}
