package commands.mod;

import Core.SettingGetter;
import ErrorMessages.UserError.NoPerms;
import ErrorMessages.UserError.RoleTooHigh;
import ErrorMessages.UserError.RolesNotSet;
import ErrorMessages.UserError.WrongCommandUsage;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ban {

    public static String example(){
        String example = "`ban <@user> <REASON> <--` (optional)";
        return example;
    }

    public static String info(){
        String info = "Bans a user";
        return info;
    }

    public static String set(){
        String set = "`set roles BanRoles <@role(S)>`";
        return set;
    }

    public static void Execute(Guild guild, Message msg, Member mentioned, String[] args, String request){

        String reason = request.replace(args[0] + " " + args[1], "");

        guild.ban(mentioned, 0, reason).queue();

    }

    public static void check(User user, Message msg, TextChannel txt, Guild guild, String request){

        String[] args = request.split("\\s+");
        Role botrole = guild.getBotRole();
        List<Member> mentioned = msg.getMentionedMembers();
        String[] roles = SettingGetter.ChannelFriendlySet("BanRoles", txt).split(",");
        List<Role> userroles = guild.getMemberById(user.getId()).getRoles();
        List<String> usersRoles = new ArrayList<>();
        String req = "";
        int check = 0;

        if (args.length > 1) {

            // Makes sure they have the roles setup

            try {

                for (int i = 0; roles.length > i; i++) {
                    guild.getRoleById(roles[i]);
                }
                check = 1;

            } catch (Exception x) {
                RolesNotSet.ChannelFriendly(txt, "ClearRoles", set());
            }

            // Makes sure everything else is ok

            if (check == 1) {

                for (int i = 0; userroles.size() > i; i++) {
                    usersRoles.add(userroles.get(i).getId());
                }

                if (!mentioned.isEmpty()) {
                    if (CollectionUtils.containsAny(Arrays.asList(roles), usersRoles)) {

                        // Permissions stuffs + hierarchy

                        int botRolePos = botrole.getPosition();
                        int selfUserRolePos = guild.getSelfMember().getRoles().get(0).getPosition();
                        int userRolePos = -1;
                        try {
                            userRolePos = msg.getMentionedMembers().get(0).getRoles().get(0).getPosition();
                        } catch (Exception x){
                            Execute(guild, msg, mentioned.get(0), args, request);
                        }

                        if (botrole.hasPermission(Permission.BAN_MEMBERS) || botrole.hasPermission(Permission.ADMINISTRATOR)) {
                            if (botRolePos > userRolePos || selfUserRolePos > userRolePos) {

                                Execute(guild, msg, mentioned.get(0), args, request);
                            } else {
                                RoleTooHigh.send(txt, "ban");
                            }

                        } else {
                            NoPerms.Bot("Ban Members", txt);
                        }

                    } else {

                        for (int i = 0; roles.length > i; i++) {
                            Role role = guild.getRoleById(roles[i]);
                            req = req + "@" + role.getName() + " ";
                        }
                        NoPerms.Send("ban", req, txt);

                    }
                } else {
                    WrongCommandUsage.send(txt, example(), "You haven't mentioned any members", request);
                }
            }
        } else {
            WrongCommandUsage.send(txt, example(), "Wrong amount of args", request);
        }
    }
}
