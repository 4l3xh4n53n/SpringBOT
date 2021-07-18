/**
 Copyright © 2021 4legs

 SpringBOT for discord

 This is my bot feel free to make your own custom version of it, either to add features or make it better or whatever else you feel like doing with it.
 Do not just download it and call it yours though because that's kinda a bad move and this will not be tolerated.
 **/

package ErrorMessages.UserError;

import Core.MessageRemover;
import Core.SettingGetter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public class WrongCommandUsage {

    public static void send(TextChannel txt, String exampleCommand, String reason, String content){

        String guildColour = SettingGetter.ChannelFriendlySet("GuildColour", txt);

        EmbedBuilder em = new EmbedBuilder();
        em.setColor(Color.decode(guildColour));
        em.setTitle("That isn't how you use this command");
        em.addField("Example:\n" + exampleCommand,"Reason: " + reason, false);
        txt.sendMessage(em.build()).queue(msg -> {
            MessageRemover.deleteAfter(msg);
        });
    }

}
