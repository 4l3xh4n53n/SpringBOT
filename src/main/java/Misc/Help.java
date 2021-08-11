package Misc;

import Core.Embed;
import Core.SettingSetter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class Help { //todo finish up in here yeah innit borger

    public static void Default(EmbedBuilder em, TextChannel txt){
        em.setTitle("Help page!");
        em.addField("Please use a sub command:", "Settings\nModeration\nGames", false);
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

    }

    public static void Games(EmbedBuilder em, TextChannel txt){

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
                default:
                    Default(em, txt);
                    break;
            }

        } else {
            Default(em, txt);
        }

    }

}
