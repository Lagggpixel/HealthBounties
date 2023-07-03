package me.lagggpixel.healthbounties.listeners;

import me.lagggpixel.healthbounties.Main;
import me.lagggpixel.healthbounties.modules.bounty.BountyManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnListener implements Listener {

    public PlayerRespawnListener() {
        Main.getInstance().getServer().getPluginManager().registerEvents(this, Main.getInstance());
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        if (BountyManager.checkIsBanned(player)) return;

        BountyManager.updateHealth(player);
    }
}
