package Utility;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;

public class RoleFormatter {

    public static String format(String[] requiredRoles, Guild guild){
        StringBuilder req = new StringBuilder();

        for (String s : requiredRoles) {
            Role role = guild.getRoleById(s);
            req.append("@").append(role.getName()).append(" ");
        }

        return req.toString();
    }

}
