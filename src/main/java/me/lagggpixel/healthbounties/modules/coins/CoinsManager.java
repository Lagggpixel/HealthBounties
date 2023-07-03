package me.lagggpixel.healthbounties.modules.coins;

import me.lagggpixel.healthbounties.Main;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class CoinsManager {

    public static final NamespacedKey coinRecipeNameSpacedKey = new NamespacedKey(Main.getInstance(), "recipe_coins");
    public static final NamespacedKey coinItemNameSpacedKey = new NamespacedKey(Main.getInstance(), "coinItem");

    public static boolean isCoinItem(ItemStack itemStack) {
        if (itemStack == null) return false;
        if (itemStack.getItemMeta() == null) return false;
        if (itemStack.getItemMeta().getPersistentDataContainer().get(coinItemNameSpacedKey, PersistentDataType.STRING) == null) return false;
        if (itemStack.getItemMeta().getPersistentDataContainer().get(coinItemNameSpacedKey, PersistentDataType.STRING).equals("coinItem")) return true;
        return false;
    }
}
