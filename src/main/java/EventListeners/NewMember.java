package EventListeners;

import Auto.AutoRole;
import Auto.GuildWelcomeMessage;
import Auto.InviteLogger;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * Listens for new members joining the guild ( for invite logging )
 */
public class NewMember extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent e){
        Guild guild = e.getGuild();
        User user = e.getUser();
        Member member  = e.getMember();

        GuildWelcomeMessage.Check(guild, user);
        AutoRole.give(guild, member);
        InviteLogger.Send(guild, user);
    }

}
