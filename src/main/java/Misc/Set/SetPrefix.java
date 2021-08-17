package Misc.Set;

import Core.Database;
import Core.SettingSetter;
import ErrorMessages.BadCode.SQLError;
import ErrorMessages.UserError.WrongCommandUsage;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class SetPrefix {

    public static String example = "`setPrefix <newPrefix>`";
    public static String info = "This command changes your servers prefix (if you forget it the safe word is 'prefix')";

    public static void Set(TextChannel txt, String content, String guildID, User user, Guild guild){
        String[] args = content.split("\\s+");

        if (guild.getMemberById(user.getId()).getPermissions().contains(Permission.ADMINISTRATOR)) {
            if (args.length == 2) {

                String setTo = args[1];

                try {
                    Connection con = Database.connect();
                    Statement stmt = con.createStatement();
                    String update = "UPDATE Settings SET Prefix ='" + setTo + "' WHERE GuildID='" + guildID + "'";
                    SettingSetter.SettingChanged(txt);
                    ResultSet ud = stmt.executeQuery(update);
                    ud.updateString(args[1], args[2]);
                    ud.updateRow();
                    con.close();
                    stmt.close();
                    ud.close();
                } catch (Exception x) {
                    if (!x.getMessage().equals("query does not return ResultSet")) { // This is here because I used my old method of doing this which used result set.
                        SQLError.TextChannel(txt, x);
                    }
                }
            } else {
                WrongCommandUsage.send(txt, example, "Wrong amount of args", user);
            }

        }

    }

}
