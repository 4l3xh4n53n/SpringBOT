package ErrorMessages.UserError;

import Core.SettingGetter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public class NoPerms {

    public static void Send(String command, String req, TextChannel txt){
        EmbedBuilder em = new EmbedBuilder();
        em.setColor(Color.decode(SettingGetter.ChannelFriendlySet("GuildColour", txt)));
        em.setTitle("You don't have the required perms for this command");
        em.addField("Command: " + command, "Required Roles: " + req, false);
        txt.sendMessage(em.build()).queue();
    }

}
