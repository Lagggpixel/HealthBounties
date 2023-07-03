package me.lagggpixel.healthbounties.listeners;

import me.lagggpixel.healthbounties.Main;
import me.lagggpixel.healthbounties.modules.menu.Menu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

public class InventoryClickListener implements Listener {

    public InventoryClickListener() {
        Main.getInstance().getServer().getPluginManager().registerEvents(this, Main.getInstance());
    }

    @EventHandler
    public void onMenuClick(InventoryClickEvent e){

        InventoryHolder holder = e.getInventory().getHolder();

        if (holder instanceof Menu menu) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null) {
                return;
            }
            menu.handleMenu(e);
        }

    }

}