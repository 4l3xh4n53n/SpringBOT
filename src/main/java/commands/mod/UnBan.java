package commands.mod;

import Core.Embed;
import Core.Main;
import Core.MessageRemover;
import Core.ModLogger;
import Core.SettingGetter;
import ErrorMessages.UserError.NoPerms;
import ErrorMessages.UserError.RolesNotSet;
import ErrorMessages.UserError.WrongCommandUsage;
import Utility.GetRoleIDs;
import Utility.RoleChecker;
import Utility.RoleFormatter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Arrays;
import java.util.List;

public class UnBan {

    private static final String example = "`unban <USERID>`";
    private static final String info = "Un bans the specified user.";
    private static final String set = "`set roles BanRoles <@role(S)>`";
    private static final String log = "`set channel BanLog <channelID>`";
    private static final String toggle = "`set module ModCommands 1/0`";

    private static void Execute(Guild guild, User mentioned, TextChannel textChannel, User user){
        int check = 0;

        try {
            guild.unban(mentioned).complete();
            check = 1;
        } catch (Exception x){
            WrongCommandUsage.send(textChannel, example, "Member isn't banned", user);
        }

        if (check == 1) {

            EmbedBuilder em = Embed.em(user, textChannel);
            em.setTitle("Un banned " + mentioned.getAsTag());
            textChannel.sendMessageEmbeds(em.build()).queue(MessageRemover::deleteAfter);

        }

        ModLogger.log(textChannel, mentioned, "BanLog", "", log, "was unbanned",user);

    }

    private static User getMentioned(Guild guild, String expectedPosition){
        JDA jda = Main.getCurrentShard(guild);
        User mentioned = null;

        try {
            mentioned = jda.retrieveUserById(expectedPosition).complete();
        } catch (Exception ignored) {
        }

        return mentioned;
    }

    public static void check(User user, TextChannel textChannel, Guild guild, String request, Member member){

        User mentioned;
        String[] args = request.split("\\s+");
        Member botMember = guild.getSelfMember();

        String[] requiredRoles = SettingGetter.ChannelFriendlySet("BanRoles", textChannel).split(",");
        List<Role> userRoles = member.getRoles();
        List<String> usersRoles = GetRoleIDs.get(userRoles);

        if (SettingGetter.ChannelFriendlySet("ModCommands", textChannel).equals("1")) {
            if (args.length == 2) {
                if (RoleChecker.areRolesValid(requiredRoles, guild) == 1) {
                    if ((mentioned = getMentioned(guild, args[1])) != null) {
                        if (CollectionUtils.containsAny(Arrays.asList(requiredRoles), usersRoles)) {
                            if (botMember.hasPermission(Permission.BAN_MEMBERS) || botMember.hasPermission(Permission.ADMINISTRATOR)) {

                                Execute(guild, mentioned, textChannel, user);

                            } else {
                                NoPerms.Bot("Ban Members", textChannel, user);
                            }
                        } else {
                            NoPerms.Send("ban", RoleFormatter.format(requiredRoles, guild), textChannel, user);
                        }
                    } else {
                        WrongCommandUsage.send(textChannel, example, "You haven't mentioned any members", user);
                    }
                } else {
                    RolesNotSet.ChannelFriendly(textChannel, "ban", set, user, toggle);
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
