package Misc;

import Core.Database;
import Core.SettingSetter;
import ErrorMessages.BadCode.SQLError;
import ErrorMessages.UserError.WrongCommandUsage;
import net.dv8tion.jda.api.entities.TextChannel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class SetPrefix {

    public static String example(){
        String example = "setPrefix <newPrefix>";
        return example;
    }

    public static String info(){
        String info = "This command changes your servers prefix (if you forget it the safe word is 'help I forgot my prefix')";
        return info;
    }

    public static void Set(TextChannel txt, String content, String guildID){
        String[] args = content.split("\\s+");

        if (args.length == 2){

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
            } catch (Exception x){
                if (!x.getMessage().equals("query does not return ResultSet")) {
                    SQLError.TextChannel(txt, x);
                }
            }
        } else {
            WrongCommandUsage.send(txt, example(), "Wrong amount of args", content);
        }


    }
}
