package net.terrocidepvp.givemehead.commands;

import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.meta.*;
import java.util.*;
import org.bukkit.inventory.*;
import org.bukkit.*;
import net.terrocidepvp.givemehead.*;

public class CmdGive
{
    static final String notOnline;
    static final String headName;
    static final String gaveHead;
    static final String givenHead;
    static final List<String> headLore;
    
    public static boolean onGive(final CommandSender sender, final String[] args, final Command cmd) {
        String arg1 = null;
        String arg2 = null;
        String arg3 = null;
        if (args.length == 3 && cmd.getName().equalsIgnoreCase("givehead")) {
            arg1 = args[0];
            arg2 = args[1];
            arg3 = args[2];
        }
        else {
            if (args.length != 4 || cmd.getName().equalsIgnoreCase("givehead")) {
                if (!cmd.getName().equalsIgnoreCase("givehead")) {
                    sender.sendMessage("/behead give [player] [headof] [killedby]");
                }
                return false;
            }
            arg1 = args[1];
            arg2 = args[2];
            arg3 = args[3];
        }
        final Player giveTo = Bukkit.getServer().getPlayer(arg1);
        if (giveTo != null) {
            final Player killedPerson = Bukkit.getPlayer(arg2);
            String killedPersonString = null;
            if (killedPerson == null) {
                killedPersonString = arg2;
            }
            else {
                killedPersonString = killedPerson.getName();
            }
            final Player killer = Bukkit.getPlayer(arg3);
            String killerString = null;
            if (killer == null) {
                killerString = arg3;
            }
            else {
                killerString = killer.getName();
            }
            final ItemStack head;
            final ItemMeta headMeta = (head = new ItemStack(Material.SKULL_ITEM, 1, (short)3)).getItemMeta();
            final ArrayList<String> lore = new ArrayList<String>();
            for (String str : CmdGive.headLore) {
                if (str == null) {
                    return false;
                }
                str = ChatColor.translateAlternateColorCodes('&', str);
                str = str.replace("%killed%", killedPersonString);
                str = str.replace("%killer%", killerString);
                lore.add(str);
            }
            headMeta.setDisplayName(CmdGive.headName.replace("%killed%", killedPersonString));
            headMeta.setLore((List)lore);
            ((SkullMeta)headMeta).setOwner(killedPersonString);
            head.setItemMeta(headMeta);
            final Inventory inventory = (Inventory)giveTo.getInventory();
            if (inventory.firstEmpty() != -1) {
                inventory.addItem(new ItemStack[] { head });
            }
            else {
                final Location loc = giveTo.getLocation();
                giveTo.getWorld().dropItem(loc, head);
            }
            sender.sendMessage(CmdGive.gaveHead.replace("%player%", giveTo.getName()).replace("%killed%", killedPersonString).replace("%killer%", killerString));
            giveTo.sendMessage(CmdGive.givenHead.replace("%player%", giveTo.getName()).replace("%killed%", killedPersonString).replace("%killer%", killerString));
        }
        else {
            sender.sendMessage(CmdGive.notOnline);
        }
        return true;
    }
    
    static {
        notOnline = ChatColor.translateAlternateColorCodes('&', GiveMeHead.plugin.getConfig().getString("plugin-messages.give-head-not-online", "&cYou can't give head to a player that isn't online!"));
        headName = ChatColor.translateAlternateColorCodes('&', GiveMeHead.plugin.getConfig().getString("beheading.head.name", "&e&l&o%killed%&6&o's Head"));
        gaveHead = ChatColor.translateAlternateColorCodes('&', GiveMeHead.plugin.getConfig().getString("plugin-messages.gave-head", "&aSuccessfully gave &2%player% &aa head belonging to &e&l&o%killed%&a, who was killed by &f%killer%&a!"));
        givenHead = ChatColor.translateAlternateColorCodes('&', GiveMeHead.plugin.getConfig().getString("plugin-messages.given-head", "&aYou were given a head belonging to &e&l&o%killed%&a, who was killed by &f%killer%&a!"));
        headLore = GiveMeHead.plugin.getConfig().getStringList("beheading.head.lore");
    }
}
