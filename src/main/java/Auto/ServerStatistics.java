package Auto;

import Core.Main;
import Core.Settings.SettingGetter;
import Core.Settings.SettingSetter;
import Utility.IsNumeric;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.util.*;

public class ServerStatistics {

    private static final String info = "This module creates a counter for how many users are in your guild";
    private static final String set = "`set StatsTotal 1/0 \nset StatsBot 1/0\nset StatsMember 1/0`";
    private static final String toggle = "`set ServerStats 1/0`";

    public static void timer(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateCounters();
            }
        }, 0, 600000);
    }

    private static final HashMap<JDA, List<Guild>> guilds= new HashMap<>();

    private static int getCount(String type, Guild guild){
        int count = 0;

        switch (type) {
            case "statsTotal":
                count = guild.getMemberCount();
                break;
            case "statsBot":
                for (Member member : guild.loadMembers().get()) {
                    if (member.getUser().isBot()) {
                        count++;
                    }
                }
                break;
            case "statsMember":
                for (Member member : guild.loadMembers().get()) {
                    if (!member.getUser().isBot()) {
                        count++;
                    }
                }
                break;
        }

        return count;
    }

    private static void updateCounters(){
        String[] channelTypes = {"statsTotal", "statsBot", "statsMember"};

        for (JDA shard : Main.getShards()){
            if (!guilds.containsKey(shard)){
                guilds.put(shard, shard.getGuilds());
            }

                // Make a thing in here yeah innit

            for (Guild guild : shard.getGuilds()){
                if (SettingGetter.GuildFriendlyGet("serverStats", guild).equals("1")){
                    for (String type : channelTypes){
                        if (SettingGetter.GuildFriendlyGet(type, guild).equals("1")){

                            // now check if it exists

                            String channelID = SettingGetter.GuildFriendlyGet(type + "Channel", guild);
                            VoiceChannel voiceChannel = null;
                            String guildID = guild.getId();
                            String newName = "";

                            try {
                                voiceChannel = guild.getVoiceChannelById(channelID);
                            } catch (Exception ignored){}

                            if (voiceChannel != null){

                                // change name

                                String voiceChannelName = voiceChannel.getName();

                                String[] nameArgs = voiceChannelName.split("\\s+");
                                for (String arg : nameArgs){
                                    if (IsNumeric.check(arg)){
                                        newName = voiceChannelName.replace(arg, String.valueOf(getCount(type, guild)));
                                        break;
                                    }
                                }

                                if (!newName.equals("") && !newName.equals(voiceChannelName)){
                                    voiceChannel.getManager().setName(newName).queue();
                                }

                            } else {

                                // make new channel

                                guild.createVoiceChannel(type + ": " + getCount(type, guild)).queue(chan ->{
                                    SettingSetter.ExternalSet(guild, guildID, type + "channel", chan.getId(), toggle);
                                    chan.getManager().setPosition(0).queue();
                                });
                            }
                        }
                    }
                }
            }
        }
    }

    public static String getInfo(){
        return info;
    }

    public static String getSet(){
        return set;
    }

    public static String getToggle(){
        return toggle;
    }

}
