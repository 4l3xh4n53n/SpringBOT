package Auto;

import Core.Database;
import Core.MessageRemover;
import Core.Settings.SettingGetter;
import ErrorMessages.BadCode.SQLError;
import ErrorMessages.UserError.WrongCommandUsage;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Tickets {

    private static final String info = "This command allows people to open up a private chat to discuss an issue.";
    private static final String set = "`set TicketRole <@role>`";
    private static final String example = "ticket <issue>";
    private static final String toggle = "`set Ticket 1/0`";

    public static void delete(TextChannel textChannel){

        String channelID = textChannel.getId();

        if (checkDatabase(channelID, textChannel)){
            textChannel.delete().queue();
        } else {
            textChannel.sendMessage("This isn't a ticket.").queue(MessageRemover::deleteAfter);
        }
    }

    private static boolean checkDatabase(String channelID, TextChannel textChannel){
        try {
            Connection con = Database.Tickets();
            Statement stmt = con.createStatement();
            String SQL = "SELECT * FROM Tickets WHERE ChannelID='" + channelID + "'";
            ResultSet rs = stmt.executeQuery(SQL);

            if (rs.next()){

                String delete = "DELETE FROM Tickets WHERE ChannelID='" + channelID + "'";
                PreparedStatement ps = con.prepareStatement(delete);
                ps.executeUpdate();
                ps.close();
                rs.close();
                stmt.close();
                con.close();

                return true;
            }

            rs.close();
            stmt.close();
            con.close();

        } catch (Exception x){
            SQLError.TextChannel(textChannel, x, toggle);
        }

        return false;

    }

    private static void addToDatabase(String channelID, TextChannel textChannel){
        try {
            Connection con = Database.Tickets();
            String insert = "INSERT INTO Tickets(ChannelID) VALUES (?)";
            PreparedStatement ps = con.prepareStatement(insert);
            ps.setString(1, channelID);
            ps.execute();
            ps.close();
            con.close();
        } catch (Exception x){
            textChannel.sendMessage("Something went wrong\n" + x).queue(MessageRemover::deleteAfter);
        }
    }

    private static void setChannel(TextChannel channel, Member member, TextChannel textChannel, Guild guild ){

        // Checks for ticketRole

        Role ticketRole = null;

        try {
            ticketRole = guild.getRoleById(SettingGetter.ChannelFriendlyGet("TicketRole", textChannel).split(",")[0]);
        } catch (Exception ignored){}

        if (ticketRole != null){
            channel.createPermissionOverride(ticketRole).setAllow(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND, Permission.MESSAGE_ATTACH_FILES, Permission.MESSAGE_MANAGE).queue();
        }

        channel.createPermissionOverride(guild.getPublicRole()).setDeny(Permission.VIEW_CHANNEL).queue();
        channel.createPermissionOverride(member).setAllow(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND, Permission.MESSAGE_ATTACH_FILES).queue();

        addToDatabase(channel.getId(), textChannel);

    }

    public static void create(TextChannel textChannel, String content, User user, Guild guild, Member member){

        String[] args = content.split("\\s+");
        Member selfMember = guild.getSelfMember();

        if (SettingGetter.ChannelFriendlyGet("Tickets", textChannel).equals("1")){
            if (args.length > 1){
                if (selfMember.hasPermission(Permission.MANAGE_CHANNEL)){

                    // Checks if a ticket category is set up

                    Category ticketCategory = null;
                    try {
                        ticketCategory = guild.getCategoryById(SettingGetter.ChannelFriendlyGet("TicketCategory", textChannel));
                    } catch (Exception ignored){}

                    if (ticketCategory != null){

                        // Creates channel with category

                        ticketCategory.createTextChannel(content.replace(args[0], "")).queue(channel ->setChannel(channel, member, textChannel, guild));

                    } else {

                        // Creates channel without category

                        guild.createTextChannel(content.replace(args[0], "")).queue(channel -> setChannel(channel, member, textChannel, guild));

                    }

                }
            } else {
                WrongCommandUsage.send(textChannel, example, "You didn't have enough args.", user);
            }
        }
    }

    public static String getInfo() {
        return info;
    }

    public static String getSet() {
        return set;
    }

    public static String getExample() {
        return example;
    }

    public static String getToggle() {
        return toggle;
    }

}
