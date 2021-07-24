package ErrorMessages.BadCode;

import Core.SettingGetter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public class SQLError {

    public static void TextChannel(TextChannel txt, Exception x){
        System.out.println(x);

        EmbedBuilder em = new EmbedBuilder();
        em.setColor(Color.decode(
                SettingGetter.ChannelFriendlySet("GuildColour", txt)
        ));
        em.setTitle("UH OH! Stinky");
        em.addField("The error:", x.getMessage(), false);
        em.setFooter("Feel free to show this to the bots owner: " + txt.getJDA().getUserById("456014662199410699").getAsTag());
        txt.sendMessage(em.build()).queue();

    }

}
