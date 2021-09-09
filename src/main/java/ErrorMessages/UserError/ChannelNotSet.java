package ErrorMessages.UserError;

import Core.Embed;
import Core.MessageRemover;
import Core.SettingGetter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;

public class ChannelNotSet {

    public static void Send(TextChannel txt, String set, User user, String toggle){
        EmbedBuilder em = Embed.em(user, txt);
        em.setTitle("Bruh momento!");
        em.addField("You don't have a channel setup for this:", set, false);
        em.addField("If you want to turn this off use this command: ", toggle, false);
        txt.sendMessageEmbeds(em.build()).queue(MessageRemover::deleteAfter);
    }

    public static void GuildFriendly(String set, User user, Guild guild, String toggle){
        EmbedBuilder em = new EmbedBuilder();
        em.setColor(Color.decode(SettingGetter.GuildFriendlySet("GuildColour", guild)));
        em.setTitle("Oh no!");
        em.addField("You don't have a channel setup for this:", set, false);
        em.addField("If you want to turn this off use this command: ", toggle, false);
        user.openPrivateChannel().queue((channel) -> channel.sendMessageEmbeds(em.build()).queue());
    }

}
