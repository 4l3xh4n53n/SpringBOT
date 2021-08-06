package ErrorMessages.UserError;

import Core.MessageRemover;
import Core.SettingGetter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public class WrongCommandUsage {

    public static void send(TextChannel txt, String exampleCommand, String reason){

        String guildColour = SettingGetter.ChannelFriendlySet("GuildColour", txt);

        EmbedBuilder em = new EmbedBuilder();
        em.setColor(Color.decode(guildColour));
        em.setTitle("That isn't how you use this command");
        em.addField("Example:\n" + exampleCommand,"Reason: " + reason, false);
        txt.sendMessage(em.build()).queue(MessageRemover::deleteAfter);
    }

}
