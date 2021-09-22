package commands.mod;

import Core.MessageRemover;
import Core.SettingGetter;
import ErrorMessages.UserError.NoPerms;
import ErrorMessages.UserError.RolesNotSet;
import ErrorMessages.UserError.WrongCommandUsage;
import Utility.GetRoleIDs;
import Utility.RoleChecker;
import Utility.RoleFormatter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import org.apache.commons.collections4.CollectionUtils;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

public class Clear {

    private static final String example = "clear <amount>";
    private static final String info = "This automatically removes a specified amount of messages from the specified channel.";
    private static final String set = "`set roles ClearRoles <@role(s)>`";
    private static final String toggle = "`set module ModCommands 1/0`";

    private static void Execute(TextChannel textChannel, int amount){

        List<Message> messagesToBeRemoved = textChannel.getHistory().retrievePast(amount).complete();
        textChannel.deleteMessages(messagesToBeRemoved).complete();

        EmbedBuilder em = new EmbedBuilder();
        em.setColor(Color.decode(SettingGetter.ChannelFriendlySet("GuildColour", textChannel)));
        em.setTitle("Removed " + (amount -1) + " messages");
        em.setFooter("");
        textChannel.sendMessageEmbeds(em.build()).queue(MessageRemover::deleteAfter);

    }

    public static void check(TextChannel textChannel, String request, User user, Guild guild, Member member) {

        String[] args = request.split("\\s+");

        Member botMember = guild.getSelfMember();
        String[] requiredRoles = SettingGetter.ChannelFriendlySet("ClearRoles", textChannel).split(",");

        List<Role> userRoles = member.getRoles();
        List<String> usersRoles = GetRoleIDs.get(userRoles);

        int amount;

        if (SettingGetter.ChannelFriendlySet("ModCommands", textChannel).equals("1")) {
            if (args.length == 2) {

                // Makes sure specified amount is a number if not defaults to 1 message

                try {
                    amount = Integer.parseInt(args[1].replaceAll("[^0-9]", ""));
                    amount = amount + 1;
                } catch (Exception x) {
                    amount = 2;
                }

                if (RoleChecker.areRolesValid(requiredRoles, guild) == 1) {
                    if (CollectionUtils.containsAny(Arrays.asList(requiredRoles), usersRoles)) {
                        if (botMember.hasPermission(Permission.MESSAGE_MANAGE) || botMember.hasPermission(Permission.ADMINISTRATOR)) {

                            if (amount > 99) {
                                amount = 99;
                            }

                            Execute(textChannel, amount);

                        } else {
                            NoPerms.Bot("Manage Messages", textChannel, user);
                        }
                    } else {
                        NoPerms.Send("clear", RoleFormatter.format(requiredRoles, guild), textChannel, user);
                    }
                } else {
                    RolesNotSet.ChannelFriendly(textChannel, "clear", set, user, toggle);
                }
            } else {
                WrongCommandUsage.send(textChannel, example, "Wrong amount of args", user);
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
