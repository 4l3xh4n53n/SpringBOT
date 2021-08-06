package ErrorMessages.UserError;

import Core.MessageRemover;
import Core.SettingGetter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public class ChannelNotSet {

    public static void Send(TextChannel txt, String set){
        EmbedBuilder em = new EmbedBuilder();
        em.setColor(Color.decode(SettingGetter.ChannelFriendlySet("GuildColour", txt)));
        em.setTitle("Bruh momento!");
        em.addField("You don't have a channel setup for this:", set, false);
        txt.sendMessage(em.build()).queue(MessageRemover::deleteAfter);
    }

}
