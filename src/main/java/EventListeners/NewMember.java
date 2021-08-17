package EventListeners;

import Auto.GuildWelcomeMessage;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class NewMember extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent e){
        Guild guild = e.getGuild();
        User user = e.getUser();

        GuildWelcomeMessage.Check(guild, user);
    }

}
