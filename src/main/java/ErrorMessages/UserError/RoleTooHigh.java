package ErrorMessages.UserError;

import Core.MessageRemover;
import Core.SettingGetter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public class RoleTooHigh {

    public static void send(TextChannel txt, String module){
        EmbedBuilder em = new EmbedBuilder();
        em.setColor(Color.decode(SettingGetter.ChannelFriendlySet("GuildColour", txt)));
        em.setTitle("The member your trying to " + module + " has a role heigher than mine");
        txt.sendMessage(em.build()).queue(msg -> {
            MessageRemover.deleteAfter(msg);
        });
    }

}
