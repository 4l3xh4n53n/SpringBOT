package Core.Settings;

import ErrorMessages.UserError.NoPerms;
import ErrorMessages.UserError.WrongCommandUsage;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;

import java.util.List;

public class SetMutedRole {

    private static final String example = "setMutedRole <@role>";
    private static final String info = "Sets the muted role that the mute command works";

    public static void Set(TextChannel textChannel, String content, Message message, User user, Member member, Guild guild){

        if (member.hasPermission(Permission.ADMINISTRATOR)){

            String[] args = content.split("\\s+");
            List<Role> mentionedRoles = message.getMentionedRoles();
            String roleID = null;

            if (args.length > 1){

                if (!mentionedRoles.isEmpty()) {

                    Role mentionedRole = mentionedRoles.get(0);
                    roleID = mentionedRole.getId();

                } else {

                    Role mentionedRole = null;
                    try {
                        mentionedRole = guild.getRoleById(args[1]);
                        roleID = mentionedRole.getId();
                    } catch (Exception ignored){}

                }

                if (roleID != null){

                    SettingSetter.ExternalSet(guild, guild.getId(), "MutedRole", roleID, "Don't use this command");
                    SettingSetter.SettingChanged(textChannel);

                } else {
                    WrongCommandUsage.send(textChannel, example, "You didn't mention a Role", user);
                }

            } else {
                WrongCommandUsage.send(textChannel, example, "You didn't supply enough args", user);
            }

        } else {
            NoPerms.Send("set", "administrator", textChannel, user);
        }

    }

    public static String getExample(){
        return example;
    }

    public static String getInfo(){
        return info;
    }

}
