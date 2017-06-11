package net.terrocidepvp.givemehead.hooks;

import net.milkbowl.vault.permission.*;
import net.milkbowl.vault.economy.*;
import net.milkbowl.vault.chat.*;
import net.terrocidepvp.givemehead.*;
import org.bukkit.plugin.*;

public class VaultHook
{
    public static Permission permission;
    public static Economy economy;
    public static Chat chat;
    
    public static boolean setupChat() {
        final RegisteredServiceProvider<Chat> chatProvider = (RegisteredServiceProvider<Chat>)GiveMeHead.plugin.getServer().getServicesManager().getRegistration((Class)Chat.class);
        if (chatProvider != null) {
            VaultHook.chat = (Chat)chatProvider.getProvider();
        }
        return VaultHook.chat != null;
    }
    
    public static boolean setupEconomy() {
        final RegisteredServiceProvider<Economy> economyProvider = (RegisteredServiceProvider<Economy>)GiveMeHead.plugin.getServer().getServicesManager().getRegistration((Class)Economy.class);
        if (economyProvider != null) {
            VaultHook.economy = (Economy)economyProvider.getProvider();
        }
        return VaultHook.economy != null;
    }
    
    public static boolean setupPermissions() {
        final RegisteredServiceProvider<Permission> permissionProvider = (RegisteredServiceProvider<Permission>)GiveMeHead.plugin.getServer().getServicesManager().getRegistration((Class)Permission.class);
        if (permissionProvider != null) {
            VaultHook.permission = (Permission)permissionProvider.getProvider();
        }
        return VaultHook.permission != null;
    }
    
    static {
        VaultHook.permission = null;
        VaultHook.economy = null;
        VaultHook.chat = null;
    }
}
