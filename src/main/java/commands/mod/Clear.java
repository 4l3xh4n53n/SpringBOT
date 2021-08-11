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

    public static String example = "clear <amount>";
    public static String info = "This automatically removes a specified amount of messages from the specified channel.";
    public static String set = "`set roles ClearRoles <@role(s)>`";

    public static void Execute(TextChannel txt, int amount){

        List<Message> messagestobecleared = txt.getHistory().retrievePast(amount).complete();
        txt.deleteMessages(messagestobecleared).complete();

        EmbedBuilder em = new EmbedBuilder();
        em.setColor(Color.decode(SettingGetter.ChannelFriendlySet("GuildColour", txt)));
        em.setTitle("Removed " + (amount -1) + " messages");
        em.setFooter("");
        txt.sendMessage(em.build()).queue(MessageRemover::deleteAfter);

    }

    public static void check(TextChannel txtchan, String request, User user, Guild guild) {

        String[] args = request.split("\\s+");

        if (SettingGetter.ChannelFriendlySet("ModCommands", txtchan).equals("1")) {
            if (args.length == 2) {
                int amount;
                Role botrole = guild.getBotRole();
                String[] roles = SettingGetter.ChannelFriendlySet("ClearRoles", txtchan).split(",");
                String req = "";
                List<Role> userroles = guild.getMember(user).getRoles();
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
                    for (int i = 0; userroles.size() > i; i++) {
                        usersRoles.add(userroles.get(i).getId());
                    }
                    if (CollectionUtils.containsAny(Arrays.asList(roles), usersRoles)) {
                        if (botrole.hasPermission(Permission.MESSAGE_MANAGE) || botrole.hasPermission(Permission.ADMINISTRATOR)) {
                            if (amount > 99) {
                                amount = 99;
                            }
                            Execute(txtchan, amount);
                        } else {
                            NoPerms.Bot("Manage Messages", txtchan, user);
                        }
                    } else {
                        for (int i = 0; roles.length > i; i++) {
                            Role role = guild.getRoleById(roles[i]);
                            req = req + "@" + role.getName() + " ";
                        }
                        NoPerms.Send("clear", req, txtchan, user);
                    }

                } else {
                    RolesNotSet.ChannelFriendly(txtchan, "clear", set, user);
                }
            } else {
                WrongCommandUsage.send(txtchan, example, "Wrong amount of args", user);
            }
        }
    }
}
