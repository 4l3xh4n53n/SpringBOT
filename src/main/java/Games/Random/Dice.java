package Games.Random;

import Core.SettingGetter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.io.File;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Dice {

    public static void roll(TextChannel txt, User user){
        //todo send an embed with a gif that then stops I don't know how to edit embed contents afterwards though, oh maybe you just use edit :)

        String tag = user.getAsTag();
        String iconurl = user.getAvatarUrl();

        EmbedBuilder em = new EmbedBuilder();
        em.setImage("https://imgur.com/asrcYJZ.gif");
        em.setAuthor(tag, null, iconurl);
        em.setTitle("Rolled a dice and got: ");
        em.setColor(Color.decode(SettingGetter.ChannelFriendlySet("GuildColour", txt)));
        txt.sendMessage(em.build()).queue(msg ->{

            String link = "";
            Random random = new Random();
            int rng = random.nextInt(7);

            switch (rng){ //todo these images don't want to work innit borger
                case 1:
                    link = "https://imgur.com/Xvm0FMp.png";
                    break;
                case 2:
                    link = "https://imgur.com/9Og2Fpr.png";
                    break;
                case 3:
                    link = "https://imgur.com/wyXOCi6.png";
                    break;
                case 4:
                    link = "https://imgur.com/k5Zsqxb.png";
                    break;
                case 5:
                    link = "https://imgur.com/P5WJAXl.png";
                    break;
                case 6:
                    link = "https://imgur.com/V2goPCG.png";
                    break;
            }

            em.setImage(link);
            em.setDescription("You got: " + rng);
            msg.editMessage(em.build()).clearFiles().queueAfter(3, TimeUnit.SECONDS);
        });
    }

}
