package EventListeners;

import Core.Settings.SettingCreator;
import Misc.NewGuildWelcomeMessage;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class NewGuild extends ListenerAdapter {

    // For when then bot joins a new server don't confuse with members joining

    @Override
    public void onGuildJoin(GuildJoinEvent e){
        Guild g = e.getGuild();
        SettingCreator.check(g);
        NewGuildWelcomeMessage.Welcome(g);
    }
}
