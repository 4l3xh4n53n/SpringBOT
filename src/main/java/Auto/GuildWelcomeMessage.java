package Auto;

import Core.Embed;
import Core.SettingGetter;
import ErrorMessages.UserError.ChannelNotSet;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.awt.Color;
import java.util.Optional;

public class GuildWelcomeMessage {

    private static final String info = "This sends a welcome message every time a user joins your discord server. It can contain a message and an image.";
    private static final String set = "`setWelcomeMessage <welcome message>`\n`setWelcomeImage <imageURL>` <-- only works with links that have file extensions.";
    private static final String toggle = "`set GuildWelcomeMessage 1/0`";

    public static void Check(Guild guild, User joined){

        if (SettingGetter.GuildFriendlySet("GuildWelcome", guild).equals("1")){

            TextChannel welcome = null;
            String tag = joined.getAsTag();
            String pfp = joined.getAvatarUrl();
            User guildOwner = guild.retrieveOwner().complete().getUser();
            String guildWelcomeMessage = SettingGetter.GuildFriendlySet("GuildWelcomeMessage", guild);
            String guildWelcomeImage = SettingGetter.GuildFriendlySet("GuildWelcomeImage", guild);

            try {

                welcome = guild.getTextChannelById(SettingGetter.GuildFriendlySet("GuildWelcomeChannel", guild));

            } catch (Exception x) {
                ChannelNotSet.GuildFriendly("GuildWelcomeMessage", guildOwner, guild, toggle);
            }

            if (welcome != null){

                EmbedBuilder em  = Embed.em(joined, welcome);
                em.setColor(Color.decode(SettingGetter.GuildFriendlySet("GuildColour", guild)));
                em.setAuthor(tag, null, pfp);
                em.addField("", Optional.ofNullable(guildWelcomeMessage).orElse("Welcome Message has not been set up lol"), false);

                try {

                    em.setImage(guildWelcomeImage);

                } catch (Exception ignored){}

                welcome.sendMessageEmbeds(em.build()).queue();

            }

        }

    }

    public static String getInfo() {
        return info;
    }

    public static String getSet() {
        return set;
    }

    public static String getToggle() {
        return toggle;
    }

}
