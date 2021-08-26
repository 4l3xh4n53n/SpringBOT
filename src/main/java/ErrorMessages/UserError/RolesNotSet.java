package ErrorMessages.UserError;

import Core.Embed;
import Core.MessageRemover;
import Core.SettingGetter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;

public class RolesNotSet {

    public static void ChannelFriendly(TextChannel txt, String notset, String command, User user, String toggle){
        EmbedBuilder em = Embed.em(user, txt);
        em.setTitle("You haven't set up roles that can use this command");
        em.addField("You haven't set: " + notset + " Please try:",command, false);
        em.addField("If you want to turn this off use this command: ", toggle, false);
        txt.sendMessage(em.build()).queue(MessageRemover::deleteAfter);
    }

    public static void GuildFriendly(User guildOwner, String notset, String command, Guild guild, String toggle){
        EmbedBuilder em = new EmbedBuilder();
        em.setColor(Color.decode(SettingGetter.GuildFriendlySet("GuildColour", guild)));
        em.setTitle("You haven't set up roles that can use this command");
        em.addField("You haven't set: " + notset + " Please try:",command, false);
        em.addField("If you want to turn this off use this command: ", toggle, false);
        guildOwner.openPrivateChannel().queue(channel -> channel.sendMessage(em.build()).queue());
    }

}
