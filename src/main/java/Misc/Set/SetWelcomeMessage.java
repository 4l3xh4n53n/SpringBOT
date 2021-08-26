package Misc.Set;

import Core.Database;
import Core.SettingSetter;
import ErrorMessages.BadCode.SQLError;
import ErrorMessages.UserError.NoPerms;
import ErrorMessages.UserError.WrongCommandUsage;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class SetWelcomeMessage {

    private static final String example = "`setWelcomeMessage <Message>`";

    public static void Check(Member member, String content, TextChannel txt, String guildID){

        User user = member.getUser();
        String[] args = content.split("\\s+");

        if (member.getPermissions().contains(Permission.ADMINISTRATOR)){
            if (args.length > 1){
                String message = content.replace(args[0], "");

                try{
                    Connection con = Database.connect();
                    String update = "UPDATE Settings SET GuildWelcomeMessage = ? WHERE GuildID = '" + guildID + "'";
                    SettingSetter.SettingChanged(txt);
                    PreparedStatement ud = con.prepareStatement(update);
                    ud.setString(1, message);
                    ud.executeUpdate();
                    ud.close();
                } catch (Exception x){
                    SQLError.TextChannel(txt, x, "Just don't run this command.");
                }

            } else {
                WrongCommandUsage.send(txt, example, "Wrong amount of args", user);
            }
        } else {
            NoPerms.Send("setWelcomeMessage", "ADMINISTRATOR", txt, user);
        }

    }

    public static String getExample() {
        return example;
    }
}
