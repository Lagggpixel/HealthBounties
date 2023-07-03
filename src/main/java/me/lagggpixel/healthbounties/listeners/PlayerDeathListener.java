package me.lagggpixel.healthbounties.listeners;

import me.lagggpixel.healthbounties.Main;
import me.lagggpixel.healthbounties.modules.bounty.BountyManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    public PlayerDeathListener() {
        Main.getInstance().getServer().getPluginManager().registerEvents(this, Main.getInstance());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {

        Player deathPlayer = event.getPlayer();
        Player playerKillerPlayer = deathPlayer.getKiller();

        if (playerKillerPlayer != null) {
            BountyManager.addKillBounty(playerKillerPlayer.getUniqueId());
            BountyManager.updateHealth(playerKillerPlayer);
            BountyManager.checkIsBanned(playerKillerPlayer);
        }

        if (!Main.getInstance().getEnvironmentDeathBountyDeduction() && playerKillerPlayer==null) return;

        BountyManager.removeDeathBounty(deathPlayer.getUniqueId());
        BountyManager.updateHealth(deathPlayer);
        BountyManager.checkIsBanned(deathPlayer);
    }
}
