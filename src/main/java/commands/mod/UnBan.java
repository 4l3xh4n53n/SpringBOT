package commands.mod;

import Core.Embed;
import Core.MessageRemover;
import Core.ModLogger;
import Core.SettingGetter;
import ErrorMessages.UserError.NoPerms;
import ErrorMessages.UserError.RolesNotSet;
import ErrorMessages.UserError.WrongCommandUsage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.List;

public class UnBan {

    public static String example = "`unban <USERID>`";
    public static String info = "Un bans a user";
    public static String set = "`set roles BanRoles <@role(S)>`";
    public static String log = "`set channel BanLog <channelID>`";

    public static void Execute(Guild guild, User mentioned, TextChannel txt, User user){
        int check = 0;
        try {
            guild.unban(mentioned).complete();
            check = 1;
        } catch (Exception x){
            WrongCommandUsage.send(txt, example, "Member isn't banned", user);
        }

        if (check == 1) {

            EmbedBuilder em = Embed.em(user, txt);
            em.setTitle("Un banned " + mentioned.getAsTag());
            txt.sendMessage(em.build()).queue(MessageRemover::deleteAfter);
        }

        ModLogger.log(txt, mentioned, "BanLog", "", log, "was unbanned",user);

    }

    public static void check(User user, Message msg, TextChannel txt, Guild guild, String request){

        String[] args = request.split("\\s+");
        Role botrole = guild.getBotRole();
        User mentioned = null;
        String[] roles = SettingGetter.ChannelFriendlySet("BanRoles", txt).split(",");
        List<Role> userroles = guild.getMemberById(user.getId()).getRoles();
        List<String> usersRoles = new ArrayList<>();
        String req = "";
        int check = 0;
        int checktwo = 0;

        int rolecheck = RoleChecker.CheckRoles(roles, guild);

        if (SettingGetter.ChannelFriendlySet("ModCommands", txt).equals("1")) {
            if (args.length == 2) {

                // Makes sure they have the roles setup

                if (rolecheck == 1) {
                    try {
                        mentioned = guild.getJDA().retrieveUserById(args[1]).complete();
                        checktwo = 1;
                    } catch (Exception x) {
                        // No members were mentioned
                    }

                    // Makes sure everything else is ok

                    if (check == 1) {

                        for (int i = 0; userroles.size() > i; i++) {
                            usersRoles.add(userroles.get(i).getId());
                        }
                        if (checktwo == 1) {
                            if (CollectionUtils.containsAny(Arrays.asList(roles), usersRoles)) {
                                if (botrole.hasPermission(Permission.BAN_MEMBERS) || botrole.hasPermission(Permission.ADMINISTRATOR)) {
                                    Execute(guild, mentioned, txt, user);
                                } else {
                                    NoPerms.Bot("Ban Members", txt, user);
                                }
                            } else {
                                for (int i = 0; roles.length > i; i++) {
                                    Role role = guild.getRoleById(roles[i]);
                                    req = req + "@" + role.getName() + " ";
                                }
                                NoPerms.Send("ban", req, txt, user);
                            }
                        } else {
                            WrongCommandUsage.send(txt, example, "You haven't mentioned any members", user);
                        }
                    }
                } else {
                    RolesNotSet.ChannelFriendly(txt, "ban", set, user);
                }
            } else {
                WrongCommandUsage.send(txt, example, "Wrong amount of args", user);
            }
        }
    }
}
