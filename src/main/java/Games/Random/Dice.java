package Games.Random;

import Core.Embed;
import Core.Settings.SettingGetter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Dice {

    private static final String info = "Rolls a dice.";
    private static final String toggle = "`set GameCommands 1/0`";

    public static void roll(TextChannel txt, User user) {

        if (SettingGetter.ChannelFriendlyGet("GameCommands", txt).equals("1")) {

            String tag = user.getAsTag();
            String iconurl = user.getAvatarUrl();

            EmbedBuilder em = Embed.em(user, txt);
            em.setImage("https://imgur.com/asrcYJZ.gif");
            em.setAuthor(tag, null, iconurl);
            em.setTitle("Rolled a dice and got: ");

            txt.sendMessageEmbeds(em.build()).queue(msg -> {

                String link = "";
                Random random = new Random();
                int rng = random.nextInt(7);

                switch (rng) {
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
                msg.editMessageEmbeds(em.build()).clearFiles().queueAfter(3, TimeUnit.SECONDS);
            });

        }

    }

    public static String getInfo() {
        return info;
    }

    public static String getToggle() {
        return toggle;
    }
}
