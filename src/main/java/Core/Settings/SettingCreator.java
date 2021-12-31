package Core.Settings;

import Core.Database;
import ErrorMessages.BadCode.GuildJoinedError;
import net.dv8tion.jda.api.entities.Guild;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class SettingCreator{

    // This creates default settings when the bot joins a new guild or when a feature that uses settings is used.

    public static void CreateSettings(Guild g,String guildID, Connection con){

        try {
            PreparedStatement ps;
            String SQL = "INSERT INTO settings(" +
                    "guildid, " +
                    "prefix, " +
                    "guildcolour," +
                    "clearroles, " +
                    "kickroles, " +
                    "banroles, " +
                    "warnroles, " +
                    "muteroles," +
                    "modcommands, " +
                    "kicklog, " +
                    "banlog, " +
                    "warnlog, " +
                    "logmodactions, " +
                    "coins," +
                    "sendcoins," +
                    "guildwelcome," +
                    "guildwelcomemessage," +
                    "guildwelcomeimage," +
                    "guildwelcomechannel," +
                    "autorole," +
                    "autorolerole," +
                    "pollrole," +
                    "invitelogging," +
                    "invitelog," +
                    "privatechannel," +
                    "privatechannelcreator," +
                    "privatechannelcategory," +
                    "serverstats," +
                    "statsTotalChannel," +
                    "statsBotChannel," +
                    "statsMemberChannel," +
                    "statsTotal," +
                    "statsBot," +
                    "statsMember," +
                    "chatfilter," +
                    "filter," +
                    "gamecommands," +
                    "reactionroles," +
                    "tickets," +
                    "ticketcategory," +
                    "ticketrole," +
                    "counting," +
                    "countingchannel," +
                    "mutedrole" +
                    ")VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = con.prepareStatement(SQL);
            ps.setString(1, guildID);
            ps.setString(2, "!"); //Prefix
            ps.setString(3, "#000000"); //GuildColour
            ps.setString(4, "");//ClearRoles
            ps.setString(5, ""); //KickRoles
            ps.setString(6, "");//BanRoles
            ps.setString(7,"");//WarnRoles
            ps.setString(8, "");//MuteRoles
            ps.setString(9, "1"); //ModCommands
            ps.setString(10,  "");//KickLog
            ps.setString(11, "");//BanLog
            ps.setString(12, "");//WarnLog
            ps.setString(13, "0");//LogModActions
            ps.setString(14, "1");//Coins
            ps.setString(15, "1");//SendCoins
            ps.setString(16, "0");//GuildWelcome
            ps.setString(17, "");//GuildWelcomeMessage
            ps.setString(18, "");//GuildWelcomeImage
            ps.setString(19, "");//GuildWelcomeChannel
            ps.setString(20, "0");//AutoRole
            ps.setString(21, "");//AutoRoleRole
            ps.setString(22, "");//PollRole
            ps.setString(23, "0");//InviteLogging
            ps.setString(24, "");//InviteLog
            ps.setString(25, "0");//PrivateChannel
            ps.setString(26, "");//PrivateChannelCreator
            ps.setString(27, "");//PrivateChannelCategory
            ps.setString(28, "0");//ServerStats
            ps.setString(29, "1");//StatsTotal
            ps.setString(30, "1");//StatsBot
            ps.setString(31, "1");//StatsMember
            ps.setString(32, "");//StatsChannel
            ps.setString(33, "0");//ChatFilter
            ps.setString(34, "");//Filter
            ps.setString(35, "0");//GameCommands
            ps.setString(36, "0");//ReactionRoles
            ps.setString(37, "0");//Tickets
            ps.setString(38, "");//TicketCategory
            ps.setString(39, "");//TicketRole
            ps.setString(40, "0");//Counting
            ps.setString(41, "");//CountingChannel
            ps.setString(42, ""); //MutedRole
            ps.executeUpdate();
            ps.close();

        } catch (Exception x){
            GuildJoinedError.SendInMain(x, g);
        }

    }

    public static void check(Guild g){

        String guildID = g.getId();

        try {
            Connection con = Database.connect();
            Statement stmt = con.createStatement();
            String SQL = "SELECT * FROM Settings WHERE GuildID='" + guildID + "'";
            ResultSet rs = stmt.executeQuery(SQL);

            if (!rs.next()) {

                CreateSettings(g, guildID, con);

            }
            con.close();
            stmt.close();
            rs.close();

        } catch (Exception x){
            GuildJoinedError.SendInMain(x, g);
        }
    }

}
