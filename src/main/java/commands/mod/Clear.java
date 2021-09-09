package commands.mod;

import Core.MessageRemover;
import Core.SettingGetter;
import ErrorMessages.UserError.NoPerms;
import ErrorMessages.UserError.RolesNotSet;
import ErrorMessages.UserError.WrongCommandUsage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import org.apache.commons.collections4.CollectionUtils;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Clear {

    private static final String example = "clear <amount>";
    private static final String info = "This automatically removes a specified amount of messages from the specified channel.";
    private static final String set = "`set roles ClearRoles <@role(s)>`";
    private static final String toggle = "`set module ModCommands 1/0`";

    public static void Execute(TextChannel txt, int amount){

        List<Message> messagestobecleared = txt.getHistory().retrievePast(amount).complete();
        txt.deleteMessages(messagestobecleared).complete();

        EmbedBuilder em = new EmbedBuilder();
        em.setColor(Color.decode(SettingGetter.ChannelFriendlySet("GuildColour", txt)));
        em.setTitle("Removed " + (amount -1) + " messages");
        em.setFooter("");
        txt.sendMessageEmbeds(em.build()).queue(MessageRemover::deleteAfter);

    }

    public static void check(TextChannel txtchan, String request, User user, Guild guild, Member member) {

        String[] args = request.split("\\s+");

        if (SettingGetter.ChannelFriendlySet("ModCommands", txtchan).equals("1")) {
            if (args.length == 2) {
                int amount;
                Member botMember = guild.getSelfMember();
                String[] roles = SettingGetter.ChannelFriendlySet("ClearRoles", txtchan).split(",");
                StringBuilder req = new StringBuilder();
                List<Role> userroles = member.getRoles();
                List<String> usersRoles = new ArrayList<>();

                // Makes sure specified amount is a number if not defaults to 1 message

                try {
                    amount = Integer.parseInt(args[1].replaceAll("[^0-9]", ""));
                    amount = amount + 1;
                } catch (Exception x) {
                    amount = 2;
                }

                // Makes sure there are real roles setup

                int rolecheck = RoleChecker.CheckRoles(roles, guild);

                // Makes sure they have the roles

                if (rolecheck == 1) {
                    for (Role userrole : userroles) {
                        usersRoles.add(userrole.getId());
                    }
                    if (CollectionUtils.containsAny(Arrays.asList(roles), usersRoles)) {
                        if (botMember.hasPermission(Permission.MESSAGE_MANAGE) || botMember.hasPermission(Permission.ADMINISTRATOR)) {
                            if (amount > 99) {
                                amount = 99;
                            }
                            Execute(txtchan, amount);
                        } else {
                            NoPerms.Bot("Manage Messages", txtchan, user);
                        }
                    } else {
                        for (String s : roles) {
                            Role role = guild.getRoleById(s);
                            req.append("@").append(role.getName()).append(" ");
                        }
                        NoPerms.Send("clear", req.toString(), txtchan, user);
                    }

                } else {
                    RolesNotSet.ChannelFriendly(txtchan, "clear", set, user, toggle);
                }
            } else {
                WrongCommandUsage.send(txtchan, example, "Wrong amount of args", user);
            }
        }
    }

    public static String getExample() {
        return example;
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
