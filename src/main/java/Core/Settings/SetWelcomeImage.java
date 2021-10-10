package Core.Settings;

import Core.Database;
import Core.Settings.SettingGetter;
import Core.Settings.SettingSetter;
import ErrorMessages.BadCode.SQLError;
import ErrorMessages.UserError.NoPerms;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class SetWelcomeImage {

    private static final String example = "`setWelcomeImage <link>` <-- (not adding a link removes the image)";
    private static final String info = "The image displayed when a member joins.";

    public static void Check(Member member, String content, TextChannel txt, String guildID){

        User user = member.getUser();
        String[] args = content.split("\\s+");

        if (member.getPermissions().contains(Permission.ADMINISTRATOR)){

            String url = args[1];

            try {
                Connection con = Database.connect();
                String update = "UPDATE Settings SET GuildWelcomeImage = ? WHERE GuildID = '" + guildID + "'";
                SettingSetter.SettingChanged(txt);
                SettingGetter.UpdateSetting(guildID, "GuildWelcomeImage", args[1]);
                PreparedStatement ud = con.prepareStatement(update);
                ud.setString(1, url);
                ud.executeUpdate();
                ud.close();

            } catch (Exception x) {
                SQLError.TextChannel(txt, x, "Just don't run this command.");
            }

        } else {
            NoPerms.Send("setWelcomeImage", "ADMINISTRATOR", txt, user);
        }
    }

    public static String getExample() {
        return example;
    }

    public static String getInfo(){
        return info;
    }

}
