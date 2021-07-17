/**
 Copyright © 2021 4legs

 SpringBOT for discord

 This is my bot feel free to make your own custom version of it, either to add features or make it better or whatever else you feel like doing with it.
 Do not just download it and call it yours though because that's kinda a bad move and this will not be tolerated.
 **/

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
