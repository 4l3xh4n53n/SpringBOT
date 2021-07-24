package Core;

import ErrorMessages.UserError.ChannelNotSet;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ModLogger {

    public static void log(TextChannel txt, User mentioned, String channel, String reason, String set, String module, User moderator){

        TextChannel log = null;
        Guild guild = txt.getGuild();
        String executor = moderator.getAsTag();
        int check = 0;

        try {
            log = guild.getTextChannelById(SettingGetter.ChannelFriendlySet(channel, txt));
            log.getName(); // here to make sure that the try fails if there is no channel
            check = 1;
        } catch (Exception x){
            ChannelNotSet.Send(txt, set);
        }

        if (check == 1){

            String tag = mentioned.getAsTag();
            String pfp = mentioned.getAvatarUrl();

            Calendar cal = GregorianCalendar.getInstance();
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE);
            String time = hour + ":" + minute;

            EmbedBuilder em = new EmbedBuilder();
            em.setColor(Color.decode(SettingGetter.ChannelFriendlySet("GuildColour", txt)));
            em.setAuthor(tag, null, pfp);
            em.addField(tag + " " + module, "**Reason:** " + reason + "\n**Executor:** " + executor, false);
            em.setFooter("ID: " + mentioned.getId() + " | Time: " + time);
            log.sendMessage(em.build()).queue();

        }
    }

}
