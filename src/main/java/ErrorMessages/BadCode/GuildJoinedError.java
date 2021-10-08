package ErrorMessages.BadCode;

import Core.Main;
import Utility.RSJG;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.awt.Color;

public class GuildJoinedError {

    public static void SendInMain(Exception x, Guild guild){

        JDA jda = Main.getCurrentShard(guild);
        User fourLegs = jda.getUserById("456014662199410699");
        TextChannel defaultChannel = guild.getDefaultChannel();

        EmbedBuilder em = new EmbedBuilder();
        em.setColor(Color.RED);
        em.setTitle("Something has gone very wrong V ^ V");
        em.addField("The bots owner has been notified and will fix the issue shortly.",
                "For any questions feel free to dm my owner: " + fourLegs.getAsTag(),
                false);
        em.setFooter(RSJG.get());
        defaultChannel.sendMessageEmbeds(em.build()).queue();

        System.out.println(x.getMessage());

    }

}
