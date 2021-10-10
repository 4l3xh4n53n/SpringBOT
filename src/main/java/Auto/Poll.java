package Auto;

import Core.Embed;
import Core.MessageRemover;
import Core.Settings.SettingGetter;
import ErrorMessages.UserError.NoPerms;
import ErrorMessages.UserError.RolesNotSet;
import ErrorMessages.UserError.WrongCommandUsage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;

public class Poll {

    private static final String info = "This command can make you polls that look very nice.";
    private static final String set = "`set PollRole <@role(s)>`";
    private static final String example = "poll <Topic> OPTION <option1> <emote1> OPTION <option2> <emote2> etc";
    private static final String toggle = "`set Poll 1/0`";

    public static void Check(TextChannel txt, Guild guild, Member member, String content){

        String[] roleID = null;
        String[] args = content.split("OPTION");
        String topic = args[0].toLowerCase().replace(SettingGetter.ChannelFriendlyGet("Prefix", txt) + "poll", "");
        String[] options = content.replace(topic + "OPTION", "").split("OPTION");
        StringBuilder optionsF = new StringBuilder();
        List<Role> roles = new java.util.ArrayList<>(Collections.emptyList());
        List<Role> usersRoles = member.getRoles();
        StringBuilder req = new StringBuilder();
        User user = member.getUser();

        int check = 0;

        try {
            roleID = SettingGetter.ChannelFriendlyGet("PollRole", txt).split("\\s+");
            check = 1;
        } catch (Exception ignored) {
            RolesNotSet.ChannelFriendly(txt, "PollRole", set, user, toggle);
        }

        if (check == 1) {
            for (String s : roleID) {

                Role role = null;
                try {
                    role = guild.getRoleById(s.replace(",", ""));
                } catch (Exception ignored) {
                }
                if (role != null) {
                    roles.add(role);
                }
            }
        }

        if (roles.size() > 0) {
            if (CollectionUtils.containsAny(roles, usersRoles)) {

                if (args.length > 2) {

                    for (String option : options) {
                        optionsF.append("\n").append(option);
                    }

                    if (guild.getSelfMember().getPermissions().contains(Permission.MESSAGE_ADD_REACTION)) {

                        EmbedBuilder em = Embed.em(user, txt);
                        em.addField(topic, optionsF.toString(), false);
                        txt.sendMessageEmbeds(em.build()).queue(msg -> {
                            for (String option : options) {

                                String[] optionArg = option.split("\\s+");
                                String emoji = optionArg[optionArg.length - 1];

                                msg.addReaction(emoji).queue(null, error -> {
                                    MessageRemover.deleteAfter(msg);
                                    WrongCommandUsage.send(txt, example, "**" + emoji + "** is not a valid emoji", user);
                                });

                            }
                        });

                    } else {
                        NoPerms.Bot("AddReaction", txt, user);
                    }

                } else {
                    WrongCommandUsage.send(txt, example, "You didn't give enough options", user);
                }
            } else {
                for (Role role : roles) {
                    req.append("@").append(role.getName()).append(" ");
                }
                NoPerms.Send("poll", req.toString(), txt, user);
            }
        } else {
            RolesNotSet.ChannelFriendly(txt, "PollRoll", set, user, toggle);
        }

    }

    public static String getInfo() {
        return info;
    }

    public static String getSet() {
        return set;
    }

    public static String getExample() {
        return example;
    }

    public static String getToggle() {
        return toggle;
    }
}
