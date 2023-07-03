package me.lagggpixel.healthbounties.modules.menu;

import me.lagggpixel.healthbounties.Main;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public abstract class Menu implements InventoryHolder {

    protected PlayerMenuUtility playerMenuUtility;
    protected Inventory inventory;
    protected ItemStack FILLER_GLASS = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
    protected NamespacedKey itemTypeNameSpacedKey = new NamespacedKey(Main.getInstance(), "type");

    public Menu(PlayerMenuUtility playerMenuUtility) {
        this.playerMenuUtility = playerMenuUtility;

        ItemMeta itemMeta = FILLER_GLASS.getItemMeta();
        itemMeta.displayName(Main.getInstance().returnChatFormatting(" "));
        itemMeta.getPersistentDataContainer().set(itemTypeNameSpacedKey, PersistentDataType.STRING, "filler");
        FILLER_GLASS.setItemMeta(itemMeta);
    }

    public abstract TextComponent getMenuName();

    public abstract int getSlots();

    public abstract void handleMenu(InventoryClickEvent e);

    public abstract void setMenuItems();

    public void open() {

        inventory = Bukkit.createInventory(this, getSlots(), getMenuName());

        this.setMenuItems();

        playerMenuUtility.getOwner().openInventory(inventory);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}