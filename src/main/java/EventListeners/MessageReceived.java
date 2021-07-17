/**
 Copyright © 2021 4legs

 SpringBOT for discord

 This is my bot feel free to make your own custom version of it, either to add features, or make it better or whatever you feel like doing with it.
 Do not just download it and call it yours though because that's kinda a bad move.
 **/

package EventListeners;

import Core.SettingGetter;
import Core.SettingSetter;
import Misc.SetColour;
import Misc.SetPrefix;
import commands.mod.Clear;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

public class MessageReceived extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {

        if (e.isFromGuild() && !e.getAuthor().isBot()) {

            TextChannel channel = e.getTextChannel();
            String content = e.getMessage().getContentRaw();
            User user = e.getMember().getUser();
            Guild guild = e.getGuild();
            String guildID = guild.getId();

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
                        SettingSetter.check(user, request, guild, channel, e.getMessage());
                        break;
                    case "setPrefix":
                        SetPrefix.Set(channel, content, guildID);
                        break;
                    case "setColour":
                        SetColour.Set(channel, content, guildID);
                        break;
                }
            } else if (content.equalsIgnoreCase("help I forgot my prefix")){
                channel.sendMessage(SettingGetter.ChannelFriendlySet("Prefix", channel)).queue();
            }
        }
    }
}
