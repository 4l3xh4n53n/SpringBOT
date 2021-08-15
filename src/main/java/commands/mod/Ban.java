package commands.mod;

import Core.Embed;
import Core.MessageRemover;
import Core.ModLogger;
import Core.SettingGetter;
import ErrorMessages.UserError.NoPerms;
import ErrorMessages.UserError.RoleTooHigh;
import ErrorMessages.UserError.RolesNotSet;
import ErrorMessages.UserError.WrongCommandUsage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.List;

public class Ban {

    public static String example = "`ban <@user/userID> <REASON> <--` (optional)";
    public static String info = "This command bans the specified user.";
    public static String set = "`set roles BanRoles <@role(S)>`";
    public static String log = "`set channel BanLog <channelID>`";

    public static void Execute(Guild guild, User mentioned, String[] args, String request, TextChannel txt, User user){
        String reason = request.replace(args[0] + " " + args[1], "");
        guild.ban(mentioned, 0, reason).queue();

        EmbedBuilder em = Embed.em(user, txt);
        em.setTitle("Banned " + mentioned.getAsTag());
        txt.sendMessage(em.build()).queue(MessageRemover::deleteAfter);

        ModLogger.log(txt, mentioned, "BanLog", reason, log, "was banned", user);

    }

    public static void check(User user, Message msg, TextChannel channel, Guild guild, String request) {

        String[] args = request.split("\\s+");
        User MentionedUser = null;
        Role botrole = guild.getBotRole();
        String[] roles = SettingGetter.ChannelFriendlySet("BanRoles", channel).split(",");
        List<Role> userroles = guild.getMemberById(user.getId()).getRoles();
        List<String> usersRoles = new ArrayList<>();
        String req = "";
        int check = 0;

        int rolecheck = RoleChecker.CheckRoles(roles, guild);

        if (SettingGetter.ChannelFriendlySet("ModCommands", channel).equals("1")) {
            if (args.length > 1) {
                if (rolecheck == 1) {
                    try {
                        MentionedUser = msg.getMentionedUsers().get(0);
                        check = 1;
                    } catch (Exception ignored) {
                    }

                    try {
                        MentionedUser = guild.getJDA().retrieveUserById(args[1]).complete();
                        check = 1;
                    } catch (Exception ignored) {
                    }

                    if (check == 1) {

                        for (int i = 0; userroles.size() > i; i++) {
                            usersRoles.add(userroles.get(i).getId());
                        }

                        if (CollectionUtils.containsAny(Arrays.asList(roles), usersRoles)) {

                            // Permissions stuffs + hierarchy

                            if (botrole.hasPermission(Permission.BAN_MEMBERS) || botrole.hasPermission(Permission.ADMINISTRATOR)) {

                                int botRolePos = botrole.getPosition();
                                int selfUserRolePos = guild.getSelfMember().getRoles().get(0).getPosition();
                                int userRolePos = -1;

                                try {
                                    userRolePos = guild.retrieveMemberById(MentionedUser.getId()).complete().getRoles().get(0).getPosition();
                                } catch (Exception ignored) {
                                }

                                if (botRolePos > userRolePos || selfUserRolePos > userRolePos) {
                                    Execute(guild, MentionedUser, args, request, channel, user);
                                } else {
                                    RoleTooHigh.send(channel, "ban", user);
                                }

                            } else {
                                NoPerms.Bot("Ban Members", channel, user);
                            }
                        } else {
                            for (int i = 0; roles.length > i; i++) {
                                Role role = guild.getRoleById(roles[i]);
                                req = req + "@" + role.getName() + " ";
                            }
                            NoPerms.Send("ban", req, channel, user);
                        }
                    } else {
                        WrongCommandUsage.send(channel, example, "You haven't mentioned any members", user);
                    }
                } else {
                    RolesNotSet.ChannelFriendly(channel, "ban", set, user);
                }
            } else {
                WrongCommandUsage.send(channel, example, "Wrong amount of args", user);
            }
        }
    }
}
