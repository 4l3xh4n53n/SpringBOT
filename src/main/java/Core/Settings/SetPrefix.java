package Core.Settings;

import Core.Database;
import Core.Settings.SettingGetter;
import Core.Settings.SettingSetter;
import ErrorMessages.BadCode.SQLError;
import ErrorMessages.UserError.WrongCommandUsage;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class SetPrefix {

    private static final String example = "`setPrefix <newPrefix>`";
    private static final String info = "This command changes your servers prefix (if you forget it the safe word is 'prefix')";

    public static void Set(TextChannel txt, String content, String guildID, User user, Member member){

        String[] args = content.split("\\s+");

        if (member.getPermissions().contains(Permission.ADMINISTRATOR)) {
            if (args.length == 2) {

                try {
                    Connection con = Database.connect();
                    Statement stmt = con.createStatement();
                    String update = "UPDATE Settings SET Prefix = ? WHERE GuildID='" + guildID + "'";
                    SettingGetter.UpdateSetting(guildID, "Prefix", args[1]);
                    SettingSetter.SettingChanged(txt);
                    PreparedStatement ud = con.prepareStatement(update);
                    ud.setString(1, args[1]);
                    ud.executeUpdate();
                    con.close();
                    stmt.close();
                    ud.close();



                } catch (Exception x) {
                    SQLError.TextChannel(txt, x, "Just don't run this command.");
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
}
