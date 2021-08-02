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
import java.util.concurrent.TimeUnit;

public class SettingSetter {

    public static String example(){
        String example = "`set module <module> 1/0` <-- (on/off)\n" +
                "`set roles <module> <@role(s)>` <-- **Each role should be separated by a comma**\n" +
                "`set channel <module> <channelID>` <-- also works with categories";
        return example;
    }

    public static void SettingChanged(TextChannel txt){
        EmbedBuilder em = new EmbedBuilder();
        em.setColor(Color.decode(SettingGetter.ChannelFriendlySet("GuildColour", txt)));
        em.setTitle("Your setting has been changed");

        txt.sendMessage(em.build()).queue(msg -> {
            msg.delete().queueAfter(10, TimeUnit.SECONDS);
        });
    }

    public static void check(User user, String request, Guild guild, TextChannel channel, Message msg){
        String[] args = request.split("\\s+");

        if (guild.getMemberById(user.getId()).getPermissions().contains(Permission.ADMINISTRATOR)) {
            if (args.length > 2) {

                String mod = args[1];

                switch (mod) {
                    case "module":
                        modules(guild, channel, args);
                        break;
                    case "roles":
                        roles(guild, channel, args, msg);
                        break;
                    case "channel":
                        channels(guild, channel, args);
                        break;
                }

            } else {
                WrongCommandUsage.send(channel, example(), "You didn't supply enough args");
            }
        } else {
            NoPerms.Send("set", "administrator", channel);
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

    public static void modules(Guild guild, TextChannel channel, String[] args){
        SettingCreator.check(guild);
        String guildID = guild.getId();

        String[] settings = {"ModCommands"};
        String[] oneOrZero = {"1", "0"};
        String setTo = args[3];

        if (args.length == 4) {
            if (Arrays.asList(settings).contains(args[2])) {
                if (Arrays.asList(oneOrZero).contains(args[3])) {

                    Set(args, channel, guildID, setTo);

                } else {
                    WrongCommandUsage.send(channel, example(), "To turn something on or off use 1 or 0");
                }
            } else {
                WrongCommandUsage.send(channel, example(), "The module you specified doesn't exist");
            }
        } else {
            WrongCommandUsage.send(channel, example(), "You didn't supply enough args");
        }
    }

    public static void roles(Guild guild, TextChannel channel, String[] args, Message msg){

        String[] modules = {"ClearRoles", "KickRoles", "BanRoles", "WarnRoles"};
        String guildID = guild.getId();
        List<Role> mentionedRoles = msg.getMentionedRoles();
        String setTo = "";

        if (args.length > 3){
            if (Arrays.asList(modules).contains(args[2])){
                if (!(mentionedRoles.size() == 0)){

                    for (int i = 0; mentionedRoles.size() > i; i++){
                        setTo = setTo + mentionedRoles.get(i).getId() + ",";
                    }
                    Set(args, channel, guildID, setTo);
                } else {
                    WrongCommandUsage.send(channel, example(), "No roles were mentioned");
                }
            } else {
                WrongCommandUsage.send(channel, example(), "Roles not supported by module or module doesn't exist");
            }
        } else {
            WrongCommandUsage.send(channel, example(), "Wrong amount of args");
        }
    }

    public static void channels(Guild guild, TextChannel channel, String[] args){

        String[] textMod = {"KickLog", "BanLog", "WarnLog"};
        String[] voiceMod = {};
        String[] catMod = {};
        String guildID = guild.getId();

        if (args.length == 4 && args[3].matches("[0-9]+")) {

            String setTo = args[3]; //This is here to stop random errors

            if (guild.getTextChannelById(args[3]) != null) {
                if (Arrays.asList(textMod).contains(args[2])){
                    Set(args, channel, guildID, setTo);
                } else {
                    WrongCommandUsage.send(channel, example(), "The ID you gave isn't compatible with that module");
                }

            } else if (guild.getVoiceChannelById(args[3]) != null) {
                if (Arrays.asList(voiceMod).contains(args[2])){
                    Set(args, channel, guildID, setTo);
                } else {
                    WrongCommandUsage.send(channel, example(), "The ID you gave isn't compatible with that module");
                }

            } else if (guild.getCategoryById(args[3]) != null) {
                if (Arrays.asList(catMod).contains(args[2])){
                    System.out.println("bean");
                    Set(args, channel, guildID, setTo);
                } else {
                    WrongCommandUsage.send(channel, example(), "The ID you gave isn't compatible with that module");
                }

            } else {
                WrongCommandUsage.send(channel, example(), "Your channel / category ID wasn't valid");
            }
        } else {
            WrongCommandUsage.send(channel, example(), "Wrong amount of args");
        }
    }

}
