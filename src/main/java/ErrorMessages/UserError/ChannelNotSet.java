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

    public static void Send(TextChannel txt, String set, User user){
        EmbedBuilder em = Embed.em(user, txt);
        em.setTitle("Bruh momento!");
        em.addField("You don't have a channel setup for this:", set, false);
        txt.sendMessage(em.build()).queue(MessageRemover::deleteAfter);
    }

    public static void GuildFriendly(String set, User user, Guild guild){
        EmbedBuilder em = new EmbedBuilder();
        em.setColor(Color.decode(SettingGetter.GuildFriendlySet("GuildColour", guild)));
        em.setTitle("Oh no!");
        em.addField("You don't have a channel setup for this:", set, false);
        user.openPrivateChannel().queue((channel) -> channel.sendMessage(em.build()).queue());
    }

}
