package ErrorMessages.BadCode;

import Core.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;

public class GuildJoinedError {

    public static void DMOwner(Exception x, Guild guild){



        JDA jda = Main.getCurrentShard(guild);
        User fourLegs = jda.getUserById("456014662199410699");
        User guildOwner = guild.retrieveOwner().complete().getUser();

        // This sends the guild's owner a dm saying that something wrong has happened

        EmbedBuilder em = new EmbedBuilder();
        em.setColor(Color.RED);
        em.setTitle("Something has gone very wrong V ^ V");
        assert fourLegs != null;
        em.addField("The bots owner has been notified and will fix the issue shortly.",
                "For any questions feel free to dm my owner: " + fourLegs.getAsTag(),
                false);
        //guildOwner.openPrivateChannel().queue((channel) -> channel.sendMessage(em.build()).queue());

        System.out.println(x.getMessage());

    }

}
