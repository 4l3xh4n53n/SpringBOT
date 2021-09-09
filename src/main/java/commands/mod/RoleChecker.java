package commands.mod;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;

import java.util.ArrayList;

public class RoleChecker {

    public static int CheckRoles(String[] roles, Guild guild){

        int check = 0;
        ArrayList<Role> Roles = new ArrayList<>();

        for (String s : roles) {

            Role role = null;
            try {
                role = guild.getRoleById(s.replace(",", ""));
            } catch (Exception ignored){}
            if (role != null) {
                Roles.add(role);
            }

        }

        if (Roles.size() > 0){
            check = 1;
        }

        return check;
    }

}
