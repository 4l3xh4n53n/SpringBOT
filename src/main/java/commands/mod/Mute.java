package commands.mod;

import Core.Settings.SettingGetter;
import ErrorMessages.UserError.NoPerms;
import ErrorMessages.UserError.RoleTooHigh;
import ErrorMessages.UserError.RolesNotSet;
import ErrorMessages.UserError.WrongCommandUsage;
import Utility.GetMentioned;
import Utility.GetRoleIDs;
import Utility.RoleChecker;
import Utility.RoleFormatter;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Arrays;
import java.util.List;

public class Mute {

    private static final String example = "`mute <@user/userID>`";
    private static final String info = "This command mutes or un-mutes the specified user.";
    private static final String set = "`set MuteRoles <@role(S)>`";
    private static final String setRoleConfig = "setMutedRole <@role>";
    private static final String toggle = "`set ModCommands 1/0`";

    public static void mute(User user, Message msg, TextChannel textChannel, Guild guild, String request, Member member){

        String mutedRole = SettingGetter.ChannelFriendlyGet("MutedRole", textChannel);

        String[] args = request.split("\\s+");
        User mentionedUser;

        Role botRole = guild.getSelfMember().getRoles().get(0);
        Member botMember = guild.getSelfMember();

        String[] requiredRoles = SettingGetter.ChannelFriendlyGet("MuteRoles", textChannel).split(",");
        List<Role> userRoles = member.getRoles();
        List<String> usersRolesIDs = GetRoleIDs.get(userRoles);

        int botRolePos = botRole.getPosition();
        int selfUserRolePos = guild.getSelfMember().getRoles().get(0).getPosition();

        if (SettingGetter.ChannelFriendlyGet("ModCommands", textChannel).equals("1")){
            if (args.length > 1) {
                if (RoleChecker.areRolesValid(requiredRoles, guild) == 1) {
                    if ((mentionedUser = GetMentioned.get(msg, args[1], guild)) != null) {
                        if (CollectionUtils.containsAny(Arrays.asList(requiredRoles), usersRolesIDs)) {
                            if (botMember.hasPermission(Permission.MANAGE_ROLES) || botMember.hasPermission(Permission.ADMINISTRATOR)) {

                                Member mentionedMember = guild.retrieveMember(mentionedUser).complete();

                                List<Role> roles = mentionedMember.getRoles();
                                int userRolePos = 0;

                                if (!roles.isEmpty()){
                                    userRolePos = roles.get(0).getPosition();
                                }

                                if (botRolePos > userRolePos || selfUserRolePos > userRolePos){

                                    Role muteRole = null;

                                    if (!mutedRole.isBlank()) muteRole = guild.getRoleById(mutedRole.replace(",", ""));

                                    if (muteRole != null) {

                                        if (mentionedMember.getRoles().contains(muteRole)){
                                            guild.removeRoleFromMember(mentionedMember, muteRole).queue();
                                        } else {
                                            guild.addRoleToMember(mentionedMember, muteRole).queue();
                                        }

                                    } else {
                                        RolesNotSet.ChannelFriendly(textChannel, "MutedRole", setRoleConfig, user, toggle);
                                    }
                                } else {
                                    RoleTooHigh.send(textChannel, "mute", user);
                                }
                            } else {
                                NoPerms.Bot("Manage Roles", textChannel, user);
                            }
                        } else {
                            NoPerms.Send("mute", RoleFormatter.format(requiredRoles, guild), textChannel, user);
                        }
                    } else {
                        WrongCommandUsage.send(textChannel, example, "You haven't mentioned any members", user);
                    }
                } else {
                    RolesNotSet.ChannelFriendly(textChannel, "mute", set, user, toggle);
                }
            } else {
                WrongCommandUsage.send(textChannel, example, "Wrong amount of args", user);
            }
        }

    }

    public static String getInfo(){
        return info;
    }

    public static String getExample(){
        return example;
    }

    public static String getSet(){
        return set;
    }

    public static String getToggle(){
        return toggle;
    }

}
