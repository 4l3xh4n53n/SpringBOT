package Auto;

import Core.Database;
import Core.Embed;
import Core.MessageRemover;
import ErrorMessages.BadCode.SQLError;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReactionRoles {
    /*
    TODO WARNING
    For future reference if you change it to remove invalid settings make sure it removes the invalid role and it's associated emoji.
     */

    private static final String info = "This allows members to claim roles by reacting to a message.";
    private static final String set = "`makereactionmessage`";
    private static final String toggle = "`set ReactionRoles 1/0`";

    public static void CheckReaction(Message message, TextChannel textChannel, Guild guild, Member member){
        try{
            Connection con = Database.ReactionRoles();
            Statement stmt = con.createStatement();
            String SQL = "SELECT * FROM ReactionMessages WHERE MessageID='" + message + "'";
            ResultSet rs = stmt.executeQuery(SQL);

            if (!rs.next()) {

                rs.getString("role");

                guild.addRoleToMember(member, role)
            }
        } catch (Exception x){
            SQLError.TextChannel(textChannel, x, toggle);
        }
    }

    private static void AddToDatabase(){

    }

    private static Map<TextChannel, Message> creating = new HashMap<>();

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

    public static void creator(Member member, Message message, String content, TextChannel textChannel){

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

                                    try {

                                        // Reacts to the message sent to see if it's valid

                                        message.addReaction(emoji).queue();


                                        MessageEmbed em = reactionMessage.getEmbeds().get(0);
                                        EmbedBuilder newEmbed = new EmbedBuilder(em);

                                        newEmbed.addField("", emoji + " " + role.get(0).getAsMention(), false);
                                        reactionMessage.editMessageEmbeds(newEmbed.build()).queue(newMessage -> creating.put(textChannel, newMessage));
                                        reactionMessage.addReaction(emoji).queue();

                                        textChannel.sendMessage("Please repeat the first step for your next role.\n" +
                                                "If you finished type: `stop`.").queue();

                                    } catch (Exception ignored) {
                                        textChannel.sendMessage(":octagonal_sign: That emoji isn't valid.").queue();
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

                // Create Initial Embed

                EmbedBuilder em = Embed.em(user, textChannel);

                em.setTitle("Roles");

                textChannel.sendMessageEmbeds(em.build()).queue(createdMessage -> creating.put(textChannel, createdMessage));
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
