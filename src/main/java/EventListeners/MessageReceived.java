package EventListeners;

import Core.SettingGetter;
import Core.SettingSetter;
import Misc.SetColour;
import Misc.SetPrefix;
import commands.mod.*;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageReceived extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {

        if (e.isFromGuild() && !e.getAuthor().isBot()) {

            TextChannel channel = e.getTextChannel();
            String content = e.getMessage().getContentRaw();
            Message msg = e.getMessage();
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
                        Ban.check(user, msg, channel, guild, request);
                        break;
                    case "unban":
                        UnBan.check(user,msg,channel,guild,request);
                        break;
                    case "kick":
                        Kick.check(user, msg, channel, guild, request);
                        break;
                    case "warn":
                        Warn.check(guild, user, channel, msg);
                        break;
                    case "warns":
                        WarnsAmount.check(guildID, msg, channel, guild, user, content);
                        break;
                    case "removewarns":
                        RemoveWarns.check(guild, user, channel, msg);
                        break;
                    case "set":
                        SettingSetter.check(user, request, guild, channel, msg);
                        break;
                    case "setPrefix":
                        SetPrefix.Set(channel, content, guildID);
                        break;
                    case "setColour":
                        SetColour.Set(channel, content, guildID);
                        break;
                }
            } else if (content.equalsIgnoreCase("prefix")){
                channel.sendMessage(SettingGetter.ChannelFriendlySet("Prefix", channel)).queue();
            }
        }
    }
}
