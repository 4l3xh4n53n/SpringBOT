package Core;

import ErrorMessages.BadCode.SQLError;
import ErrorMessages.UserError.NoPerms;
import ErrorMessages.UserError.WrongCommandUsage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;

import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

public class SettingSetter {

    public static String example = "`set module <module> 1/0` <-- (on/off)\n" +
            "`set roles <module> <@role(s)>` \n" +
            "`set channel <module> <channelID>` <-- also works with categories";

    public static void SettingChanged(TextChannel txt){
        EmbedBuilder em = new EmbedBuilder();
        em.setColor(Color.decode(SettingGetter.ChannelFriendlySet("GuildColour", txt)));
        em.setTitle("Your setting has been changed");

        txt.sendMessage(em.build()).queue(MessageRemover::deleteAfter);
    }

    public static void check(User user, String request, Guild guild, TextChannel channel, Message msg, Member member){
        String[] args = request.split("\\s+");

        if (member.getPermissions().contains(Permission.ADMINISTRATOR)) {
            if (args.length > 2) {

                String mod = args[1];

                switch (mod) {
                    case "module":
                        modules(guild, channel, args, user);
                        break;
                    case "roles":
                        roles(guild, channel, args, msg, user);
                        break;
                    case "channel":
                        channels(guild, channel, args, user);
                        break;
                }

            } else {
                WrongCommandUsage.send(channel, example, "You didn't supply enough args", user);
            }
        } else {
            NoPerms.Send("set", "administrator", channel, user);
        }
    }

    public static void Set(String[] args, TextChannel channel, String guildID, String setTo){
        try {
            Connection con = Database.connect();
            Statement stmt = con.createStatement();
            String update = "UPDATE Settings SET '" + args[2] + "'='" + setTo + "' WHERE GuildID='" + guildID + "'";
            SettingChanged(channel);
            ResultSet ud = stmt.executeQuery(update);
            ud.updateString(args[1], args[2]);
            ud.updateRow();
            con.close();
            stmt.close();
            ud.close();
        } catch (Exception x) {
            if (!x.getMessage().equals("query does not return ResultSet")) {
                SQLError.TextChannel(channel, x);
            }
        }
    }

    public static String modules = "`ModCommands, LogModActions, Coins, SendCoins, AutoRole, InviteLogger, PrivateChannel`";

    public static void modules(Guild guild, TextChannel channel, String[] args, User user){
        SettingCreator.check(guild);
        String guildID = guild.getId();

        String[] settings = {"ModCommands", "LogModActions", "Coins", "SendCoins", "AutoRole", "InviteLogger", "PrivateChannel"};
        String[] oneOrZero = {"1", "0"};
        String setTo = args[3];

        if (args.length == 4) {
            if (Arrays.asList(settings).contains(args[2])) {
                if (Arrays.asList(oneOrZero).contains(args[3])) {

                    Set(args, channel, guildID, setTo);

                } else {
                    WrongCommandUsage.send(channel, example, "To turn something on or off use 1 or 0", user);
                }
            } else {
                WrongCommandUsage.send(channel, example, "The module you specified doesn't exist", user);
            }
        } else {
            WrongCommandUsage.send(channel, example, "You didn't supply enough args", user);
        }
    }

    public static String roles = "`ClearRoles, KickRoles, BanRoles, WarnRoles, AutoRoleRole, PollRole`";

    public static void roles(Guild guild, TextChannel channel, String[] args, Message msg, User user){

        String[] modules = {"ClearRoles", "KickRoles", "BanRoles", "WarnRoles", "AutoRoleRole", "PollRole"};
        String guildID = guild.getId();
        List<Role> mentionedRoles = msg.getMentionedRoles();
        StringBuilder setTo = new StringBuilder();

        if (args.length > 3){
            if (Arrays.asList(modules).contains(args[2])){
                if (!(mentionedRoles.size() == 0)){

                    for (Role mentionedRole : mentionedRoles) {
                        setTo.append(mentionedRole.getId()).append(",");
                    }
                    Set(args, channel, guildID, setTo.toString());
                } else {
                    WrongCommandUsage.send(channel, example, "No roles were mentioned", user);
                }
            } else {
                WrongCommandUsage.send(channel, example, "Roles not supported by module or module doesn't exist", user);
            }
        } else {
            WrongCommandUsage.send(channel, example, "Wrong amount of args", user);
        }
    }

    public static String channels = "`KickLog, BanLog, WarnLog, GuildWelcomeChannel, InviteLog`";

    public static void channels(Guild guild, TextChannel channel, String[] args, User user){

        String[] textMod = {"KickLog", "BanLog", "WarnLog", "GuildWelcomeChannel", "InviteLog"};
        String[] voiceMod = {"PrivateChannelCreator"};
        String[] catMod = {"PrivateChannelCategory"};
        String guildID = guild.getId();

        if (args.length == 4 && args[3].matches("[0-9]+")) {

            String setTo = args[3]; //This is here to stop random errors

            if (guild.getTextChannelById(args[3]) != null) {
                if (Arrays.asList(textMod).contains(args[2])){
                    Set(args, channel, guildID, setTo);
                } else {
                    WrongCommandUsage.send(channel, example, "The ID you gave isn't compatible with that module", user);
                }

            } else if (guild.getVoiceChannelById(args[3]) != null) {
                if (Arrays.asList(voiceMod).contains(args[2])){
                    Set(args, channel, guildID, setTo);
                } else {
                    WrongCommandUsage.send(channel, example, "The ID you gave isn't compatible with that module", user);
                }

            } else if (guild.getCategoryById(args[3]) != null) {
                if (Arrays.asList(catMod).contains(args[2])){
                    Set(args, channel, guildID, setTo);
                } else {
                    WrongCommandUsage.send(channel, example, "The ID you gave isn't compatible with that module", user);
                }

            } else {
                WrongCommandUsage.send(channel, example, "Your channel / category ID wasn't valid", user);
            }
        } else {
            WrongCommandUsage.send(channel, example, "Wrong amount of args", user);
        }
    }

}
