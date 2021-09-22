package Misc.Set;

import Core.Database;
import Core.SettingSetter;
import ErrorMessages.BadCode.SQLError;
import ErrorMessages.UserError.WrongCommandUsage;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class SetColour {

    private static final String example = "setColour <#hexcode>";
    private static final String info = "Sets the colour that is used on the side of the embed";

    public static void Set(TextChannel txt, String content, String guildID, User user, Member member){

        if (member.getPermissions().contains(Permission.ADMINISTRATOR)) {

            String[] args = content.split("\\s+");
            int check = 0;

            if (args.length == 2) {

                try {
                    Color.decode(args[1]);
                    check = 1;
                } catch (Exception x) {
                    WrongCommandUsage.send(txt, example, "Not a valid hex code", user);
                }

                if (check == 1) {

                    try {
                        Connection con = Database.connect();
                        Statement stmt = con.createStatement();
                        String update = "UPDATE Settings SET GuildColour = ? WHERE GuildID='" + guildID + "'";
                        PreparedStatement ud = con.prepareStatement(update);
                        ud.setString(1, args[1]);
                        ud.executeUpdate();
                        con.close();
                        stmt.close();
                        ud.close();

                        SettingSetter.SettingChanged(txt);

                    } catch (Exception x) {
                        SQLError.TextChannel(txt, x, "Just don't run this command.");
                    }

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
