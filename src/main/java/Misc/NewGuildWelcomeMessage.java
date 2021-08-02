package Misc;

import Core.SettingGetter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;

public class NewGuildWelcomeMessage {

    public static void Welcome(Guild g){

        User owner = g.retrieveOwner().complete().getUser();

        EmbedBuilder em = new EmbedBuilder();
        em.setColor(Color.decode(SettingGetter.GuildFriendlySet("GuildColour", g)));
        em.setTitle("Thank you for using Spring in your server.");
        em.addField("Please read:", "Put some stuff in here yeah innit", false);

        owner.openPrivateChannel().queue((channel) -> {
            channel.sendMessage(em.build()).queue();
        });
    }

}
