package Misc;

import Auto.*;
import Core.Embed;
import Core.SettingSetter;
import Games.ActivityPoints.Commands.CoinsAmount;
import Games.ActivityPoints.Commands.Send;
import Games.ActivityPoints.Commands.Shop;
import Games.ActivityPoints.Commands.UsersStats;
import Games.Random.Dice;
import Games.Random.FlipACoin;
import commands.mod.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class Help {

    public static void Default(EmbedBuilder em, TextChannel txt){
        em.setTitle("Help page!");
        em.addField("Please use a sub command:", "Settings\nModeration\nGames\nAutomation\nSpringCOIN", false);
        txt.sendMessage(em.build()).queue();
    }

    public static void Settings(EmbedBuilder em, TextChannel txt){
        em.setTitle("Settings");
        em.setDescription("Settings commands allow you to setup log channels, turn modules on and off, and give certain roles the ability to use certain features.");
        em.addField("set", SettingSetter.example, false);
        em.addField("Modules: (things that can be turned on and off)", SettingSetter.modules, false);
        em.addField("Roles: (The roles that you want to have access to a specific module)", SettingSetter.roles, false);
        em.addField("Channels: (The channels where certain things will happen", SettingSetter.channels, false);
        txt.sendMessage(em.build()).queue();
    }

    public static void Moderation(EmbedBuilder em, TextChannel txt){
        em.setTitle("Moderation");
        em.setDescription("These commands let you do things by typing rather than the gui, even though gui is probably faster for most (whatever floats your boat I guess)");
        em.addField("How to turn them on and off: ", "`set MODULE ModCommands 1/0`", false);
        em.addField("You can also log mod actions with this command: ", "`set MODULE LogModActions 1/0`", false);
        em.addField("ban " + Ban.getExample(), Ban.getInfo() + "\nHow to set the log: " + Ban.getLog() + "\nHow to set the roles: " + Ban.getSet(), false);
        em.addField("unban " + UnBan.getExample(), UnBan.getInfo() + "\nHow to set the log: " + UnBan.getLog() + "\nHow to set the roles: " + Ban.getSet(), false);
        em.addField("kick " + Kick.getExample(), Kick.getInfo() + "\nHow to set the log: " + Kick.getLog() + "\nHow to set the roles: " + Kick.getSet(), false);
        em.addField("warn " + Warn.getExample(), Warn.getInfo() + "\nHow to set the log: " + Warn.getLog() + "\nHow to set the roles: " + Warn.getSet(), false);
        em.addField("removewarns " + RemoveWarns.getExample(), RemoveWarns.getInfo() + "\nHow to set the log: " + RemoveWarns.getLog() + "\nHow to set the roles: " + RemoveWarns.getSet(), false);
        em.addField("warnsamount " + WarnsAmount.getExample(), WarnsAmount.getInfo() + "\nHow to set the roles: " + WarnsAmount.getSet(), false);
        em.addField("clear " + Clear.getExample(), Clear.getInfo() + "\nHow to set the roles" + Clear.getSet(), false);
        txt.sendMessage(em.build()).queue();
    }

    public static void Games(EmbedBuilder em, TextChannel txt){
        em.setTitle("Games");
        em.setDescription("Stuff in here for fun.");
        em.addField("flip", FlipACoin.getInfo(), false);
        em.addField("roll", Dice.getInfo(), false);
        txt.sendMessage(em.build()).queue();
    }

    public static void Automation(EmbedBuilder em, TextChannel txt){
        em.setTitle("Automation");
        em.setDescription("Automates your server.");
        em.addField("Auto Role", AutoRole.getInfo() + " " +  AutoRole.getSet() + " " + AutoRole.getInfo(), false);
        em.addField("Chat Sensor", ChatSensor.getInfo() + " " +  ChatSensor.getSet() + " " + ChatSensor.getInfo(), false);
        em.addField("Welcome Message", GuildWelcomeMessage.getInfo() + " " +  GuildWelcomeMessage.getSet() + " " + GuildWelcomeMessage.getInfo(), false);
        em.addField("Invite Logger", InviteLogger.getInfo() + " " +  InviteLogger.getSet() + " " + InviteLogger.getInfo(), false);
        em.addField("Poll", Poll.getInfo() + " " +  Poll.getSet() + " " + Poll.getInfo(), false);
        em.addField("Private Channel Creator", PrivateChannelCreator.getInfo() + " " +  PrivateChannelCreator.getSet() + " " + PrivateChannelCreator.getInfo(), false);
        txt.sendMessage(em.build()).queue();
    }

    public static void SpringCOIN(EmbedBuilder em, TextChannel txt){
        em.setDescription("SpringCOIN is a virtual currency, everytime you send a message you get coins (limited to 1 minute). You can use springCOIN for whatever you want. Here's your options: ");
        em.addField("How to turn it on and off: " , "`set MODULE Coins 1/0`",false);
        em.addField("coins " + CoinsAmount.getExample(), CoinsAmount.getInfo() + "\nHow to set the roles: " + CoinsAmount.getSet(), false);
        em.addField("shop " + Shop.getExample(), Shop.getInfo(), false);
        em.addField("user " + UsersStats.getExample(), UsersStats.getInfo(), false);
        em.addField("send " + Send.getExample(), Send.getInfo() + "\nThis can be turned on and off with: `set MODULE SendCoins 1/0`", false);
        em.addField("Coming soon:", "An option to buy specific roles with your coins and possibly a few other things (:", false);
        txt.sendMessage(em.build()).queue();
    }

    public static void checkForSubCommands(String content, TextChannel txt, User user){

        String[] args = content.split("\\s+");
        EmbedBuilder em = Embed.em(user, txt);
        em.setThumbnail("https://cdn.discordapp.com/avatars/673654725127831562/526af14d924487c4af7aabae5fa2850a.png?size=1024");

        if (args.length > 1){
            String sub = args[1].toLowerCase();
            switch (sub){
                case "settings":
                    Settings(em, txt);
                    break;
                case "moderation":
                    Moderation(em, txt);
                    break;
                case "games":
                    Games(em, txt);
                    break;
                case "automation":
                    Automation(em, txt);
                    break;
                case "springcoin":
                    SpringCOIN(em, txt);
                    break;
                default:
                    Default(em, txt);
                    break;
            }

        } else {
            Default(em, txt);
        }

    }

}
