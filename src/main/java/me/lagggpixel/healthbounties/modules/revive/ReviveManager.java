package me.lagggpixel.healthbounties.modules.revive;

import me.lagggpixel.healthbounties.Main;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class ReviveManager {
    public static final NamespacedKey reviveRecipeNameSpacedKey = new NamespacedKey(Main.getInstance(), "recipe_revive");
    public static final NamespacedKey reviveItemNameSpacedKey = new NamespacedKey(Main.getInstance(), "reviveItem");

    public static boolean isReviveItem(ItemStack itemStack) {
        if (itemStack == null) return false;
        if (itemStack.getItemMeta() == null) return false;
        if (itemStack.getItemMeta().getPersistentDataContainer().get(reviveItemNameSpacedKey, PersistentDataType.STRING) == null) return false;
        if (itemStack.getItemMeta().getPersistentDataContainer().get(reviveItemNameSpacedKey, PersistentDataType.STRING).equals("reviveItem")) return true;
        return false;
    }
}
