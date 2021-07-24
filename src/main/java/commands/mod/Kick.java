package commands.mod;

import Core.MessageRemover;
import Core.ModLogger;
import Core.SettingGetter;
import ErrorMessages.UserError.NoPerms;
import ErrorMessages.UserError.RoleTooHigh;
import ErrorMessages.UserError.WrongCommandUsage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import org.apache.commons.collections4.CollectionUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Kick {

    public static String example(){
        String example = "`ban <@user/userID> <REASON> <--` (optional)";
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

    public static String log(){
        String log = "`set channel KickLog <channelID>`";
        return log;
    }

    public static void Execute(Guild guild, User mentioned, String[] args, String request, TextChannel txt, User user){
        String reason = request.replace(args[0] + " " + args[1], "");
        guild.kick(mentioned.getId(), reason).queue();

        EmbedBuilder em = new EmbedBuilder();
        em.setColor(Color.decode(SettingGetter.ChannelFriendlySet("GuildColour", txt)));
        em.setTitle("Kicked " + mentioned.getAsTag());
        txt.sendMessage(em.build()).queue(MessageRemover::deleteAfter);

        ModLogger.log(txt, mentioned, "KickLog", reason, log(), "was kicked", user);

    }

    public static void check(User user, Message msg, TextChannel channel, Guild guild, String request) {

        String[] args = request.split("\\s+");
        User MentionedUser = null;
        Role botrole = guild.getBotRole();
        String[] roles = SettingGetter.ChannelFriendlySet("KickRoles", channel).split(",");
        java.util.List<Role> userroles = guild.getMemberById(user.getId()).getRoles();
        List<String> usersRoles = new ArrayList<>();
        String req = "";
        int check = 0;

        if (args.length > 1) {

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

                    if (botrole.hasPermission(Permission.KICK_MEMBERS) || botrole.hasPermission(Permission.ADMINISTRATOR)) {

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
                            RoleTooHigh.send(channel, "kick");
                        }

                    } else {
                        NoPerms.Bot("Kick Members", channel);
                    }
                } else {
                    for (int i = 0; roles.length > i; i++) {
                        Role role = guild.getRoleById(roles[i]);
                        req = req + "@" + role.getName() + " ";
                    }
                    NoPerms.Send("kick", req, channel);
                }
            } else {
                WrongCommandUsage.send(channel, example(), "You haven't mentioned any members", request);
            }
        } else {
            WrongCommandUsage.send(channel, example(), "Wrong amount of args", request);
        }
    }

}
