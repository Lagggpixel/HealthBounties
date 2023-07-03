package me.lagggpixel.healthbounties.modules.menu;

import me.lagggpixel.healthbounties.Main;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public abstract class PaginatedMenu extends Menu {

    protected NamespacedKey itemFunctionNameSpaceKey = new NamespacedKey(Main.getInstance(), "function");
    protected int page = 0;
    protected int maxItemsPerPage = 28;
    protected int index = 0;

    public PaginatedMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    public void addMenuBorder(){

        ItemStack forwardItem = new ItemStack(Material.DARK_OAK_BUTTON);
        ItemMeta forwardMeta = forwardItem.getItemMeta();
        forwardMeta.displayName(Main.getInstance().returnChatFormatting("&aRight"));
        forwardMeta.getPersistentDataContainer().set(itemTypeNameSpacedKey, PersistentDataType.STRING, "button");
        forwardMeta.getPersistentDataContainer().set(itemFunctionNameSpaceKey, PersistentDataType.STRING, "right");
        forwardItem.setItemMeta(forwardMeta);

        ItemStack backItem = new ItemStack(Material.DARK_OAK_BUTTON);
        ItemMeta backMeta = forwardItem.getItemMeta();
        backMeta.displayName(Main.getInstance().returnChatFormatting("&aLeft"));
        backMeta.getPersistentDataContainer().set(itemTypeNameSpacedKey, PersistentDataType.STRING, "button");
        backMeta.getPersistentDataContainer().set(itemFunctionNameSpaceKey, PersistentDataType.STRING, "left");
        backItem.setItemMeta(backMeta);

        ItemStack closeItem = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = forwardItem.getItemMeta();
        closeMeta.displayName(Main.getInstance().returnChatFormatting("&c&lClose"));
        closeMeta.getPersistentDataContainer().set(itemTypeNameSpacedKey, PersistentDataType.STRING, "button");
        closeMeta.getPersistentDataContainer().set(itemFunctionNameSpaceKey, PersistentDataType.STRING, "close");
        closeItem.setItemMeta(closeMeta);

        inventory.setItem(48, backItem);

        inventory.setItem(49, closeItem);

        inventory.setItem(50, forwardItem);

        for (int i = 0; i < 10; i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, super.FILLER_GLASS);
            }
        }

        inventory.setItem(17, super.FILLER_GLASS);
        inventory.setItem(18, super.FILLER_GLASS);
        inventory.setItem(26, super.FILLER_GLASS);
        inventory.setItem(27, super.FILLER_GLASS);
        inventory.setItem(35, super.FILLER_GLASS);
        inventory.setItem(36, super.FILLER_GLASS);

        for (int i = 44; i < 54; i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, super.FILLER_GLASS);
            }
        }
    }

    public int getMaxItemsPerPage() {
        return maxItemsPerPage;
    }
}
