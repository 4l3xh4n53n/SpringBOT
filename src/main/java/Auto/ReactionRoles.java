package Auto;

import Core.Database;
import Core.Embed;
import Core.MessageRemover;
import Core.Settings.SettingGetter;
import ErrorMessages.BadCode.SQLError;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReactionRoles {
    /*
    TODO WARNING
    For future reference if you change it to remove invalid settings make sure it removes the invalid role and it's associated emoji.
    Also make the thread that deletes the setup as well
     */

    private static final String info = "This allows members to claim roles by reacting to a message.";
    private static final String set = "`makereactionmessage`";
    private static final String toggle = "`set ReactionRoles 1/0`";

    private static void cantChangeRole(Member member){
        member.getUser().openPrivateChannel().queue(channel -> {
            channel.sendMessage("I don't have permission to grant you that role.\n" +
                    "Please tell the server owner").queue();
        });
    }

    public static void removeRole(String messageID, TextChannel textChannel, Guild guild, Member member, String reaction){

        if (SettingGetter.ChannelFriendlyGet("ReactionRoles", textChannel).equals("1")) {

            Member selfMember = guild.getSelfMember();
            if (selfMember.hasPermission(Permission.MANAGE_ROLES)) {
                try {
                    Connection con = Database.ReactionRoles();
                    Statement stmt = con.createStatement();
                    String SQL = "SELECT * FROM ReactionMessages WHERE MessageID='" + messageID + "'";
                    ResultSet rs = stmt.executeQuery(SQL);

                    if (rs.next()) {

                        String[] emojis = rs.getString("Emojis").split(",");
                        String[] roles = rs.getString("RoleIDs").split(",");
                        int itterator = -1;

                        for (String emoji : emojis) {

                            itterator++;
                            if (reaction.equals(emoji)) {

                                try {
                                    Role role = guild.getRoleById(roles[itterator]);
                                    if (selfMember.getRoles().get(0).getPosition() > role.getPosition()) {
                                        guild.removeRoleFromMember(member, role).queue();
                                    } else {
                                        cantChangeRole(member);
                                    }
                                } catch (Exception ignored) {
                                }

                            }

                        }

                    }
                } catch (Exception x) {
                    SQLError.TextChannel(textChannel, x, toggle);
                }
            } else {
                cantChangeRole(member);
            }
        }
    }

    public static void addRole(String messageID, TextChannel textChannel, Guild guild, Member member, String reaction){

        if (SettingGetter.ChannelFriendlyGet("ReactionRoles", textChannel).equals("1")) {

            Member selfMember = guild.getSelfMember();
            if (selfMember.hasPermission(Permission.MANAGE_ROLES)) {
                try {
                    Connection con = Database.ReactionRoles();
                    Statement stmt = con.createStatement();
                    String SQL = "SELECT * FROM ReactionMessages WHERE MessageID='" + messageID + "'";
                    ResultSet rs = stmt.executeQuery(SQL);

                    if (rs.next()) {

                        String[] emojis = rs.getString("Emojis").split(",");
                        String[] roles = rs.getString("RoleIDs").split(",");
                        int itterator = -1;

                        for (String emoji : emojis) {

                            itterator++;
                            if (reaction.equals(emoji)) {

                                try {
                                    Role role = guild.getRoleById(roles[itterator]);
                                    if (selfMember.getRoles().get(0).getPosition() > role.getPosition()) {
                                        guild.addRoleToMember(member, role).queue();
                                    } else {
                                        cantChangeRole(member);
                                    }
                                } catch (Exception ignored) {
                                }

                            }

                        }

                    }
                } catch (Exception x) {
                    SQLError.TextChannel(textChannel, x, toggle);
                }
            } else {
                cantChangeRole(member);
            }
        }
    }

    private static void AddToDatabase(Message reactionMessage, String emoji, String roleID, TextChannel textChannel){
        try {

            String messageID = reactionMessage.getId();
            Connection con = Database.ReactionRoles();
            Statement stmt = con.createStatement();
            String SQL = "SELECT * FROM ReactionMessages WHERE MessageID='" + messageID + "'";
            ResultSet rs = stmt.executeQuery(SQL);

            if (rs.next()) {

                // Update the values

                String Emojis = rs.getString("Emojis");
                String RoleIDs = rs.getString("RoleIDs");

                Emojis = Emojis + "," + emoji;
                RoleIDs = RoleIDs + "," + roleID;

                String update1 = "UPDATE ReactionMessages SET Emojis = ?, RoleIDs = ? WHERE MessageID ='" + messageID + "'";
                PreparedStatement ud = con.prepareStatement(update1);
                ud.setString(1, Emojis);
                ud.setString(2, RoleIDs);
                ud.executeUpdate();
                ud.close();

            } else {

                // Create new values

                String insert = "INSERT INTO ReactionMessages(MessageID, Emojis, RoleIDs) VALUES(?,?,?)";
                PreparedStatement ps = con.prepareStatement(insert);
                ps.setString(1, messageID);
                ps.setString(2, emoji);
                ps.setString(3, roleID);
                ps.executeUpdate();
                ps.close();

            }

            rs.close();
            con.close();
            stmt.close();

        }catch(Exception x){
            SQLError.TextChannel(textChannel, x, toggle);
        }
    }

    private static Map<TextChannel, Message> creating = new HashMap<>();

    private static void timeLimiter(TextChannel textChannel){
        ExecutorService threadpool = Executors.newCachedThreadPool();
        threadpool.submit(() -> {

            try {
                Thread.sleep(600000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            creating.remove(textChannel);

        });
    }

    private static List<String> getReactedEmojis(Message reactionMessage, TextChannel textChannel){
        List<String> reactedEmojis = new ArrayList<>();

        Message message = textChannel.retrieveMessageById(reactionMessage.getId()).complete();

        for (MessageReaction reaction : message.getReactions()){
            reactedEmojis.add(reaction.getReactionEmote().getEmoji());
        }

        return reactedEmojis;
    }

    private static List<String> getMentionedRoles(Message reactionMessage){
        List<String> mentioned = new ArrayList<>();

        for (MessageEmbed.Field field : reactionMessage.getEmbeds().get(0).getFields()){
            mentioned.add(field.getValue().split("\\s+")[1]);
        }

        return mentioned;
    }

    public static void creator(Member member, Message message, String content, TextChannel textChannel, Guild guild){

        Message reactionMessage = creating.get(textChannel);
        String[] args = content.split("\\s+");

        if (content.equals("stop")){

            creating.remove(textChannel);

        } else {

            if (reactionMessage != null) {
                if (member.hasPermission(Permission.ADMINISTRATOR)) {
                    if (args.length == 2) {

                        List<Role> role = message.getMentionedRoles();
                        String emoji = args[1];

                        if (role.size() > 0) {
                            if (!getMentionedRoles(reactionMessage).contains(args[0])) {
                                if (!getReactedEmojis(reactionMessage, textChannel).contains(emoji)) {
                                    if (role.get(0).getPosition() < guild.getSelfMember().getRoles().get(0).getPosition() && role.get(0).isManaged() && role.get(0).isPublicRole() ) {

                                        try {

                                            // Reacts to the message sent to see if it's valid

                                            message.addReaction(emoji).queue();

                                            // Adds to the database and embed

                                            MessageEmbed em = reactionMessage.getEmbeds().get(0);
                                            EmbedBuilder newEmbed = new EmbedBuilder(em);

                                            newEmbed.addField("", emoji + " " + role.get(0).getAsMention(), false);
                                            reactionMessage.editMessageEmbeds(newEmbed.build()).queue(newMessage -> creating.put(textChannel, newMessage));
                                            reactionMessage.addReaction(emoji).queue();

                                            textChannel.sendMessage("Please repeat the first step for your next role.\n" +
                                                    "If you finished type: `stop`.").queue();

                                            AddToDatabase(reactionMessage, emoji, role.get(0).getId(), textChannel);

                                        } catch (Exception ignored) {
                                            textChannel.sendMessage(":octagonal_sign: That emoji isn't valid.").queue();
                                        }
                                    } else {
                                        textChannel.sendMessage(":octagonal_sign: I don't have the ability to grant anyone this role.").queue();
                                    }
                                } else {
                                    textChannel.sendMessage(":octagonal_sign:  You already have this emoji.").queue();
                                }
                            } else {
                                textChannel.sendMessage(":octagonal_sign:  You already have this role.").queue();
                            }
                        } else {
                            textChannel.sendMessage(":octagonal_sign:  You haven't supplied any roles").queue();
                        }
                    } else {
                        textChannel.sendMessage(":octagonal_sign:  You haven't supplied enough args").queue();
                    }

                }

            }

        }

    }

    public static void initializer(Member member, User user, TextChannel textChannel){
        if (member.hasPermission(Permission.ADMINISTRATOR)){

            Message reactionMessage = creating.get(textChannel);

            if (reactionMessage != null){

                textChannel.sendMessage("You are already setting something up in this channel.").queue(MessageRemover::deleteAfter);

            } else {

                // Start the creation process

                EmbedBuilder em = Embed.em(user, textChannel);

                em.setTitle("Roles");

                textChannel.sendMessageEmbeds(em.build()).queue(createdMessage ->{
                    creating.put(textChannel, createdMessage);
                    timeLimiter(textChannel);
                });
                textChannel.sendMessage(":warning:**YOU HAVE TEN MINUTES TO COMPLETE THE SETUP**\n" +
                        "To stop the setup process type `finish`\n\n" +
                        "State a role and the associated emoji in this format:\n" +
                        "@role :emoji:").queue();

            }

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
