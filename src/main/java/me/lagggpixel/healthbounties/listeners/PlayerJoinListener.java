package me.lagggpixel.healthbounties.listeners;

import com.destroystokyo.paper.profile.PlayerProfile;
import me.lagggpixel.healthbounties.Main;
import me.lagggpixel.healthbounties.modules.bounty.BountyManager;
import me.lagggpixel.healthbounties.modules.coins.CoinsManager;
import me.lagggpixel.healthbounties.modules.lang.Lang;
import me.lagggpixel.healthbounties.modules.revive.ReviveManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PlayerJoinListener implements Listener {

    public PlayerJoinListener() {
        Main.getInstance().getServer().getPluginManager().registerEvents(this, Main.getInstance());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        BountyManager.setDefaultBountyIfAbsent(player.getUniqueId());
        BountyManager.updateHealth(player);

        BountyManager.checkIsBanned(player);

        if (!player.hasDiscoveredRecipe(CoinsManager.coinRecipeNameSpacedKey)) {
            player.discoverRecipe(CoinsManager.coinRecipeNameSpacedKey);
        }
        if (!player.hasDiscoveredRecipe(ReviveManager.reviveRecipeNameSpacedKey)) {
            player.discoverRecipe(ReviveManager.reviveRecipeNameSpacedKey);
        }
    }

    @EventHandler
    public void onPlayerPreJoin(AsyncPlayerPreLoginEvent event) {
        @NotNull PlayerProfile player = event.getPlayerProfile();
        UUID uuid = player.getId();
        if (uuid == null) return;
        if (BountyManager.isPlayerBanned(uuid)) {
            event.kickMessage(Lang.BANNED.toTextComponent());
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);
        }
    }

}
