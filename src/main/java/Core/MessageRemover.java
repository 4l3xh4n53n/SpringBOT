package Core;

import net.dv8tion.jda.api.entities.Message;
import java.util.concurrent.TimeUnit;

public class MessageRemover {

    public static void deleteAfter(Message msg) {
        msg.delete().queueAfter(10, TimeUnit.SECONDS); //todo make this not throw out an error somehow
    }
}
