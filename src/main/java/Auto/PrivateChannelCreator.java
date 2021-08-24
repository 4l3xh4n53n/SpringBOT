package Auto;

import Core.Database;
import Core.SettingGetter;
import ErrorMessages.BadCode.SQLError;
import ErrorMessages.UserError.ChannelNotSet;
import net.dv8tion.jda.api.entities.*;

import java.sql.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PrivateChannelCreator {

    public static String info = "When you join a certain voice channel you get put into your own";
    public static String set = "`set channel PrivateChannelCreator <VoiceChannelID>` + `set channel PrivateChannelCategory <CategoryID>`";

    public static void CheckDB(String guildID, Guild guild){

        try {
            Connection con = Database.CreatedChannels();
            DatabaseMetaData dbm = con.getMetaData();
            ResultSet tables = dbm.getTables(null, null, guildID, null);
            if (!tables.next()) {
                Statement stmt = con.createStatement();
                String sql = "CREATE TABLE '" + guildID + "' (channelID TEXT, users INTEGER)";
                stmt.executeUpdate(sql);
                stmt.close();
            }
            tables.close();
            con.close();

        } catch (Exception x){
            SQLError.GuildFriendly(guild, x);
        }

    }

    public static void AddToDB(String guildID, Guild guild, String channelID){

        try{
            Connection con = Database.CreatedChannels();
            String sql = "INSERT INTO '" + guildID + "' (channelID, users) VALUES (?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, channelID);
            ps.setInt(2, 1);
            ps.executeUpdate();
            con.close();
            ps.close();

        } catch (Exception x){
            SQLError.GuildFriendly(guild, x);
        }

    }

    public static void RemoveFromDB(String guildID, Guild guild, String channelID){

        try{
            Connection con = Database.CreatedChannels();
            String sql = "DELETE FROM '" + guildID + "' WHERE channelID = '" + channelID + "'";
            Statement ps = con.createStatement();
            ps.executeUpdate(sql);
            con.close();
            ps.close();
        } catch (Exception x){
            SQLError.GuildFriendly(guild, x);
        }

    }

    public static boolean CheckDatabase(String channelID, Guild guild, String guildID){

        boolean check = false;

        try{

            Connection con = Database.CreatedChannels();
            Statement stmt = con.createStatement();
            String SQL = "SELECT * FROM '" + guildID + "' WHERE channelID='" + channelID + "'";
            ResultSet rs = stmt.executeQuery(SQL);

            if (rs.next()){

                check = true;

            }

            con.close();
            stmt.close();
            rs.close();

        } catch (Exception x){

            SQLError.GuildFriendly(guild, x);

        }

        return check;

    }

    public static int Get(Guild guild, String guildID, String channelID){

        int users = 0;

        try{
            Connection con = Database.CreatedChannels();
            Statement stmt = con.createStatement();
            String SQL = "SELECT * FROM '" + guildID + "' WHERE channelID ='" + channelID + "'";
            ResultSet rs = stmt.executeQuery(SQL);
            users = rs.getInt("users");
            con.close();
            stmt.close();
            rs.close();
        } catch (Exception x){
            SQLError.GuildFriendly(guild, x);
        }
        return users;
    }



    public static void Delete(VoiceChannel channel, Guild guild, String guildID){
        String channelID = channel.getId();
        int members = channel.getMembers().size();

        if (CheckDatabase(channelID, guild, guildID)) {
            if (members == 0) {

                ExecutorService threadpool = Executors.newCachedThreadPool(); // todo I think I had an issue with it creating tables make sure ok innit borger
                threadpool.submit(() -> {
                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (Get(guild, guildID, channelID) == 0) {
                        channel.delete().queue();
                        RemoveFromDB(guildID, guild, channelID);
                    }
                });

            }

        }

    }

    public static void Increase(VoiceChannel channel, Guild guild){
        String channelID = channel.getId();
        String guildID = guild.getId();
        int size = channel.getMembers().size();

        if (CheckDatabase(channelID, guild, guildID)){
            try {
                Connection con = Database.CreatedChannels();
                String update = "UPDATE '" + guildID + "' SET users = ? WHERE channelID ='" + channelID + "'";
                PreparedStatement ud = con.prepareStatement(update);
                ud.setInt(1, size);
                ud.executeUpdate();
                ud.close();
                con.close();
            } catch (Exception x){
                SQLError.GuildFriendly(guild, x);
            }
        }
    }

    public static void Decrease(VoiceChannel channel, Guild guild){
        String channelID = channel.getId();
        String guildID = guild.getId();
        int size = channel.getMembers().size();

        if (CheckDatabase(channelID, guild, guildID)){
            try {
                Connection con = Database.CreatedChannels();
                String update = "UPDATE '" + guildID + "' SET users = ? WHERE channelID ='" + channelID + "'";
                PreparedStatement ud = con.prepareStatement(update);
                ud.setInt(1, size);
                ud.executeUpdate();
                ud.close();
                con.close();
            } catch (Exception x){
                SQLError.GuildFriendly(guild, x);
            }
        }
    }

    public static void Create(Guild guild, User user, VoiceChannel channel, Member member, String guildID){


        User guildOwner = guild.retrieveOwner().complete().getUser();

        String PrivateChannelCreator = SettingGetter.GuildFriendlySet("PrivateChannelCreator", guild);
        String PrivateChannelCategory = SettingGetter.GuildFriendlySet("PrivateChannelCategory", guild);
        VoiceChannel creator = null;
        Category category = null;
        try {
            creator = guild.getVoiceChannelById(PrivateChannelCreator);
            category = guild.getCategoryById(PrivateChannelCategory);
        } catch (Exception ignored) {
        }

        if (creator != null && category != null) {

            if (channel.getId().equals(PrivateChannelCreator)) {

                category.createVoiceChannel(user.getAsTag()).queue(chan -> {
                    guild.moveVoiceMember(member, chan).queue();
                    AddToDB(guildID, guild, chan.getId());
                });

            }

        } else {
            ChannelNotSet.GuildFriendly(set, guildOwner, guild);
        }

    }

}
