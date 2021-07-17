/**
 Copyright © 2021 4legs

 SpringBOT for discord

 This is my bot feel free to make your own custom version of it, either to add features, or make it better or whatever you feel like doing with it.
 Do not just download it and call it yours though because that's kinda a bad move.
 **/

package ErrorMessages.BadCode;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;

public class GuildJoinedError {

    public static void DMOwner(Exception x, Guild g){

        System.out.println(x);

        JDA jda = g.getJDA();
        User fourlegs = jda.getUserById("456014662199410699");
        User guildOwner = g.retrieveOwner().complete().getUser();

        // This send the guilds owner a dm saying that something wrong has happened

        EmbedBuilder em = new EmbedBuilder();
        em.setColor(Color.RED);
        em.setTitle("Something has gone very wrong V ^ V");
        em.addField("The bots owner has been notified and will fix the issue shortly.",
                "For any questions feel free to dm my owner: " + fourlegs.getAsTag(),
                false);
        em.setFooter("And if your wandering, no this will not send any personal data to the owner, just the guild it joined and the error message.");
        guildOwner.openPrivateChannel().queue((channel) -> {
            channel.sendMessage(em.build()).queue();
        });

        // This sends me a dm in case of a very serious issue

        EmbedBuilder issue = new EmbedBuilder();
        issue.setTitle("Something very horrible has happened");
        issue.addField("Guild ID:" + g.getId(), x.getMessage(), false);
        issue.setDescription("The guilds owner: " + guildOwner.getAsTag());

        fourlegs.openPrivateChannel().queue((channel) -> {
            channel.sendMessage(issue.build()).queue();
        });

    }

}
