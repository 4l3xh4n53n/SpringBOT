package Utility;

import net.dv8tion.jda.api.entities.Role;

import java.util.ArrayList;
import java.util.List;

public class GetRoleIDs {

    public static ArrayList<String> get(List<Role> userRoles){
        ArrayList<String> RoleIDs = new ArrayList<>();

        for (Role userrole : userRoles) {
            RoleIDs.add(userrole.getId());
        }

        return RoleIDs;

    }

}
