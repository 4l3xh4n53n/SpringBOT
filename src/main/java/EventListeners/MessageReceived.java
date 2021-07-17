/**
 Copyright © 2021 4legs

 SpringBOT for discord

 This is my bot feel free to make your own custom version of it, either to add features, or make it better or whatever you feel like doing with it.
 Do not just download it and call it yours though because that's kinda a bad move.
 **/

package EventListeners;

import Core.SettingGetter;
import Core.SettingSetter;
import commands.mod.Clear;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Arrays;

public class MessageReceived extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {

        if (e.isFromGuild() && !e.getAuthor().isBot()) {

            TextChannel channel = e.getTextChannel();
            String content = e.getMessage().getContentRaw();
            String[] commands = {"clear", "ban"};
            User user = e.getMember().getUser();
            Guild guild = e.getGuild();

            String botprefix = SettingGetter.ChannelFriendlySet("Prefix", channel);

            if (content.startsWith(botprefix)) {

                String request = content.replace(botprefix, "");
                String com = request.split("\\s+")[0];

                // Decides what the user want's to do

                switch (com) {
                    case "clear":
                        Clear.check(channel, request, user, guild);
                        break;
                    case "ban":
                        break;
                    case "kick":
                        break;
                    case "warn":
                        break;
                    case "set":
                        SettingSetter.check(request, guild, channel);

                }
            }
        }
    }
}
