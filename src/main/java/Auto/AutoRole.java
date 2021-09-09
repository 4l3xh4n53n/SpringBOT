package Auto;

import Core.SettingGetter;
import ErrorMessages.UserError.NoPerms;
import ErrorMessages.UserError.RolesNotSet;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

import java.util.Collections;
import java.util.List;

public class AutoRole {

    private static final String info = "This command automatically gives people roles on arrival.";
    private static final String set = "`set roles AutoRoleRole <@role(s)>`";
    private static final String toggle = "`set module AutoRole 1/0`";

    public static void give(Guild guild, Member member) {

        if (SettingGetter.GuildFriendlySet("AutoRole", guild).equals("1")) {

            User guildOwner = guild.retrieveOwner().complete().getUser();
            Member botMember = guild.getSelfMember();
            String[] roleID = null;

            try {

                roleID = SettingGetter.GuildFriendlySet("AutoRoleRole", guild).split(",");

            } catch (Exception ignored) {}

            List<Role> roles = new java.util.ArrayList<>(Collections.emptyList());

            if (roleID != null) {
                for (String s : roleID) {

                    Role role = guild.getRoleById(s);

                    if (role != null) {

                        roles.add(role);

                    }
                }

                if (botMember.hasPermission(Permission.MANAGE_ROLES)) {
                    if (roles.size() > 0) {
                        for (Role role : roles) {
                            if (botMember.getRoles().get(0).getPosition() > role.getPosition()) {
                                guild.addRoleToMember(member, role).complete();
                            }
                        }
                    } else {
                        RolesNotSet.GuildFriendly(guildOwner, "AutoRoleRole", set, guild, toggle);
                    }
                } else {
                    NoPerms.GuildBot("MANAGE_ROLES", guildOwner, guild);
                }
            } else {
                RolesNotSet.GuildFriendly(guildOwner, "AutoRole", set, guild, toggle);
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
