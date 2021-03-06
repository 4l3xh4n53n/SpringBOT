package Core;

import Core.Settings.SettingGetter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.awt.Color;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Embed {

    public static EmbedBuilder em(User sender, TextChannel txt){

        Calendar cal = GregorianCalendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        String time = hour + ":" + minute;
        if (String.valueOf(minute).split("").length != 2){
            time = hour + ": 0" + minute;
        }

        EmbedBuilder em = new EmbedBuilder();
        em.setColor(Color.decode(SettingGetter.ChannelFriendlyGet("GuildColour", txt)));
        em.setFooter("ID: " + sender.getId() + " | Time: " + time);

        return em;
    }

}
