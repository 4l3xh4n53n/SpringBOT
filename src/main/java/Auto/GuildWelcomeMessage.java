package Auto;

import Core.Embed;
import Core.SettingGetter;
import ErrorMessages.UserError.ChannelNotSet;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.util.Optional;

public class GuildWelcomeMessage {

    public static String info = "This send a welcome message every time a user joins your discord server. It can contain a message and an image.";
    public static String set = "setWelcomeMessage <welcome message>\nsetWelcomeImage <Any link ot an image, only works with links that have file extensions.";

    public static void Check(Guild guild, User joined){
        if (SettingGetter.GuildFriendlySet("GuildWelcome", guild).equals("1")){

            User guildOwner = guild.retrieveOwner().complete().getUser();
            TextChannel welcome = null;
            String tag = joined.getAsTag();
            String pfp = joined.getAvatarUrl();
            String guildWelcomeMessage = SettingGetter.GuildFriendlySet("GuildWelcomeMessage", guild);
            String guildWelcomeImage = SettingGetter.GuildFriendlySet("GuildWelcomeIMAGE", guild);

            try {
                welcome = guild.getTextChannelById(SettingGetter.GuildFriendlySet("GuildWelcomeChannel", guild));
            } catch (Exception x) {
                ChannelNotSet.GuildFriendly("GuildWelcomeMessage", guildOwner, guild);
            }

            if (welcome != null){

                EmbedBuilder em  = Embed.em(joined, welcome);
                em.setColor(Color.decode(SettingGetter.GuildFriendlySet("GuildColour", guild)));
                em.setAuthor(tag, null, pfp);
                em.addField("", Optional.ofNullable(guildWelcomeMessage).orElse("Welcome Message has not been set up lol"), false);
                try {
                    em.setImage(guildWelcomeImage);
                } catch (Exception ignored){}
                welcome.sendMessage(em.build()).queue();
            }
        }
    }

}
