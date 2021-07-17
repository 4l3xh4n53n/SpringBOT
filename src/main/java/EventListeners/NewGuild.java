/**
 Copyright © 2021 4legs

 SpringBOT for discord

 This is my bot feel free to make your own custom version of it, either to add features or make it better or whatever else you feel like doing with it.
 Do not just download it and call it yours though because that's kinda a bad move and this will not be tolerated.
 **/

package EventListeners;

import Core.SettingCreator;
import Misc.NewGuildWelcomeMessage;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class NewGuild extends ListenerAdapter {

    // For when then bot joins a new server

    @Override
    public void onGuildJoin(GuildJoinEvent e){
        Guild g = e.getGuild();
        SettingCreator.check(g);
        NewGuildWelcomeMessage.Welcome(g);
    }
}
