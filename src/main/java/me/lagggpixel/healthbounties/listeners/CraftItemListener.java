package me.lagggpixel.healthbounties.listeners;

import me.lagggpixel.healthbounties.Main;
import me.lagggpixel.healthbounties.modules.bounty.BountyManager;
import me.lagggpixel.healthbounties.modules.lang.Lang;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

public class CraftItemListener implements Listener {

    public CraftItemListener() {
        Main.getInstance().getServer().getPluginManager().registerEvents(this, Main.getInstance());
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void CraftItemEvent(CraftItemEvent event) {
        Player player = (Player) event.getWhoClicked();
        int bounties = BountyManager.getBountyOrDefault(player.getUniqueId());

        if (bounties <= 300) {
            player.sendMessage(Lang.NOT_ENOUGH_BOUNTIES_TO_CRAFT.toTextComponentWithPrefix(null));
            event.setCancelled(true);
            return;
        }

        BountyManager.removeBounty(player.getUniqueId(), 300);
        BountyManager.updateHealth(player);
    }
}
