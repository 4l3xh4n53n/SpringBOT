package commands.mod;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;

import java.util.ArrayList;

public class RoleChecker {

    public static int CheckRoles(String[] roles, Guild guild){
        int check = 0;
        ArrayList<Role> Roles = new ArrayList<>();

        for (String role : roles) {
            try {
                 Roles.add(guild.getRoleById(role));
            } catch (Exception ignored){}
        }
        if (roles.length > 0) {
            check = 1;
        }

        return check;
    }

}
