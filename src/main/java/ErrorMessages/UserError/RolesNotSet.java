package ErrorMessages.UserError;

import Core.MessageRemover;
import Core.SettingGetter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public class RolesNotSet {

    public static void ChannelFriendly(TextChannel txt, String notset, String command){
        EmbedBuilder em = new EmbedBuilder();
        em.setColor(Color.decode(SettingGetter.ChannelFriendlySet("GuildColour", txt)));
        em.setTitle("You haven't set up roles that can use this command");
        em.addField("You haven't set: " + notset + " Please try:",command, false);
        txt.sendMessage(em.build()).queue(MessageRemover::deleteAfter);
    }

}
