package commands.mod;

import Core.MessageRemover;
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
import java.util.stream.StreamSupport;

public class Ban {

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

    public static void Execute(Guild guild, User mentioned, String[] args, String request, TextChannel txt){
        String reason = request.replace(args[0] + " " + args[1], "");
        guild.ban(mentioned, 0, reason).queue();

        EmbedBuilder em = new EmbedBuilder();
        em.setColor(Color.decode(SettingGetter.ChannelFriendlySet("GuildColour", txt)));
        em.setTitle("Banned " + mentioned.getAsTag());
        txt.sendMessage(em.build()).queue(MessageRemover::deleteAfter);

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

                    if (botrole.hasPermission(Permission.BAN_MEMBERS) || botrole.hasPermission(Permission.ADMINISTRATOR)) {

                        int botRolePos = botrole.getPosition();
                        int selfUserRolePos = guild.getSelfMember().getRoles().get(0).getPosition();
                        int userRolePos = -1;

                        try {
                            userRolePos = guild.retrieveMemberById(MentionedUser.getId()).complete().getRoles().get(0).getPosition();
                            System.out.println("they got roles");
                        } catch (Exception ignored) {
                            System.out.println("they NOT got roles");
                        }

                        if (botRolePos > userRolePos || selfUserRolePos > userRolePos) {
                            Execute(guild, MentionedUser, args, request, channel);
                        } else {
                            RoleTooHigh.send(channel, "ban");
                        }

                    } else {
                        NoPerms.Bot("Ban Members", channel);
                    }
                } else {
                    for (int i = 0; roles.length > i; i++) {
                        Role role = guild.getRoleById(roles[i]);
                        req = req + "@" + role.getName() + " ";
                    }
                    NoPerms.Send("ban", req, channel);
                }
            } else {
                WrongCommandUsage.send(channel, example(), "You haven't mentioned any members", request);
            }
        } else {
            WrongCommandUsage.send(channel, example(), "Wrong amount of args", request);
        }
    }
}
