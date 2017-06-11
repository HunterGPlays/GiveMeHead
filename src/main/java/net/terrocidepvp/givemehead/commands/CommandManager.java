package net.terrocidepvp.givemehead.commands;

import org.bukkit.command.*;
import net.terrocidepvp.givemehead.*;
import org.bukkit.*;
import org.bukkit.entity.*;

public class CommandManager implements CommandExecutor
{
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        final String NoPermission = ChatColor.translateAlternateColorCodes('&', GiveMeHead.plugin.getConfig().getString("plugin-messages.no-permission"));
        if (!cmd.getName().equalsIgnoreCase("behead") && !cmd.getName().equalsIgnoreCase("beheading") && !cmd.getName().equalsIgnoreCase("givemehead") && !cmd.getName().equalsIgnoreCase("head") && !cmd.getName().equalsIgnoreCase("gmh") && !cmd.getName().equalsIgnoreCase("givehead")) {
            return false;
        }
        if (args.length == 0 && !cmd.getName().equalsIgnoreCase("givehead")) {
            if (sender instanceof Player) {
                if (sender.hasPermission("givemehead.use")) {
                    CmdUse.onUse(sender);
                }
                else {
                    sender.sendMessage(NoPermission);
                }
            }
            else {
                sender.sendMessage("This command can only be executed as a player!");
            }
        }
        if ((args.length != 0 && args[0].equalsIgnoreCase("give")) || cmd.getName().equalsIgnoreCase("givehead")) {
            if (sender.hasPermission("givemehead.give")) {
                CmdGive.onGive(sender, args, cmd);
            }
            else {
                sender.sendMessage(NoPermission);
            }
        }
        return true;
    }
}
