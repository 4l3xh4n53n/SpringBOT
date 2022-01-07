package Core.Settings;

import Core.Database;
import Core.Embed;
import Core.MessageRemover;
import ErrorMessages.BadCode.SQLError;
import ErrorMessages.UserError.NoPerms;
import ErrorMessages.UserError.WrongCommandUsage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class SettingSetter {

    public static String example = "MODULES: `set <modules> 1/0` <-- (on/off)\n" +
            "ROLES: `set <module> <@role(s)>` \n" +
            "CHANNELS: `set <module> <channelID>` <-- also works with categories";

    public static void SettingChanged(TextChannel txt){
        EmbedBuilder em = new EmbedBuilder();
        em.setColor(Color.decode(SettingGetter.ChannelFriendlyGet("GuildColour", txt)));
        em.setTitle("Your setting has been changed");
        txt.sendMessageEmbeds(em.build()).queue(MessageRemover::deleteAfter);
    }

    public static void NoSuchSetting(TextChannel txt, User sender){
        EmbedBuilder em = Embed.em(sender, txt);
        em.setTitle("No Such Setting");
        em.setTitle("Please run `help settings` command.");
        txt.sendMessageEmbeds(em.build()).queue();
    }

    /**
     * Checks the settings to make sure that everything is correct. <br>
     * This should be used from the settings commands
     * @param user User executing the command
     * @param request The commands content ( the raw string excluding the command itself )
     * @param guild The guild the command was sent in
     * @param channel The text channel the command was sent in
     * @param msg The message object
     * @param member The member executing the command
     */
    public static void check(User user, String request, Guild guild, TextChannel channel, Message msg, Member member){
        String[] args = request.split("\\s+");

        if (member.getPermissions().contains(Permission.ADMINISTRATOR)) {
            if (args.length > 1) {

                String mod = args[1].toLowerCase(Locale.ROOT);

                String[] modules = {"modcommands", "logmodactions", "coins", "sendcoins", "autorole", "invitelogging", "privatechannel", "serverstats", "statsTotal", "statsBot", "statsMember", "gamecommands", "chatfilter", "guildwelcome", "reactionroles", "tickets", "counting"};
                String[] channels = {"kicklog", "banlog", "warnlog", "guildwelcomechannel", "invitelog","privatechannelcreator", "statschannel","privatechannelcategory", "countingchannel"};
                String[] roles = {"clearroles", "kickroles", "banroles", "warnroles", "muteroles", "autorolerole", "pollrole"};

                if (Arrays.asList(modules).contains(mod)){
                    modules(guild, channel, args, user);
                } else if (Arrays.asList(channels).contains(mod)){
                    channels(guild, channel, args, user);
                } else if (Arrays.asList(roles).contains(mod)){
                    roles(guild, channel, args, msg, user);
                } else {
                    NoSuchSetting(channel, user);
                }

            } else {
                WrongCommandUsage.send(channel, example, "You didn't supply enough args", user);
            }
        } else {
            NoPerms.Send("set", "administrator", channel, user);
        }
    }

    /**
     * This is for changing settings outside this class. <br>
     * Input sanitization should be done before sending settings here.
     * @param guild The guild that the setting is going to be changed for
     * @param guildID The id of that guild
     * @param setting The setting that is going to be changed
     * @param setTo What the setting is going to be changed to
     * @param toggle How to turn the module off ( in case of a bug or the server admins not wanting to set it up )
     */
    public static void ExternalSet(Guild guild, String guildID, String setting, String setTo, String toggle){
        try {
            Connection con = Database.connect();
            String update = "UPDATE Settings SET '" + setting + "'= ? WHERE GuildID='" + guildID + "'";

            SettingGetter.UpdateSetting(guildID, setting, setTo);

            PreparedStatement ud = con.prepareStatement(update);
            ud.setString(1, setTo);
            ud.executeUpdate();
            con.close();
            ud.close();
        } catch (Exception x){
            SQLError.GuildFriendly(guild, x, toggle);
        }
    }

    public static void Set(String[] args, TextChannel channel, String guildID, String setTo){
        try {

            String setting = args[1];
            Connection con = Database.connect();
            String update = "UPDATE Settings SET '" + setting + "'= ? WHERE GuildID='" + guildID + "'";

            SettingGetter.UpdateSetting(guildID, setting, setTo);
            SettingChanged(channel);

            PreparedStatement ud = con.prepareStatement(update);
            ud.setString(1, setTo);
            ud.executeUpdate();
            con.close();
            ud.close();


        } catch (Exception x) {
            SQLError.TextChannel(channel, x, "Can't turn this off sorry.");
        }
    }

    public static String modules = "`ModCommands, LogModActions, Coins, SendCoins, AutoRole, InviteLogging, PrivateChannel, ServerStats, statsTotal, statsBot, statsMember, GameCommands, Poll, ChatFilter, GuildWelcome, ReactionRoles, Tickets, Counting`";

    public static void modules(Guild guild, TextChannel channel, String[] args, User user){
        String guildID = guild.getId();

        String[] oneOrZero = {"1", "0"};
        String setTo = args[2];
        if (args.length == 3) {
            if (Arrays.asList(oneOrZero).contains(args[2])) {

                Set(args, channel, guildID, setTo);

            } else {
                WrongCommandUsage.send(channel, example, "To turn something on or off use 1 or 0", user);
            }
        } else {
            WrongCommandUsage.send(channel, example, "You didn't supply enough args", user);
        }
    }

    public static String roles = "`ClearRoles, KickRoles, BanRoles, WarnRoles, MuteRoles, AutoRoleRole, PollRole, TicketRole`";

    public static void roles(Guild guild, TextChannel channel, String[] args, Message msg, User user){

        String[] modules = {"clearroles", "kickroles", "banroles", "warnroles", "muteroles", "autorolerole", "pollrole", "ticketrole"};
        String guildID = guild.getId();
        List<Role> mentionedRoles = msg.getMentionedRoles();
        StringBuilder setTo = new StringBuilder();

        if (args.length > 2){
            if (Arrays.asList(modules).contains(args[1].toLowerCase(Locale.ROOT))){
                if (!(mentionedRoles.size() == 0)){
                    for (Role mentionedRole : mentionedRoles) {
                        System.out.println("ok");
                        setTo.append(mentionedRole.getId()).append(",");
                        System.out.println(mentionedRole.getId());
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

    public static String channels = "`KickLog, BanLog, WarnLog, GuildWelcomeChannel, InviteLog. PrivateChannelCreator, PrivateChannelCategory, TicketCategory, CountingChannel`";

    public static void channels(Guild guild, TextChannel channel, String[] args, User user){

        String[] textMod = {"kicklog", "banlog", "warnlog", "guildwelcomechannel", "invitelog", "countingchannel"};
        String[] voiceMod = {"privatechannelcreator"};
        String[] catMod = {"privatechannelcategory", "ticketcategory"};
        String guildID = guild.getId();

        if (args.length == 3 && args[2].matches("[0-9]+")) {

            String setTo = args[2]; //This is here to stop random errors

            if (guild.getTextChannelById(args[2]) != null) {
                if (Arrays.asList(textMod).contains(args[1].toLowerCase(Locale.ROOT))){
                    Set(args, channel, guildID, setTo);
                } else {
                    WrongCommandUsage.send(channel, example, "The ID you gave isn't compatible with that module", user);
                }

            } else if (guild.getVoiceChannelById(args[2]) != null) {
                if (Arrays.asList(voiceMod).contains(args[1].toLowerCase(Locale.ROOT))){
                    Set(args, channel, guildID, setTo);
                } else {
                    WrongCommandUsage.send(channel, example, "The ID you gave isn't compatible with that module", user);
                }

            } else if (guild.getCategoryById(args[2]) != null) {
                if (Arrays.asList(catMod).contains(args[1].toLowerCase(Locale.ROOT))){
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
