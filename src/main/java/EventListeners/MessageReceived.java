package EventListeners;

import Auto.Poll;
import Core.SettingGetter;
import Core.SettingSetter;
import Games.ActivityPoints.Commands.CoinsAmount;
import Games.ActivityPoints.Commands.Send;
import Games.ActivityPoints.Commands.Shop;
import Games.ActivityPoints.Commands.UsersStats;
import Games.ActivityPoints.Core.UserTimeOut;
import Games.Random.Dice;
import Games.Random.FlipACoin;
import Misc.Help;
import Misc.Set.SetColour;
import Misc.Set.SetPrefix;
import Misc.Set.SetWelcomeImage;
import Misc.Set.SetWelcomeMessage;
import Misc.Stats;
import commands.mod.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Locale;

public class MessageReceived extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {

        if (e.isFromGuild() && !e.getAuthor().isBot() && e.getTextChannel().canTalk()) {

            TextChannel channel = e.getTextChannel();
            String content = e.getMessage().getContentRaw();
            Message msg = e.getMessage();
            Member member = e.getMember();
            User user = member.getUser();
            Guild guild = e.getGuild();
            String guildID = guild.getId();
            String userID = user.getId();

            String botprefix = SettingGetter.ChannelFriendlySet("Prefix", channel);

            UserTimeOut.check(userID, user, guild, channel);

            if (content.startsWith(botprefix)) {

                String request = content.replace(botprefix, "");
                String com = request.split("\\s+")[0].toLowerCase(Locale.ROOT);

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
                        SettingSetter.check(user, request, guild, channel, msg, member);
                        break;
                    case "setprefix":
                        SetPrefix.Set(channel, content, guildID, user, guild);
                        break;
                    case "setcolour":
                        SetColour.Set(channel, content, guildID, user);
                        break;
                    case "setwelcomemessage":
                        SetWelcomeMessage.Check(member, content, channel, guildID);
                        break;
                    case "setwelcomeimage":
                        SetWelcomeImage.Check(member, content, channel, guildID);
                        break;
                    case "help":
                        Help.checkForSubCommands(content, guild, channel, user);
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


                }
            } else if (content.equalsIgnoreCase("prefix")){
                channel.sendMessage(SettingGetter.ChannelFriendlySet("Prefix", channel)).queue();
            }

        }
    }
}
