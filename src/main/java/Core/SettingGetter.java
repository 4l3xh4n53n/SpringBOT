package Core;

import ErrorMessages.BadCode.GuildJoinedError;
import ErrorMessages.BadCode.SQLError;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class SettingGetter {

    public static String ChannelFriendlySet(String setting, TextChannel e){
        String set = null;
        String guild = e.getGuild().getId();

        SettingCreator.check(e.getGuild()); // Checks to make sure that the settings exist

        try {
            Connection con = Database.connect();
            Statement stmt = con.createStatement();
            String get = "SELECT " + setting + " FROM Settings WHERE GuildID= '" + guild + "'";
            ResultSet rs = stmt.executeQuery(get);
            set = rs.getString(setting);
            con.close();
            rs.close();
            stmt.close();

        } catch(Exception x) {
            SQLError.TextChannel(e, x, "Can't turn this off sorry.");
        }

        return set;
    }

    public static String GuildFriendlySet(String setting, Guild g){
        String set = null;
        String guild = g.getId();

        SettingCreator.check(g); // Checks to make sure that the settings exist

        try {
            Connection con = Database.connect();
            Statement stmt = con.createStatement();
            String get = "SELECT " + setting + " FROM Settings WHERE GuildID= '" + guild + "'";
            ResultSet rs = stmt.executeQuery(get);
            set = rs.getString(setting);
            con.close();
            rs.close();
            stmt.close();

        } catch(Exception x) {
            GuildJoinedError.DMOwner(x, g);
        }

        return set;
    }
}
