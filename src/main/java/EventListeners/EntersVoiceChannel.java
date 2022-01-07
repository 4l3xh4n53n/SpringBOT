package EventListeners;

import Auto.PrivateChannelCreator;
import Core.Settings.SettingGetter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * Listens for Members joining and leaving voice channels
 */
public class EntersVoiceChannel extends ListenerAdapter {

    public static Guild guild;
    public static VoiceChannel channel;
    public static String guildID;

    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent e){
        guild = e.getGuild();
        channel = (VoiceChannel) e.getChannelLeft();
        guildID = guild.getId();

        if (SettingGetter.GuildFriendlyGet("PrivateChannel", guild).equals("1")) {
            PrivateChannelCreator.CheckDB(guildID, guild);
            PrivateChannelCreator.Decrease(channel, guild);
            PrivateChannelCreator.Delete(channel, guild, guildID);
        }
    }

    @Override
    public void onGuildVoiceJoin(GuildVoiceJoinEvent e){

        Guild guild = e.getGuild();
        Member member = e.getMember();
        User user = member.getUser();
        VoiceChannel channel = (VoiceChannel) e.getChannelJoined();
        String guildID = guild.getId();

        if (SettingGetter.GuildFriendlyGet("PrivateChannel", guild).equals("1")) {
            PrivateChannelCreator.CheckDB(guildID, guild);
            PrivateChannelCreator.Increase(channel, guild);
            PrivateChannelCreator.Create(guild, user, channel, member, guildID);
        }

    }

}
