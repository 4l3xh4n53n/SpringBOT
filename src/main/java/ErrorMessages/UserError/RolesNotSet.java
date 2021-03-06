package ErrorMessages.UserError;

import Core.Embed;
import Core.MessageRemover;
import Core.Settings.SettingGetter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.awt.Color;

public class RolesNotSet {

    public static void ChannelFriendly(TextChannel txt, String notset, String command, User user, String toggle){
        EmbedBuilder em = Embed.em(user, txt);
        em.setTitle("You haven't set up roles that can use this command");
        em.addField("You haven't set: " + notset + " Please try:",command, false);
        em.addField("If you want to turn this off use this command: ", toggle, false);
        txt.sendMessageEmbeds(em.build()).queue(MessageRemover::deleteAfter);
    }

    public static void GuildFriendly(User user, String notset, String command, Guild guild, String toggle){
        EmbedBuilder em = new EmbedBuilder();
        em.setColor(Color.decode(SettingGetter.GuildFriendlyGet("GuildColour", guild)));
        em.setTitle("You haven't set up roles that can use this command");
        em.addField("You haven't set: " + notset + " Please try:",command, false);
        em.addField("If you want to turn this off use this command: ", toggle, false);
        user.openPrivateChannel().queue(channel -> channel.sendMessageEmbeds(em.build()).queue());
    }

}
