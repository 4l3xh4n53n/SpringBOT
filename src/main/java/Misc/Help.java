package Misc;

import Core.Embed;
import Core.SettingSetter;
import Games.ActivityPoints.Commands.CoinsAmount;
import Games.ActivityPoints.Commands.Send;
import Games.ActivityPoints.Commands.Shop;
import Games.ActivityPoints.Commands.UsersStats;
import commands.mod.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class Help { //todo finish up in here yeah innit borger

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
        em.addField("ban " + Ban.example, Ban.info + "\nHow to set the log: " + Ban.log + "\nHow to set the roles: " + Ban.set, false);
        em.addField("unban " + UnBan.example, UnBan.info + "\nHow to set the log: " + Ban.log + "\nHow to set the roles: " + Ban.set, false);
        em.addField("kick " + Kick.example, Kick.info + "\nHow to set the log: " + Kick.log + "\nHow to set the roles: " + Kick.set, false);
        em.addField("warn " + Warn.example, Warn.info + "\nHow to set the log: " + Warn.log + "\nHow to set the roles: " + Warn.set, false);
        em.addField("removewarns " + RemoveWarns.example, RemoveWarns.info + "\nHow to set the log: " + RemoveWarns.log + "\nHow to set the roles: " + RemoveWarns.set, false);
        em.addField("warnsamount " + WarnsAmount.example, WarnsAmount.info + "\nHow to set the roles: " + WarnsAmount.set, false);
        em.addField("clear " + Clear.example, Clear.info + "\nHow to set the roles" + Clear.set, false);
        txt.sendMessage(em.build()).queue();
    }

    public static void Games(EmbedBuilder em, TextChannel txt){

    }

    public static void Automation(EmbedBuilder em, TextChannel txt){
        //todo put in commands like the chat sensor, invite loggger, server stats. custom welcome message
    }

    public static void SpringCOIN(EmbedBuilder em, TextChannel txt){
        em.setDescription("SpringCOIN is a virtual currency, everytime you send a message you get coins (limited to 1 minute). You can use springCOIN for whatever you want. Here's your options: ");
        em.addField("How to turn it on and off: " , "`set MODULE Coins 1/0`",false);
        em.addField("coins " + CoinsAmount.example, CoinsAmount.info + "\nHow to set the roles: " + CoinsAmount.set, false);
        em.addField("shop " + Shop.example, Shop.info, false);
        em.addField("user " + UsersStats.example, UsersStats.info, false);
        em.addField("send " + Send.example, Send.info + "\nThis can be turned on and off with: `set MODULE SendCoins 1/0`", false);
        em.addField("Coming soon:", "An option to buy specific roles with your coins and possibly a few other things (:", false);
        txt.sendMessage(em.build()).queue();
    }

    public static void checkForSubCommands(String content, Guild guild, TextChannel txt, User user){

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
