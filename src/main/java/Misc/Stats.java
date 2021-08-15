package Misc;

import Core.Main;
import Core.SettingGetter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;

import java.awt.*;
import java.io.FileReader;

public class Stats {

    public static void send(Guild guild, TextChannel txt, User user){
        MavenXpp3Reader reader = new MavenXpp3Reader();
        String version = null;
        try {
            Model model = reader.read(new FileReader("pom.xml"));
            version = model.getVersion();
        } catch (Exception ignored){}
        Runtime runtime = Runtime.getRuntime();
        JDA jda = guild.getJDA();
        String guildcount = String.valueOf(jda.getGuilds().size());
        int shardcount = Main.shardcount;
        int shardnumber = jda.getShardInfo().getShardId();
        long gatewayping = jda.getGatewayPing();
        long restping = jda.getRestPing().complete();
        long totalmem = runtime.totalMemory() / 1024 / 16;
        long freemem = runtime.freeMemory() / 1024 / 16;
        long usedmem = totalmem - freemem;
        String owner = jda.getUserById("456014662199410699").getAsTag();
        String osname = System.getProperty("os.name");
        String osversion = System.getProperty("os.version");
        String osbit = System.getProperty("os.arch");

        EmbedBuilder em = new EmbedBuilder();
        em.setColor(Color.decode(SettingGetter.GuildFriendlySet("GuildColour", guild)));
        em.setThumbnail("https://cdn.discordapp.com/avatars/673654725127831562/526af14d924487c4af7aabae5fa2850a.png?size=1024");
        em.setTitle("Stats:");
        em.addField("System:", "Ram: `" + usedmem + "mb/" + totalmem + "mb`\nOperating System: `" + osname + "`\nOperating System Version: `" + osversion + "`\nArchitecture: `" + osbit + "`", true);
        em.addField("Ping:","Gateway: `" + gatewayping + "`\nRest: `" + restping + "`", true);
        em.addBlankField(false);
        em.addField("Shard:", "`" + shardnumber + "/" + (shardcount - 1) + "`", true);
        em.addField("Owner:", "`" + owner + "`", true);
        em.addField("Guild Count:", "`" + guildcount + "`", true);
        em.setFooter("Spring, Version: " + version);
        txt.sendMessage(em.build()).queue();

    }

}
