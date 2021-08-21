package EventListeners;

import Auto.InviteLogger;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.events.guild.invite.GuildInviteCreateEvent;
import net.dv8tion.jda.api.events.guild.invite.GuildInviteDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;

public class GuildInviteCreated extends ListenerAdapter {

    @Override
    public void onGuildInviteDelete(GuildInviteDeleteEvent e){
        Guild guild = e.getGuild();
        String guildID = guild.getId();
        String inviteURL = e.getUrl();
        InviteLogger.RemoveInviteFromDatabase(inviteURL, guild, guildID);
    }

    @Override
    public void onGuildInviteCreate(GuildInviteCreateEvent e){
        Invite invite = e.getInvite();
        Guild guild  = e.getGuild();
        String guildID = guild.getId();
        String inviteURL = invite.getUrl();
        String inviterID = Objects.requireNonNull(invite.getInviter()).getId();
        InviteLogger.AddInviteToDatabase(inviteURL, inviterID, guild, guildID);

    }

}
