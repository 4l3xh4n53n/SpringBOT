package ErrorMessages.BadCode;

import Core.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;

public class SQLError {

    public static void TextChannel(TextChannel txt, Exception x, String toggle){
        System.out.println(x);
        Guild guild = txt.getGuild();

        EmbedBuilder em = new EmbedBuilder();
        em.setColor(Color.RED);
        em.setTitle("UH OH! Stinky");
        em.addField("The error:", x.getMessage(), false);
        em.addField("Feel free to temporarily turn off this module if the issue remains: ", toggle, false);
        em.setFooter("Feel free to show this to the bots owner: " + Main.getCurrentShard(guild).getUserById("456014662199410699").getAsTag());
        txt.sendMessage(em.build()).queue();

    }

    public static void GuildFriendly(Guild guild, Exception x, String toggle){
        User guildOwner = guild.retrieveOwner().complete().getUser();
        System.out.println(x);

        EmbedBuilder em = new EmbedBuilder();
        em.setColor(Color.RED);
        em.setTitle("Hold up!");
        em.addField("The error:", x.getMessage(), false);
        em.addField("Feel free to temporarily turn off this module if the issue remains: ", toggle, false);
        em.setFooter("Feel free to show this to the bots owner: " + Main.getCurrentShard(guild).getUserById("456014662199410699").getAsTag());

        guildOwner.openPrivateChannel().queue((channel) -> {
            channel.sendMessage(em.build()).queue();
        });

    }

}
