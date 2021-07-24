package Misc;

import Core.SettingGetter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;

public class NewGuildWelcomeMessage {

    public static void Welcome(Guild g){

        User owner = g.retrieveOwner().complete().getUser();

        EmbedBuilder em = new EmbedBuilder();
        em.setColor(Color.decode(SettingGetter.GuildFriendlySet("GuildColour", g)));
        em.setTitle("Thank you for using Spring in your server.");
        em.addField("Please read:", "Hello I am Alex, the bots owner, I thank you very much for inviting my bot to your server," +
                "if in the rare occasion that the bot runs into a bug, please may you send me a screen shot of the error message and the message above the error message " +
                "(This is to help fix bugs and make the bot better), if for any reason you aren't comfortable sending me this information then just don't. Just know that this is very helpfull." +
                "\n -Alex", false);

        owner.openPrivateChannel().queue((channel) -> {
            channel.sendMessage(em.build()).queue();
        });
    }

}
