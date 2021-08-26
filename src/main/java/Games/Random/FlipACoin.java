package Games.Random;

import Core.Embed;
import Core.SettingGetter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class FlipACoin {

    private static final String info = "Flips a coin.";
    private static final String toggle = "`set module GameCommands 1/0`";

    public static void flip(User user, TextChannel txt) {

        if (SettingGetter.ChannelFriendlySet("GameCommands", txt).equals("1")) {
            String tag = user.getAsTag();
            String iconurl = user.getAvatarUrl();
            EmbedBuilder em = Embed.em(user, txt);
            em.setAuthor(tag, null, iconurl);
            em.setTitle("Flipped a coin and got: ");
            em.setImage("https://imgur.com/3F5J6IA.gif");
            txt.sendMessage(em.build()).queue(msg -> {

                String link = "";
                Random random = new Random();
                String hot = "";
                int rng = random.nextInt(3);

                switch (rng) {
                    case 1:
                        link = "https://imgur.com/SqFDObj.png";
                        hot = "heads";
                        break;
                    case 2:
                        link = "https://imgur.com/yA6NVZD.png";
                        hot = "tails";
                        break;

                }

                em.setImage(link);
                em.setDescription("You got: " + hot);
                msg.editMessage(em.build()).clearFiles().queueAfter(3, TimeUnit.SECONDS);
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
