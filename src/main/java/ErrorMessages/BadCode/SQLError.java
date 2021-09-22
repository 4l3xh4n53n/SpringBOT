package ErrorMessages.BadCode;

import Core.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.awt.Color;

public class SQLError {

    public static void TextChannel(TextChannel txt, Exception x, String toggle){
        Guild guild = txt.getGuild();

        EmbedBuilder em = new EmbedBuilder();
        em.setColor(Color.RED);
        em.setTitle("UH OH! Stinky");
        em.addField("The error:", x.getMessage(), false);
        em.addField("Feel free to temporarily turn off this module if the issue remains: ", toggle, false);
        em.setFooter("Feel free to show this to the bots owner: " + Main.getCurrentShard(guild).retrieveApplicationInfo().complete().getOwner().getAsTag());
        txt.sendMessageEmbeds(em.build()).queue();

        System.out.println(x);

    }

    public static void GuildFriendly(Guild guild, Exception x, String toggle){
        TextChannel defaultChannel = guild.getDefaultChannel();

        EmbedBuilder em = new EmbedBuilder();
        em.setColor(Color.RED);
        em.setTitle("Hold up!");
        em.addField("The error:", x.getMessage(), false);
        em.addField("Feel free to temporarily turn off this module if the issue remains: ", toggle, false);
        em.setFooter("Feel free to show this to the bots owner: " + Main.getCurrentShard(guild).retrieveApplicationInfo().complete().getOwner().getAsTag());

        defaultChannel.sendMessageEmbeds(em.build()).queue();

        System.out.println(x);

    }

}
