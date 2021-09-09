package commands.mod;

import Core.*;
import ErrorMessages.UserError.NoPerms;
import ErrorMessages.UserError.RoleTooHigh;
import ErrorMessages.UserError.RolesNotSet;
import ErrorMessages.UserError.WrongCommandUsage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.List;

public class Kick {

    private static final String example = "`kick <@user/userID> <REASON> <--` (optional)";
    private static final String info = "This command kicks the specified user.";
    private static final String set = "`set roles KickRoles <@role(S)>`";
    private static final String log = "`set channel KickLog <channelID>`";
    private static final String toggle = "`set module ModCommands 1/0`";


    public static void Execute(Guild guild, User mentioned, String[] args, String request, TextChannel txt, User user){
        String reason = request.replace(args[0] + " " + args[1], "");
        guild.kick(mentioned.getId(), reason).queue();

        EmbedBuilder em = Embed.em(user, txt);
        em.setTitle("Kicked " + mentioned.getAsTag());
        txt.sendMessageEmbeds(em.build()).queue(MessageRemover::deleteAfter);

        ModLogger.log(txt, mentioned, "KickLog", reason, log, "was kicked", user);

    }

    public static void check(User user, Message msg, TextChannel channel, Guild guild, String request, Member member) {

        JDA jda = Main.getCurrentShard(guild);
        String[] args = request.split("\\s+");
        User MentionedUser = null;
        Role botrole = guild.getSelfMember().getRoles().get(0);
        Member botMember = guild.getSelfMember();
        String[] roles = SettingGetter.ChannelFriendlySet("KickRoles", channel).split(",");
        List<Role> userroles = member.getRoles();
        List<String> usersRoles = new ArrayList<>();
        StringBuilder req = new StringBuilder();
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
                        MentionedUser = jda.retrieveUserById(args[1]).complete();
                        check = 1;
                    } catch (Exception ignored) {
                    }

                    if (check == 1) {

                        for (Role userrole : userroles) {
                            usersRoles.add(userrole.getId());
                        }

                        if (CollectionUtils.containsAny(Arrays.asList(roles), usersRoles)) {

                            // Permissions stuffs + hierarchy

                            if (botMember.hasPermission(Permission.KICK_MEMBERS) || botMember.hasPermission(Permission.ADMINISTRATOR)) {

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
                                    RoleTooHigh.send(channel, "kick", user);
                                }

                            } else {
                                NoPerms.Bot("Kick Members", channel, user);
                            }
                        } else {
                            for (String s : roles) {
                                Role role = guild.getRoleById(s);
                                req.append("@").append(role.getName()).append(" ");
                            }
                            NoPerms.Send("kick", req.toString(), channel, user);
                        }
                    } else {
                        WrongCommandUsage.send(channel, example, "You haven't mentioned any members", user);
                    }
                } else {
                    RolesNotSet.ChannelFriendly(channel, "kick", set, user, toggle);
                }
            } else {
                WrongCommandUsage.send(channel, example, "Wrong amount of args", user);
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
