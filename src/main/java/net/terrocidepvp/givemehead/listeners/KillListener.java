package net.terrocidepvp.givemehead.listeners;

import org.bukkit.plugin.*;
import net.terrocidepvp.givemehead.configurations.*;
import net.terrocidepvp.givemehead.*;
import org.bukkit.entity.*;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.*;
import net.terrocidepvp.givemehead.hooks.*;
import net.terrocidepvp.givemehead.utils.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.*;
import org.bukkit.event.*;
import java.util.*;
import org.bukkit.enchantments.*;
import org.bukkit.event.player.*;
import org.bukkit.event.block.*;
import org.bukkit.block.*;

public class KillListener implements Listener
{
    private final Plugin plugin;
    final Random rng;
    final boolean debug;
    final boolean beheadingEnabled;
    final boolean headHuntingEnabled;
    final Integer headHuntingFlatAmount;
    final Integer headHuntingMinPercent;
    final int headHuntingMaxPercent;
    final String stoleMoneyFrom;
    final String wasStolenFrom;
    final int regularBaseChance;
    final int level1BaseChance;
    final int level2BaseChance;
    final int level3BaseChance;
    final int outOf;
    final String headName;
    final List<String> headLore;
    final String deathMessage;
    final String noHeadData;
    final String rightClickSkullNoKiller;
    final String rightClickSkull;
    final boolean changeDeathMessage;
    final boolean useBukkitBroadcaster;
    final boolean useDisplayName;
    final boolean headHuntingNegatives;
    final String headHuntingMode;
    public final List<String> blockedWorlds;
    Config c;
    
    public KillListener(final Plugin plugin) {
        this.rng = new Random();
        this.debug = GiveMeHead.plugin.getConfig().getBoolean("debug", false);
        this.beheadingEnabled = GiveMeHead.plugin.getConfig().getBoolean("beheading.enabled", false);
        this.headHuntingEnabled = GiveMeHead.plugin.getConfig().getBoolean("beheading.head-hunting.enabled", false);
        this.headHuntingFlatAmount = GiveMeHead.plugin.getConfig().getInt("beheading.head-hunting.flat.amount", 69);
        this.headHuntingMinPercent = GiveMeHead.plugin.getConfig().getInt("beheading.head-hunting.percentage.minimum-percentage", 10);
        this.headHuntingMaxPercent = GiveMeHead.plugin.getConfig().getInt("beheading.head-hunting.percentage.maximum-percentage", 30);
        this.stoleMoneyFrom = ChatColor.translateAlternateColorCodes('&', GiveMeHead.plugin.getConfig().getString("plugin-messages.stole-money-from", "&7You stole &a$%amount% &8(&f%percentage%%&8) &7from &b%killed%&7! You now have &2$%killerbalance%&7."));
        this.wasStolenFrom = ChatColor.translateAlternateColorCodes('&', GiveMeHead.plugin.getConfig().getString("plugin-messages.was-stolen-from", "&b%killer% &7stole &a$%amount% &8(&f%percentage%%&8) &7from you! You now have &2$%killedbalance%&7."));
        this.regularBaseChance = GiveMeHead.plugin.getConfig().getInt("beheading.probability.regular-base-chance", 5);
        this.level1BaseChance = GiveMeHead.plugin.getConfig().getInt("beheading.probability.looting-base-chance.level1", 7);
        this.level2BaseChance = GiveMeHead.plugin.getConfig().getInt("beheading.probability.looting-base-chance.level2", 10);
        this.level3BaseChance = GiveMeHead.plugin.getConfig().getInt("beheading.probability.looting-base-chance.level3", 15);
        this.outOf = GiveMeHead.plugin.getConfig().getInt("beheading.probability.out-of", 100);
        this.headName = ChatColor.translateAlternateColorCodes('&', GiveMeHead.plugin.getConfig().getString("beheading.head.name", "&e&l&o%killed%&6&o's Head"));
        this.headLore = (List<String>)GiveMeHead.plugin.getConfig().getStringList("beheading.head.lore");
        this.deathMessage = ChatColor.translateAlternateColorCodes('&', GiveMeHead.plugin.getConfig().getString("beheading.death-message.messsage", "&e%killed%&e was beheaded by %killer%&e! &8(&f%chance%&7/&f%outof%&8)"));
        this.noHeadData = ChatColor.translateAlternateColorCodes('&', GiveMeHead.plugin.getConfig().getString("plugin-messages.no-head-data", "&cWe couldn't find any data for this skull!"));
        this.rightClickSkullNoKiller = ChatColor.translateAlternateColorCodes('&', GiveMeHead.plugin.getConfig().getString("plugin-messages.right-click-skull-no-killer", "&aThis skull belongs to &2%killed%&a. We couldn't find any information on the killer."));
        this.rightClickSkull = ChatColor.translateAlternateColorCodes('&', GiveMeHead.plugin.getConfig().getString("plugin-messages.right-click-skull", "&aThis skull belongs to &2%killed%&a, who was beheaded by &2%killer%&a."));
        this.changeDeathMessage = GiveMeHead.plugin.getConfig().getBoolean("beheading.death-message.change-death-message", false);
        this.useBukkitBroadcaster = GiveMeHead.plugin.getConfig().getBoolean("beheading.death-message.use-bukkit-broadcaster", true);
        this.useDisplayName = GiveMeHead.plugin.getConfig().getBoolean("beheading.death-message.use-player-display-name", false);
        this.headHuntingNegatives = GiveMeHead.plugin.getConfig().getBoolean("beheading.head-hunting.flat.minus-money", false);
        this.headHuntingMode = GiveMeHead.plugin.getConfig().getString("beheading.head-hunting.mode", "percentage");
        this.blockedWorlds = (List<String>)GiveMeHead.plugin.getConfig().getStringList("beheading.blocked-worlds");
        this.c = new Config("locations", GiveMeHead.plugin);
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents((Listener)this, this.plugin);
    }
    
    public void d(final Player p, final String s) {
        if (this.debug && p.hasPermission("givemehead.debug")) {
            p.sendMessage(ChatColor.RED + "[DEBUG] " + ChatColor.GRAY + s);
        }
    }
    
    public void giveHead(final PlayerDeathEvent event, final Player killer, final Player killed, final Integer random, final Integer chance) {
        if (event == null) {
            return;
        }
        this.d(killer, "Running giveHead()");
        final ItemStack head;
        final ItemMeta headMeta = (head = new ItemStack(Material.SKULL_ITEM, 1, (short)3)).getItemMeta();
        final Location killedLocation = killed.getLocation();
        this.d(killer, "Replacing lore.");
        final ArrayList<String> lore = new ArrayList<String>();
        for (String str : this.headLore) {
            if (str == null) {
                return;
            }
            str = ChatColor.translateAlternateColorCodes('&', str);
            str = str.replace("%killed%", killed.getName());
            str = str.replace("%killer%", killer.getName());
            lore.add(str);
        }
        this.d(killer, "Setting other metadata.");
        ((SkullMeta)headMeta).setOwner(killed.getName());
        headMeta.setDisplayName(this.headName.replace("%killed%", killed.getName()));
        headMeta.setLore((List)lore);
        head.setItemMeta(headMeta);
        this.d(killer, "Dropping head.");
        killedLocation.getWorld().dropItem(killedLocation, head);
        this.d(killer, "Replacing death message.");
        String newDeathMessage = this.deathMessage;
        if (this.useDisplayName) {
            newDeathMessage = newDeathMessage.replace("%killed%", killed.getDisplayName());
            newDeathMessage = newDeathMessage.replace("%killer%", killer.getDisplayName());
        }
        else {
            newDeathMessage = newDeathMessage.replace("%killed%", killed.getName());
            newDeathMessage = newDeathMessage.replace("%killer%", killer.getName());
        }
        newDeathMessage = newDeathMessage.replace("%chance%", chance.toString());
        newDeathMessage = newDeathMessage.replace("%outof%", Integer.toString(this.outOf));
        newDeathMessage = newDeathMessage.replace("%randomnumbergenerated%", random.toString());
        this.d(killer, "Check for changeDeathMessage.");
        if (this.changeDeathMessage) {
            event.setDeathMessage(newDeathMessage);
        }
        this.d(killer, "Check for useBukkitBroadcaster.");
        if (this.useBukkitBroadcaster) {
            Bukkit.broadcastMessage(newDeathMessage);
        }
        this.d(killer, "headHuntingEnabled + Vault check.");
        if (this.headHuntingEnabled && GiveMeHead.plugin.getServer().getPluginManager().isPluginEnabled("Vault")) {
            this.d(killer, "Set HeadHunting variables.");
            final double killedBalance = VaultHook.economy.getBalance((OfflinePlayer)killed);
            final double killerBalance = VaultHook.economy.getBalance((OfflinePlayer)killer);
            double amountToTake = 0.0;
            double probability = 100.0;
            if (this.headHuntingMode.equals("percentage")) {
                this.d(killer, "Taking money using percentage mode.");
                final int result = this.rng.nextInt(this.headHuntingMaxPercent - this.headHuntingMinPercent) + this.headHuntingMinPercent;
                probability = result;
                amountToTake = Math.round(killedBalance * (probability / 100.0) * 100.0) / 100.0;
            }
            else if (this.headHuntingMode.equals("flat")) {
                this.d(killer, "Taking money using flat mode.");
                if (!this.headHuntingNegatives && 0.0 > killedBalance) {
                    amountToTake = 0.0;
                }
                else {
                    amountToTake = this.headHuntingFlatAmount;
                }
            }
            else {
                killed.sendMessage(ChatColor.RED + "Incorrect configuration for the head hunting mode! Please inform an admin!");
                GiveMeHead.plugin.getLogger().severe("Incorrect configuration for the head hunting mode! Please inform an admin!");
            }
            String stoleMoneyFromNew = this.stoleMoneyFrom;
            String wasStolenFromNew = this.wasStolenFrom;
            this.d(killer, "Take and deposit money.");
            VaultHook.economy.withdrawPlayer((OfflinePlayer)killed, amountToTake);
            VaultHook.economy.depositPlayer((OfflinePlayer)killer, amountToTake);
            this.d(killer, "Replace and send killed message.");
            wasStolenFromNew = wasStolenFromNew.replace("%killed%", killed.getName());
            wasStolenFromNew = wasStolenFromNew.replace("%killer%", killer.getName());
            wasStolenFromNew = wasStolenFromNew.replace("%amount%", NumberUtil.formatAsCurrency(amountToTake));
            wasStolenFromNew = wasStolenFromNew.replace("%percentage%", Double.toString(probability));
            wasStolenFromNew = wasStolenFromNew.replace("%killedbalance%", NumberUtil.formatAsCurrency(killedBalance - amountToTake));
            wasStolenFromNew = wasStolenFromNew.replace("%killerbalance%", NumberUtil.formatAsCurrency(killerBalance + amountToTake));
            killed.sendMessage(wasStolenFromNew);
            this.d(killer, "Replace and send killer message.");
            stoleMoneyFromNew = stoleMoneyFromNew.replace("%killed%", killed.getName());
            stoleMoneyFromNew = stoleMoneyFromNew.replace("%killer%", killer.getName());
            stoleMoneyFromNew = stoleMoneyFromNew.replace("%amount%", NumberUtil.formatAsCurrency(amountToTake));
            stoleMoneyFromNew = stoleMoneyFromNew.replace("%percentage%", Double.toString(probability));
            stoleMoneyFromNew = stoleMoneyFromNew.replace("%killedbalance%", NumberUtil.formatAsCurrency(killedBalance - amountToTake));
            stoleMoneyFromNew = stoleMoneyFromNew.replace("%killerbalance%", NumberUtil.formatAsCurrency(killerBalance + amountToTake));
            killer.sendMessage(stoleMoneyFromNew);
        }
    }
    
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onHeadBreak(final BlockBreakEvent event) {
        if (event.isCancelled()) {
            return;
        }
        final Player player = event.getPlayer();
        final Block block = event.getBlock();
        this.d(player, "Head broken, checking if material is skull.");
        if (block.getType() != Material.SKULL) {
            return;
        }
        final Skull skull = (Skull)block.getState();
        this.d(player, "Checking if skull belongs to player.");
        if (skull.getSkullType() != SkullType.PLAYER) {
            return;
        }
        final Skull skullblock = (Skull)block.getState();
        final String skullowner = skullblock.getOwner();
        final Location location = block.getLocation();
        final int x = block.getX();
        final int y = block.getY();
        final int z = block.getZ();
        this.d(player, "Checking if skull data exists in locations.yml.");
        final String rawloc = this.c.getConfig().getString("locations." + x + ";" + y + ";" + z);
        if (rawloc == null) {
            player.sendMessage(this.noHeadData);
            return;
        }
        final String[] loc = rawloc.split(";");
        final ItemStack head;
        final ItemMeta headMeta = (head = new ItemStack(Material.SKULL_ITEM, 1, (short)3)).getItemMeta();
        this.d(player, "Replacing itemstack lore with one from config.");
        final ArrayList<String> lore = new ArrayList<String>();
        for (String str : this.headLore) {
            if (str == null) {
                return;
            }
            str = ChatColor.translateAlternateColorCodes('&', str);
            str = str.replace("%killed%", loc[1]);
            str = str.replace("%killer%", loc[0]);
            lore.add(str);
        }
        this.d(player, "Replacing display name with one from config + setting lore.");
        headMeta.setDisplayName(this.headName.replace("%killed%", loc[1]));
        headMeta.setLore((List)lore);
        this.d(player, "Setting skull owner.");
        ((SkullMeta)headMeta).setOwner(skullowner);
        head.setItemMeta(headMeta);
        this.d(player, "Dropping item at killed location.");
        if (player.getGameMode() != GameMode.CREATIVE) {
            player.getWorld().dropItem(location, head);
        }
        this.d(player, "Storing the location in file.");
        this.c.getConfig().set("locations." + x + ";" + y + ";" + z, (Object)null);
        this.d(player, "Saving custom config.");
        this.c.save();
        this.d(player, "Setting block as air and cancelling event.");
        block.setType(Material.AIR);
        event.setCancelled(true);
    }
    
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onHeadPlace(final BlockPlaceEvent event) {
        if (event.isCancelled()) {
            return;
        }
        final ItemStack item = event.getItemInHand();
        final Block block = event.getBlock();
        final Player player = event.getPlayer();
        if (item == null) {
            return;
        }
        this.d(player, "Checking if item is skull. SPAM ALERT!");
        if (item.getType() != Material.SKULL_ITEM) {
            return;
        }
        this.d(player, "Checking if skull has durability of 3 (head).");
        if (item.getDurability() != 3) {
            return;
        }
        this.d(player, "Storing block and item information.");
        final int x = block.getX();
        final int y = block.getY();
        final int z = block.getZ();
        final ItemMeta meta = item.getItemMeta();
        String displayname = meta.getDisplayName();
        final List<String> lore = (List<String>)meta.getLore();
        if (displayname == null || lore == null) {
            return;
        }
        this.d(player, "Extracting killed from display name.");
        final String[] splitname = this.headName.split("%killed%");
        String killedfromname = null;
        for (final String str : splitname) {
            if (str == null) {
                return;
            }
            displayname = displayname.replaceFirst(str, "");
            killedfromname = displayname.replaceFirst(str, "");
        }
        this.d(player, "Extracting killer from lore.");
        final ArrayList<String> splitlore = new ArrayList<String>();
        String killerfromlore = null;
        for (String str : this.headLore) {
            if (str == null) {
                return;
            }
            str = ChatColor.translateAlternateColorCodes('&', str);
            Collections.addAll(splitlore, str.split("%killer%"));
        }
        this.d(player, "Replacing lore to get killer.");
        for (String str : lore) {
            if (str == null) {
                return;
            }
            for (final String str2 : splitlore) {
                if (str2 == null) {
                    return;
                }
                str = str.replaceFirst(str2, "");
                killerfromlore = str.replaceFirst(str2, "");
            }
        }
        this.d(player, "Storing in data file.");
        this.c.set("locations." + x + ";" + y + ";" + z, killerfromlore + ";" + killedfromname);
        this.d(player, "Saving configuration.");
        this.c.save();
    }
    
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerKill(final PlayerDeathEvent event) {
        if (event == null) {
            return;
        }
        if (!this.beheadingEnabled) {
            return;
        }
        final Player killed;
        final Player killer;
        if ((killer = (killed = event.getEntity()).getKiller()) == null) {
            return;
        }
        this.d(killer, "Checking if killed is in blocked world.");
        for (final String s : this.blockedWorlds) {
            if (killed.getWorld().getName().equals(s)) {
                return;
            }
        }
        this.d(killer, "Checking if killed has exempt perm.");
        if (killed.hasPermission("givemehead.exempt")) {
            return;
        }
        this.d(killer, "Getting item in hand as itemstack.");
        ItemStack heldItem;
        if (GiveMeHead.plugin.serverVersion[0] == 1 && GiveMeHead.plugin.serverVersion[1] >= 9) {
            heldItem = killer.getInventory().getItemInMainHand();
        }
        else {
            heldItem = killer.getInventory().getItemInHand();
        }
        this.d(killer, "Running random number generator.");
        final Integer random = this.rng.nextInt(this.outOf);
        this.d(killer, "Checking if the player got lucky and checking for no looting.");
        if (this.regularBaseChance >= random && !heldItem.containsEnchantment(Enchantment.LOOT_BONUS_MOBS)) {
            this.giveHead(event, killer, killed, random, this.regularBaseChance);
            return;
        }
        this.d(killer, "Checking if player has looting.");
        if (heldItem.containsEnchantment(Enchantment.LOOT_BONUS_MOBS)) {
            this.d(killer, "Checking if player has looting 3 or above.");
            if (heldItem.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS) >= 3 && this.level3BaseChance >= random) {
                this.giveHead(event, killer, killed, random, this.level3BaseChance);
                return;
            }
            this.d(killer, "Checking if player has looting 2.");
            if (heldItem.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS) >= 2 && this.level2BaseChance >= random) {
                this.giveHead(event, killer, killed, random, this.level2BaseChance);
                return;
            }
            this.d(killer, "Checking if player has looting 1.");
            if (heldItem.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS) >= 1 && this.level1BaseChance >= random) {
                this.giveHead(event, killer, killed, random, this.level1BaseChance);
            }
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onRightClick(final PlayerInteractEvent event) {
        if (event.isCancelled() || event == null) {
            return;
        }
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            final Player player = event.getPlayer();
            this.d(player, "Checking if player has givemehead perm.");
            this.d(player, "This message will SPAM as it overrides every right click, regardless of block.");
            if (!player.hasPermission("givemehead.rightclick")) {
                return;
            }
            this.d(player, "Checking if block is skull.");
            final BlockState block = event.getClickedBlock().getState();
            if (block instanceof Skull) {
                final Skull skull = (Skull)block;
                final int x = block.getX();
                final int y = block.getY();
                final int z = block.getZ();
                final String owner = skull.getOwner();
                this.d(player, "Getting raw location from config.");
                final String rawloc = this.c.getConfig().getString("locations." + x + ";" + y + ";" + z);
                if (rawloc == null) {
                    player.sendMessage(this.rightClickSkullNoKiller.replace("%killed%", owner));
                    return;
                }
                final String[] loc = rawloc.split(";");
                String newRightClickSkull = this.rightClickSkull;
                this.d(player, "Replacing variables for the right click message.");
                newRightClickSkull = newRightClickSkull.replace("%killer%", loc[0]);
                newRightClickSkull = newRightClickSkull.replace("%killed%", loc[1]);
                player.sendMessage(newRightClickSkull);
            }
        }
    }
}
