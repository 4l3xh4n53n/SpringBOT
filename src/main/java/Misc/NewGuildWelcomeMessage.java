package Misc;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class NewGuildWelcomeMessage {

    public static void Welcome(Guild g){

        Calendar cal = GregorianCalendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        String time = hour + ":" + minute;

        User owner = g.retrieveOwner().complete().getUser();

        EmbedBuilder em = new EmbedBuilder();
        em.setColor(Color.GREEN);
        em.setTitle("Thank you for using Spring in your server.");
        em.addField("Thanks for using spring bot :)", "To see the what is turned on by default please use `currentSettings` command. Default prefix is `!`", false);
        em.setFooter("ID: " + g.getId() + " | Time: " + time);

        owner.openPrivateChannel().queue((channel) -> channel.sendMessageEmbeds(em.build()).queue());
    }

}
