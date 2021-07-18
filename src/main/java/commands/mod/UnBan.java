package commands.mod;

import Core.MessageRemover;
import Core.SettingGetter;
import ErrorMessages.UserError.NoPerms;
import ErrorMessages.UserError.RolesNotSet;
import ErrorMessages.UserError.WrongCommandUsage;
import gnu.trove.impl.sync.TSynchronizedShortByteMap;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import org.apache.commons.collections4.CollectionUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UnBan {

    public static String example(){
        String example = "`unban <USERID>`";
        return example;
    }

    public static String info(){
        String info = "Un bans a user";
        return info;
    }

    public static String set(){
        String set = "`set roles BanRoles <@role(S)>`";
        return set;
    }

    public static void Execute(Guild guild, User mentioned, TextChannel txt, String request){
        int check = 0;
        try {
            guild.unban(mentioned).complete();
            check = 1;
        } catch (Exception x){
            WrongCommandUsage.send(txt, example(), "Member isn't banned", request);
        }

        if (check == 1) {

            EmbedBuilder em = new EmbedBuilder();
            em.setColor(Color.decode(SettingGetter.ChannelFriendlySet("GuildColour", txt)));
            em.setTitle("Un banned " + mentioned.getAsTag());
            txt.sendMessage(em.build()).queue(MessageRemover::deleteAfter);
        }
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

        if (args.length == 2) {

            // Makes sure they have the roles setup

            try {

                for (int i = 0; roles.length > i; i++) {
                    guild.getRoleById(roles[i]);
                }
                check = 1;

            } catch (Exception x) {
                RolesNotSet.ChannelFriendly(txt, "ClearRoles", set());
            }
            try {
                mentioned = guild.getJDA().retrieveUserById(args[1]).complete();
                checktwo = 1;
            } catch (Exception x){
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
                            Execute(guild, mentioned, txt, request);
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
