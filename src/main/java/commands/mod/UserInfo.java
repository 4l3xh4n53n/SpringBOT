package commands.mod;

import Core.Embed;
import ErrorMessages.UserError.NoPerms;
import Utility.GetMentioned;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class UserInfo {

    private static final String example = "information <user>";
    private static final String info = "Shows information about a user.";
    private static final String toggle = "`set ModCommands 1/0`";

    public static void get(User user, TextChannel textChannel, Message message, String request, Guild guild, Member member){

        if (member.getPermissions().contains(Permission.ADMINISTRATOR)) {

            String[] args = request.split("\\s+");

            User mentioned;

            if (args.length > 1) {
                mentioned = GetMentioned.get(message, args[1], guild);
            } else {
                mentioned = GetMentioned.get(message, user.getId(), guild);
            }

            if (mentioned != null) {

                OffsetDateTime created = mentioned.getTimeCreated();
                int minute = created.toLocalTime().getMinute();
                int hour = created.toLocalTime().getHour();

                LocalDate d1 = LocalDate.parse(created.toLocalDate().toString(), DateTimeFormatter.ISO_LOCAL_DATE);
                LocalDate d2 = LocalDate.parse(LocalDate.now().toString(), DateTimeFormatter.ISO_LOCAL_DATE);
                Duration diff = Duration.between(d1.atStartOfDay(), d2.atStartOfDay());
                long diffDays = diff.toDays();

                String name = mentioned.getAsTag();
                String id = mentioned.getId();
                String avatar = mentioned.getAvatarUrl();
                String dateCreated = created.toLocalDate().toString();

                String timeCreated = hour + ":" + minute;

                EmbedBuilder em = Embed.em(user, textChannel);

                em.setAuthor(name, null, avatar);
                em.addField("ID:", id, false);
                em.addField("Created:", "At: " + timeCreated + " on the: " + dateCreated, false);
                em.addField("Account Age :", diffDays + "days", false);

                textChannel.sendMessageEmbeds(em.build()).queue();

            }
        } else {
            NoPerms.Send("information", "admin", textChannel, user);
        }

    }
    public static String getExample() {
        return example;
    }

    public static String getInfo() {
        return info;
    }

    public static String getToggle() {
        return toggle;
    }
}
