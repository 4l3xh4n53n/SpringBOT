package Games.Server;

import Core.Settings.SettingGetter;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.codehaus.plexus.util.StringUtils;

public class Counting {

    private static final String info = "Rolls a dice.";
    private static final String toggle = "`set GameCommands 1/0`";

    private static void deleteMessage(Guild guild, Message message, TextChannel textChannel){
        if (guild.getSelfMember().hasPermission(Permission.MESSAGE_MANAGE)) {
            message.delete().queue();
        } else {
            textChannel.sendMessage("Well... I would delete that because it is incorrect but I don't have permission to.").queue();
        }
    }

    public static void check(TextChannel textChannel, Message message, String content, Guild guild){

        String channelID = textChannel.getId();

        if (SettingGetter.ChannelFriendlyGet("Counting", textChannel).equals("1")){
            if (SettingGetter.ChannelFriendlyGet("CountingChannel", textChannel).equals(channelID)){

                textChannel.getHistory().retrievePast(2).queue(lastMessage ->{

                    String newMessage = lastMessage.get(1).getContentRaw();
                    if (StringUtils.isNumeric(newMessage)) {
                        if (lastMessage.size() > 1) {

                            int lastNumber = Integer.parseInt(newMessage);

                            if (!content.equals(String.valueOf(lastNumber + 1))) {
                                deleteMessage(guild, message, textChannel);
                            }

                        } else {
                            if (!content.equals("1")) {
                                deleteMessage(guild, message, textChannel);
                            }
                        }
                    }

                });

            }

        }

    }

    public static String getInfo(){
        return info;
    }

    public static String getToggle(){
        return toggle;
    }

}
