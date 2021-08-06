package EventListeners;

import Core.SettingGetter;
import Core.SettingSetter;
import Games.ActivityPoints.Commands.CoinsAmount;
import Games.ActivityPoints.Core.UserTimeOut;
import Games.Random.Dice;
import Games.Random.FlipACoin;
import Misc.Help;
import Misc.SetColour;
import Misc.SetPrefix;
import Misc.Stats;
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

        if (e.isFromGuild() && !e.getAuthor().isBot() && e.getTextChannel().canTalk()) {

            TextChannel channel = e.getTextChannel();
            String content = e.getMessage().getContentRaw();
            Message msg = e.getMessage();
            User user = e.getMember().getUser();
            Guild guild = e.getGuild();
            String guildID = guild.getId();
            String userID = user.getId();

            String botprefix = SettingGetter.ChannelFriendlySet("Prefix", channel);

            UserTimeOut.check(userID, user, guild, channel);

            if (content.startsWith(botprefix)) {

                String request = content.replace(botprefix, "");
                String com = request.split("\\s+")[0];

                // Decides what the user want's to do

                switch (com) {

                    // MOD COMMANDS

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

                    // SETTING COMMANDS AND OTHER STUFF

                    case "set":
                        SettingSetter.check(user, request, guild, channel, msg);
                        break;
                    case "setPrefix":
                        SetPrefix.Set(channel, content, guildID);
                        break;
                    case "setColour":
                        SetColour.Set(channel, content, guildID);
                        break;
                    case "help":
                        Help.checkForSubCommands(content, guild, channel);
                        break;
                    case "stats":
                        Stats.send(guild, channel, user);
                        break;

                    // GAMES AND POINTS RELATED STUFF

                    case "roll":
                        Dice.roll(channel, user);
                        break;
                    case "flip":
                        FlipACoin.check();
                        break;
                    case "coins":
                        CoinsAmount.get(user, guild, channel, msg, content);
                        break;

                }
            } else if (content.equalsIgnoreCase("prefix")){
                channel.sendMessage(SettingGetter.ChannelFriendlySet("Prefix", channel)).queue();
            }

        }
    }
}
