package Auto;

import Core.Database;
import Core.Embed;
import Core.SettingGetter;
import ErrorMessages.BadCode.SQLError;
import ErrorMessages.UserError.ChannelNotSet;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class InviteLogger {

    private static final String info = "This module tally's the amount of invites a user has and which invite a user joined through.";
    private static final String set = "`set InviteLog <channelID>`";
    private static final String toggle = "`set InviteLogger 1/0`";

    private static void checkGuild(String guildID, Guild guild){

        try {
            Connection con = Database.invites();
            DatabaseMetaData dbm = con.getMetaData();
            ResultSet tables = dbm.getTables(null, null, guildID, null);

            if (!tables.next()) {

                Statement stmt = con.createStatement();
                String sql = "CREATE TABLE '" + guildID + "' (inviteURL TEXT, inviterID TEXT, uses INTEGER)";
                stmt.executeUpdate(sql);
                stmt.close();

            }

            tables.close();
            con.close();

        } catch (Exception x){
            SQLError.GuildFriendly(guild, x, toggle);
        }

    }

    private static void update(Guild guild, String guildID){

        List<Invite> invites = guild.retrieveInvites().complete();

        try {

            Connection con = Database.invites();
            Statement stmt = con.createStatement();

            for (Invite invite : invites) {

                String get = "SELECT uses FROM '" + guildID + "' WHERE inviteURL = '" + invite.getUrl() + "'";
                ResultSet rs = stmt.executeQuery(get);
                String id;

                if (!rs.next()){
                    if (invite.getInviter() == null){
                        id = "0";
                    } else {
                        id = invite.getInviter().getId();
                    }

                    AddInviteToDatabase(invite.getUrl(),
                            id,
                            guild,
                            guildID);

                }

                rs.close();

            }

        } catch (Exception x){
            SQLError.GuildFriendly(guild, x, toggle);
        }

    }

    public static void Send(Guild guild, User joined){

        if (SettingGetter.GuildFriendlySet("InviteLogging", guild).equals("1")) {

            String guildID = guild.getId();
            User guildOwner = guild.retrieveOwner().complete().getUser();
            TextChannel log = null;

            checkGuild(guildID, guild);
            Invite invite = GetUsedInvite(guildID, guild);
            update(guild, guildID);

            String user = null;

            if (invite != null) {

                User inviter = invite.getInviter();

                if (inviter != null){
                    user = inviter.getAsTag();
                }

                String uses = String.valueOf(invite.getUses());
                String url = invite.getUrl();

                try {

                    log = guild.getTextChannelById(SettingGetter.GuildFriendlySet("InviteLog", guild));

                } catch (Exception x) {
                    ChannelNotSet.GuildFriendly(set, guildOwner, guild, toggle);
                }

                if (log != null) {

                    if (user != null) {

                        String tag = joined.getAsTag();
                        String avatar = joined.getAvatarUrl();

                        EmbedBuilder em = Embed.em(joined, log);
                        em.setAuthor(tag, null, avatar);
                        em.addField("Invited by: ", user, true);
                        em.addField("Using url: ", url, true);
                        em.addField("", "Url uses: " + uses, false);
                        log.sendMessageEmbeds(em.build()).queue();

                    } else {
                        EmbedBuilder em = Embed.em(joined, log);
                        em.addField("", "I'm not too sure who used this invite, perhaps it was temporary. " + uses, false);
                        log.sendMessageEmbeds(em.build()).queue();
                    }
                }
            }
        }
    }

    private static void IncreaseInvite(Connection con, Invite usedInvite, Guild guild, String guildID){

        try {

            String update = "UPDATE '" + guildID + "' SET uses = ? WHERE inviteURL ='" + usedInvite.getUrl() + "'";
            PreparedStatement ud = con.prepareStatement(update);
            ud.setInt(1, usedInvite.getUses());
            ud.executeUpdate();
            ud.close();

        } catch (Exception x){
            SQLError.GuildFriendly(guild, x, toggle);
        }

    }

    private static Invite GetUsedInvite(String guildID, Guild guild){

        Invite usedInvite = null;
        List<Invite> invites = guild.retrieveInvites().complete();
        int uses  = 0;

        Connection con = Database.invites();
        Statement stmt = null;

        try {

            stmt = con.createStatement();

        } catch (Exception x){
            SQLError.GuildFriendly(guild, x, toggle);
        }

        for (Invite invite : invites){

            String get = "SELECT uses FROM '" + guildID + "' WHERE inviteURL = '" + invite.getUrl() + "'";

            try {

                assert stmt != null;
                ResultSet rs = stmt.executeQuery(get);
                if (!rs.isClosed()) {
                    uses = rs.getInt(1);
                    rs.close();
                }
                stmt.close();

            } catch (Exception x){
                SQLError.GuildFriendly(guild, x, toggle);
            }

            if (uses < invite.getUses()){
                usedInvite = invite;
                IncreaseInvite(con, usedInvite, guild, guildID);
            }

        }

        try{
            con.close();
        } catch (Exception x){
            SQLError.GuildFriendly(guild, x, toggle);
        }

        return usedInvite;
    }

    public static void AddInviteToDatabase(String inviteURL, String inviterID, Guild guild, String guildID){
        checkGuild(guildID, guild);

        try{

            Connection con = Database.invites();
            String sql = "INSERT INTO '" + guildID + "' (inviteURL, inviterID, uses) VALUES (?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, inviteURL);
            ps.setString(2, inviterID);
            ps.setInt(3,0);
            ps.executeUpdate();
            con.close();
            ps.close();

        } catch (Exception x){
            SQLError.GuildFriendly(guild, x, toggle);
        }
    }

    public static void RemoveInviteFromDatabase(String inviteURL, Guild guild, String guildID){
        checkGuild(guildID, guild);
        try{
            Connection con = Database.invites();
            String sql = "DELETE FROM '" + guildID + "' WHERE inviteURL = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, inviteURL);
            ps.executeUpdate();
            con.close();
            ps.close();
        } catch (Exception x){
            SQLError.GuildFriendly(guild, x, toggle);
        }

    }

    public static String getInfo() {
        return info;
    }

    public static String getSet() {
        return set;
    }

    public static String getToggle() {
        return toggle;
    }
}
