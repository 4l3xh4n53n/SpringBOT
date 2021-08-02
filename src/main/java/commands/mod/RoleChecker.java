package commands.mod;

import net.dv8tion.jda.api.entities.Guild;

public class RoleChecker {

    public static int CheckRoles(String[] roles, Guild guild){
        int check = 0;
        try {
            for (int i = 0; roles.length > i; i++){
                guild.getRoleById(roles[i]);
            }
            check = 1;
        } catch (Exception ignored){

        }
        return check;
    }

}
