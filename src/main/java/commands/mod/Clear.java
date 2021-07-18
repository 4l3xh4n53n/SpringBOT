/**
 Copyright © 2021 4legs

 SpringBOT for discord

 This is my bot feel free to make your own custom version of it, either to add features or make it better or whatever else you feel like doing with it.
 Do not just download it and call it yours though because that's kinda a bad move and this will not be tolerated.
 **/

package commands.mod;

import Core.MessageRemover;
import Core.SettingGetter;
import ErrorMessages.UserError.NoPerms;
import ErrorMessages.UserError.RolesNotSet;
import ErrorMessages.UserError.WrongCommandUsage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import org.apache.commons.collections4.CollectionUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Clear {

    public static String example(){
        String example = "clear <amount>";
        return example;
    }

    public static String info(){
        String info = "This automatically removes a specified amount of messages from the specified channel.";
        return info;
    }

    public static String set(){
        String set = "`set roles ClearRoles <@role(s)>`";
        return set;
    }

    public static void Execute(TextChannel txt, int amount){

        List<Message> messagestobecleared = txt.getHistory().retrievePast(amount).complete();
        txt.deleteMessages(messagestobecleared).complete();

        EmbedBuilder em = new EmbedBuilder();
        em.setColor(Color.decode(SettingGetter.ChannelFriendlySet("GuildColour", txt)));
        em.setTitle("Removed " + (amount -1) + " messages");
        txt.sendMessage(em.build()).queue(msg -> {
            MessageRemover.deleteAfter(msg);
        });

    }

    public static void check(TextChannel txtchan, String request, User user, Guild guild){
        String[] args = request.split("\\s+");

        if (args.length == 2){

            int check = 0;
            int amount;
            Role botrole = guild.getBotRole();
            String[] roles = SettingGetter.ChannelFriendlySet("ClearRoles", txtchan).split(",");
            String req = "";
            List<Role> userroles = guild.getMember(user).getRoles();
            List<String> usersRoles = new ArrayList<>();

            // Makes sure specified amount is a number if not defaults to 1 message

            try {
                amount = Integer.parseInt(args[1].replaceAll("[^0-9]",""));
                amount = amount + 1;
            } catch (Exception x){
                System.out.println("sdlfkjsdflkjjsdfkl");
                amount = 2;
            }

            // Makes sure there are real roles setup

            try {
                for (int i = 0; roles.length > i; i++){
                    guild.getRoleById(roles[i]);
                }
                check = 1;
            } catch (Exception x){
                RolesNotSet.ChannelFriendly(txtchan, "ClearRoles", set());
            }

            // Makes sure they have the roles

            if (check == 1){
                for (int i = 0; userroles.size() > i; i++){
                    usersRoles.add(userroles.get(i).getId());
                }
                if (CollectionUtils.containsAny(Arrays.asList(roles), usersRoles)){
                    if (botrole.hasPermission(Permission.MESSAGE_MANAGE) || botrole.hasPermission(Permission.ADMINISTRATOR)) {
                        Execute(txtchan, amount);
                    } else {
                        NoPerms.Bot("Manage Messages", txtchan);
                    }
                } else {
                    for (int i = 0; roles.length > i; i++){
                        Role role = guild.getRoleById(roles[i]);
                        req = req + "@"+ role.getName() + " ";
                    }
                    NoPerms.Send("clear", req, txtchan);
                }

            }
        } else {
            WrongCommandUsage.send(txtchan, example(), "Wrong amount of args", request);
        }
    }
}
