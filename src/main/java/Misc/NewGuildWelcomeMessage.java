package Misc;

import Core.SettingGetter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class NewGuildWelcomeMessage {

    //todo Make it list the commands that you can use to turn things on and off

    public static void Welcome(Guild g){

        Calendar cal = GregorianCalendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        String time = hour + ":" + minute;

        User owner = g.retrieveOwner().complete().getUser();

        EmbedBuilder em = new EmbedBuilder();
        em.setColor(Color.GREEN);
        em.setTitle("Thank you for using Spring in your server.");
        em.addField("Please read:", "Put some stuff in here yeah innit", false);
        em.setFooter("ID: " + g.getId() + " | Time: " + time);

        owner.openPrivateChannel().queue((channel) -> {
            channel.sendMessage(em.build()).queue();
        });
    }

}
