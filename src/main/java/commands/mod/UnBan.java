package commands.mod;

import Core.*;
import ErrorMessages.UserError.NoPerms;
import ErrorMessages.UserError.RolesNotSet;
import ErrorMessages.UserError.WrongCommandUsage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.List;

public class UnBan {

    private static final String example = "`unban <USERID>`";
    private static final String info = "Un bans the specified user.";
    private static final String set = "`set roles BanRoles <@role(S)>`";
    private static final String log = "`set channel BanLog <channelID>`";
    private static final String toggle = "`set module ModCommands 1/0`";

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

    public static void check(User user, TextChannel txt, Guild guild, String request){

        JDA jda = Main.getCurrentShard(guild);
        String[] args = request.split("\\s+");
        Member botMember = guild.getSelfMember();
        User mentioned = null;
        String[] roles = SettingGetter.ChannelFriendlySet("BanRoles", txt).split(",");
        List<Role> userroles = guild.getMemberById(user.getId()).getRoles();
        List<String> usersRoles = new ArrayList<>();
        StringBuilder req = new StringBuilder();
        int checktwo = 0;

        int rolecheck = RoleChecker.CheckRoles(roles, guild);

        if (SettingGetter.ChannelFriendlySet("ModCommands", txt).equals("1")) {
            if (args.length == 2) {

                if (rolecheck == 1) {
                    try {
                        mentioned = jda.retrieveUserById(args[1]).complete();
                        checktwo = 1;
                    } catch (Exception x) {
                        // No members were mentioned
                    }

                    for (Role userrole : userroles) {
                        usersRoles.add(userrole.getId());
                    }

                    if (checktwo == 1) {
                        if (CollectionUtils.containsAny(Arrays.asList(roles), usersRoles)) {
                            if (botMember.hasPermission(Permission.BAN_MEMBERS) || botMember.hasPermission(Permission.ADMINISTRATOR)) {
                                Execute(guild, mentioned, txt, user);
                            } else {
                                NoPerms.Bot("Ban Members", txt, user);
                            }
                        } else {
                            for (String s : roles) {
                                Role role = guild.getRoleById(s);
                                req.append("@").append(role.getName()).append(" ");
                            }
                            NoPerms.Send("ban", req.toString(), txt, user);
                        }
                    } else {
                        WrongCommandUsage.send(txt, example, "You haven't mentioned any members", user);
                    }

                } else {
                    RolesNotSet.ChannelFriendly(txt, "ban", set, user, toggle);
                }
            } else {
                WrongCommandUsage.send(txt, example, "Wrong amount of args", user);
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
