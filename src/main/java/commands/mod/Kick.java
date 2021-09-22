package commands.mod;

import Core.Embed;
import Core.MessageRemover;
import Core.ModLogger;
import Core.SettingGetter;
import ErrorMessages.UserError.NoPerms;
import ErrorMessages.UserError.RoleTooHigh;
import ErrorMessages.UserError.RolesNotSet;
import ErrorMessages.UserError.WrongCommandUsage;
import Utility.GetMentioned;
import Utility.GetRoleIDs;
import Utility.GetRolePosition;
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

import java.util.Arrays;
import java.util.List;

public class Kick {

    private static final String example = "`kick <@user/userID> <REASON> <--` (optional)";
    private static final String info = "This command kicks the specified user.";
    private static final String set = "`set roles KickRoles <@role(S)>`";
    private static final String log = "`set channel KickLog <channelID>`";
    private static final String toggle = "`set module ModCommands 1/0`";


    private static void Execute(Guild guild, User mentioned, String[] args, String request, TextChannel textChannel, User user){
        String reason = request.replace(args[0] + " " + args[1], "");
        guild.kick(mentioned.getId(), reason).queue();

        EmbedBuilder em = Embed.em(user, textChannel);
        em.setTitle("Kicked " + mentioned.getAsTag());
        textChannel.sendMessageEmbeds(em.build()).queue(MessageRemover::deleteAfter);

        ModLogger.log(textChannel, mentioned, "KickLog", reason, log, "was kicked", user);

    }

    public static void check(User user, Message msg, TextChannel textChannel, Guild guild, String request, Member member) {

        String[] args = request.split("\\s+");
        User mentionedUser;

        Role botRole = guild.getSelfMember().getRoles().get(0);
        Member botMember = guild.getSelfMember();

        String[] requiredRoles = SettingGetter.ChannelFriendlySet("KickRoles", textChannel).split(",");
        List<Role> userRoles = member.getRoles();
        List<String> usersRolesIDs = GetRoleIDs.get(userRoles);


        int botRolePos = botRole.getPosition();
        int selfUserRolePos = guild.getSelfMember().getRoles().get(0).getPosition();

        if (SettingGetter.ChannelFriendlySet("ModCommands", textChannel).equals("1")) {
            if (args.length > 1) {
                if (RoleChecker.areRolesValid(requiredRoles, guild) == 1) {
                    if ((mentionedUser = GetMentioned.get(msg, args[1], guild)) != null) {
                        if (CollectionUtils.containsAny(Arrays.asList(requiredRoles), usersRolesIDs)) {
                            if (botMember.hasPermission(Permission.KICK_MEMBERS) || botMember.hasPermission(Permission.ADMINISTRATOR)) {

                                int userRolePos = GetRolePosition.get(guild, mentionedUser);

                                if (botRolePos > userRolePos || selfUserRolePos > userRolePos) {

                                    Execute(guild, mentionedUser, args, request, textChannel, user);

                                } else {
                                    RoleTooHigh.send(textChannel, "kick", user);
                                }
                            } else {
                                NoPerms.Bot("Kick Members", textChannel, user);
                            }
                        } else {
                            NoPerms.Send("ban", RoleFormatter.format(requiredRoles, guild), textChannel, user);
                        }
                    } else {
                        WrongCommandUsage.send(textChannel, example, "You haven't mentioned any members", user);
                    }
                } else {
                    RolesNotSet.ChannelFriendly(textChannel, "kick", set, user, toggle);
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

    public static String getLog() {
        return log;
    }

    public static String getToggle() {
        return toggle;
    }
}
