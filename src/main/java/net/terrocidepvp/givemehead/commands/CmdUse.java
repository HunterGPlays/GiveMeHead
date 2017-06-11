package net.terrocidepvp.givemehead.commands;

import org.bukkit.command.*;
import net.terrocidepvp.givemehead.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import java.util.*;

public class CmdUse
{
    public static boolean onUse(final CommandSender sender) {
        final String notHoldingSkull = ChatColor.translateAlternateColorCodes('&', GiveMeHead.plugin.getConfig().getString("plugin-messages.not-holding-skull", "&cYou must hold a skull in your hand!"));
        String headCommand = ChatColor.translateAlternateColorCodes('&', GiveMeHead.plugin.getConfig().getString("plugin-messages.head-command", "&aYou're holding the skull of &2%killed%&a, who was beheaded by &2%killer%&a."));
        final String headName = ChatColor.translateAlternateColorCodes('&', GiveMeHead.plugin.getConfig().getString("beheading.head.name", "%killed%'s Head"));
        final String rightClickSkullNoKiller = ChatColor.translateAlternateColorCodes('&', GiveMeHead.plugin.getConfig().getString("plugin-messages.right-click-skull-no-killer", "&aThis skull belongs to &2%killed%&a. We couldn't find any information on the killer."));
        final List<String> headLore = (List<String>)GiveMeHead.plugin.getConfig().getStringList("beheading.head.lore");
        final Player player = Bukkit.getPlayer(sender.getName());
        ItemStack heldItem;
        if (GiveMeHead.plugin.serverVersion[0] == 1 && GiveMeHead.plugin.serverVersion[1] >= 9) {
            heldItem = player.getInventory().getItemInMainHand();
        }
        else {
            heldItem = player.getInventory().getItemInHand();
        }
        if (heldItem.getType() != Material.SKULL) {
            if (heldItem.getDurability() != 3) {
                sender.sendMessage(notHoldingSkull);
                return true;
            }
            final ItemMeta meta = heldItem.getItemMeta();
            String displayname = meta.getDisplayName();
            final List<String> lore = (List<String>)meta.getLore();
            if (displayname == null || lore == null) {
                final SkullMeta skull = (SkullMeta)heldItem.getItemMeta();
                final String owner = skull.getOwner();
                player.sendMessage(rightClickSkullNoKiller.replace("%killed%", owner));
                return true;
            }
            final String[] splitname = headName.split("%killed%");
            String killedfromname = null;
            for (final String str : splitname) {
                displayname = displayname.replace(str, "");
                killedfromname = displayname.replace(str, "");
            }
            final ArrayList<String> splitlore = new ArrayList<String>();
            String killerfromlore = null;
            for (String str : headLore) {
                str = ChatColor.translateAlternateColorCodes('&', str);
                Collections.addAll(splitlore, str.split("%killer%"));
            }
            for (String str : lore) {
                for (final String str2 : splitlore) {
                    str = str.replace(str2, "");
                    killerfromlore = str.replace(str2, "");
                }
            }
            headCommand = headCommand.replace("%killed%", killedfromname);
            headCommand = headCommand.replace("%killer%", killerfromlore);
            sender.sendMessage(headCommand);
        }
        return true;
    }
}
