package EventListeners;

import Auto.ChatSensor;
import Auto.Poll;
import Auto.ReactionRoles;
import Auto.Tickets;
import Core.Settings.*;
import Games.ActivityPoints.Commands.CoinsAmount;
import Games.ActivityPoints.Commands.Send;
import Games.ActivityPoints.Commands.Shop;
import Games.ActivityPoints.Commands.UsersStats;
import Games.ActivityPoints.Core.UserTimeOut;
import Games.Random.Dice;
import Games.Random.FlipACoin;
import Games.Server.Counting;
import Misc.CurrentSettings;
import Misc.Help;
import Misc.Ping;
import Misc.Stats;
import commands.mod.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Locale;

/**
 * Listens to messages sent in text channels and threads
 */
public class MessageReceived extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {

        ChannelType channelType = e.getChannel().getType();

        // Stops the bot from trying to run commands in thread

        if (channelType == ChannelType.GUILD_PUBLIC_THREAD || channelType ==ChannelType.GUILD_PRIVATE_THREAD){
            return; // I will try adding in support for Threads soon ...
        }

        if (e.isFromGuild() && !e.getAuthor().isBot() && e.getTextChannel().canTalk()) {

            TextChannel channel = e.getTextChannel();
            String content = e.getMessage().getContentRaw();
            Message msg = e.getMessage();
            Member member = e.getMember();
            assert member != null;
            User user = member.getUser();
            Guild guild = e.getGuild();
            String guildID = guild.getId();
            String userID = user.getId();

            String botprefix = SettingGetter.ChannelFriendlyGet("Prefix", channel);

            // Automatic processes

            UserTimeOut.check(userID, user, guild, channel);
            ChatSensor.check(content, channel, msg);
            ReactionRoles.creator(member, msg, content, channel, guild);
            Counting.check(channel,msg, content, guild);

            if (content.startsWith(botprefix)) {

                String request = content.replace(botprefix, "");
                String com = request.split("\\s+")[0].toLowerCase(Locale.ROOT);

                // Decides what the user want's to do

                switch (com) {

                    // MOD COMMANDS

                    case "clear":
                        Clear.check(channel, request, user, guild, member);
                        break;
                    case "ban":
                        Ban.check(user, msg, channel, guild, request, member);
                        break;
                    case "unban":
                        UnBan.check(user,channel,guild,request, member);
                        break;
                    case "kick":
                        Kick.check(user, msg, channel, guild, request, member);
                        break;
                    case "warn":
                        Warn.check(guild, user, channel, msg, member, guildID);
                        break;
                    case "warns":
                        WarnsAmount.check(guildID, msg, channel, guild, user, content, member);
                        break;
                    case "removewarns":
                        RemoveWarns.check(guild, user, channel, msg, member, guildID);
                        break;
                    case "mute":
                        Mute.mute(user, msg, channel, guild, request, member);
                        break;
                    case "info":
                        UserInfo.get(user, channel, msg, request, guild, member);
                        break;

                    // SETTING COMMANDS AND OTHER STUFF

                    case "set":
                        SettingSetter.check(user, request, guild, channel, msg, member);
                        break;
                    case "setprefix":
                        SetPrefix.Set(channel, content, guildID, user, member);
                        break;
                    case "setcolour":
                        SetColour.Set(channel, content, guildID, user, member);
                        break;
                    case "setwelcomemessage":
                        SetWelcomeMessage.Check(member, content, channel, guildID);
                        break;
                    case "setwelcomeimage":
                        SetWelcomeImage.Check(member, content, channel, guildID);
                        break;
                    case "setmutedrole":
                        SetMutedRole.Set(channel, content, msg, user, member, guild);
                        break;
                    case "setfilter":
                        ChatSensor.SetFilter(channel, request, guildID, member);
                        break;
                    case "addfilter":
                        ChatSensor.AppendFilter(channel, request, guildID, member);
                        break;
                    case "removefilter":
                        ChatSensor.RemoveFilter(channel, request, guildID, member);
                        break;
                    case "help":
                        Help.checkForSubCommands(content, channel, user);
                        break;
                    case "stats":
                        Stats.send(guild, channel);
                        break;
                    case "currentsettings":
                        CurrentSettings.send(guildID, channel, user, member);
                        break;

                    // GAMES AND POINTS RELATED STUFF

                    case "roll":
                        Dice.roll(channel, user);
                        break;
                    case "flip":
                        FlipACoin.flip(user, channel);
                        break;
                    case "coins":
                        CoinsAmount.get(user, guild, channel, msg, content);
                        break;
                    case "shop":
                        Shop.check(channel, content, guildID, userID, user);
                        break;
                    case "user":
                        UsersStats.Send(user, msg, channel, guildID);
                        break;
                    case "send":
                        Send.Check(user, msg, guildID, channel, content);
                        break;

                        // Automation

                    case "poll":
                        Poll.Check(channel, guild, member, content);
                        break;
                    case "makereactionmessage":
                        ReactionRoles.initializer(member, user, channel);
                        break;
                    case "ticket":
                        Tickets.create(channel, request, user, guild, member);
                        break;
                    case "closeticket":
                        Tickets.delete(channel);
                        break;

                        // Misc

                    case "ping":
                        Ping.pong(channel, e);
                        break;

                }
            } else if (content.equalsIgnoreCase("prefix")){
                channel.sendMessage(SettingGetter.ChannelFriendlyGet("Prefix", channel)).queue();
            }

        }
    }
}
