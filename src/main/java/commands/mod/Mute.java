package commands.mod;

import Core.Settings.SettingGetter;
import Utility.GetMentioned;
import Utility.GetRoleIDs;
import Utility.RoleChecker;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Arrays;
import java.util.List;

public class Mute {

    public static void mute(User user, Message msg, TextChannel textChannel, Guild guild, String request, Member member){
        // todo, make a mute role, also have the mute role automatically set up when the command is used

        String mutedRole = SettingGetter.ChannelFriendlyGet("MutedRole", textChannel); // todo make sure that this is legit if not make a new role

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
                            if (botMember.hasPermission(Permission.KICK_MEMBERS) || botMember.hasPermission(Permission.ADMINISTRATOR)) {

                            }
                        }
                    }
                }
            }
        }

    }

}
