package Misc.Set;

import Core.Database;
import Core.SettingSetter;
import ErrorMessages.BadCode.SQLError;
import ErrorMessages.UserError.WrongCommandUsage;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class SetColour {

    private static final String example = "setColour <#hexcode>";
    private static final String info = "Sets the colour that is used on the side of the embed";

    public static void Set(TextChannel txt, String content, String guildID, User user){
        String[] args = content.split("\\s+");
        int check = 0;

        if (args.length == 2){

            String setTo = args[1];

            try{
                Color.decode(args[1]);
                check = 1;
            } catch (Exception x){
                WrongCommandUsage.send(txt, example, "Not a valid hex code", user);
            }

            if (check == 1){
                try {
                    Connection con = Database.connect();
                    Statement stmt = con.createStatement();
                    String update = "UPDATE Settings SET GuildColour ='" + setTo + "' WHERE GuildID='" + guildID + "'";
                    SettingSetter.SettingChanged(txt);
                    ResultSet ud = stmt.executeQuery(update);
                    ud.updateString(args[1], args[2]);
                    ud.updateRow();
                    con.close();
                    stmt.close();
                    ud.close();
                } catch (Exception x){
                    if (!x.getMessage().equals("query does not return ResultSet")) {
                        SQLError.TextChannel(txt, x, "Just don't run this command.");
                    }
                }
            }
        } else {
            WrongCommandUsage.send(txt, example, "Wrong amount of args", user);
        }

    }

    public static String getExample() {
        return example;
    }

    public static String getInfo() {
        return info;
    }
}
