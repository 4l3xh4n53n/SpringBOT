package Core;

import ErrorMessages.UserError.ChannelNotSet;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class ModLogger {

    private static String toggle = "`set module LogModActions 1/0`";

    public static void log(TextChannel txt, User mentioned, String channel, String reason, String set, String module, User moderator){

        if (SettingGetter.ChannelFriendlySet("LogModActions", txt).equals("1")) {

            TextChannel log = null;
            Guild guild = txt.getGuild();
            String executor = moderator.getAsTag();


            try {
                log = guild.getTextChannelById(SettingGetter.ChannelFriendlySet(channel, txt));
            } catch (Exception x) {
                ChannelNotSet.Send(txt, set, moderator, toggle);
            }

            if (log != null) {
                if (log.canTalk()) {

                    String tag = mentioned.getAsTag();
                    String pfp = mentioned.getAvatarUrl();

                    EmbedBuilder em = Embed.em(mentioned, txt);
                    em.setAuthor(tag, null, pfp);
                    em.addField(tag + " " + module, "**Reason:** " + reason + "\n**Executor:** " + executor, false);
                    log.sendMessageEmbeds(em.build()).queue();
                }

            }
        }
    }

}
