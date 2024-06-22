package gecko10000.unbreakabletoken;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import redempt.redlib.inventorygui.InventoryGUI;
import redempt.redlib.inventorygui.ItemButton;
import redempt.redlib.itemutils.ItemUtils;

public class UnbreakabilityGUI implements InventoryHolder {

    private static final int SIZE = 27;
    private static final int ITEM_SLOT = 13;

    private final UnbreakableToken plugin;
    private final Player player;
    private final InventoryGUI gui;
    private boolean actionCompleted;

    public UnbreakabilityGUI(UnbreakableToken plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        this.gui = createInventory();
        this.gui.open(player);
    }

    private InventoryGUI createInventory() {
        Inventory inventory = Bukkit.createInventory(this, SIZE, plugin.miniMessage.deserialize(plugin.getConfig().getString("gui_name")));
        InventoryGUI gui = new InventoryGUI(inventory);
        gui.setReturnsItems(true);
        ItemStack filler = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        filler.editMeta(m -> m.displayName(Component.empty()));
        gui.fill(0, SIZE, filler);
        gui.getInventory().setItem(ITEM_SLOT, null);
        gui.openSlot(ITEM_SLOT);
        gui.addButton(21, cancelButton());
        gui.addButton(23, confirmButton());
        gui.setOnDestroy(() -> {
            if (!actionCompleted) {
                player.sendMessage(plugin.miniMessage.deserialize("<red>Unbreakable token cancelled."));
                this.refund();
            }
        });
        return gui;
    }

    private void refund() {
        actionCompleted = true;
        ItemUtils.give(player, plugin.getItem());
    }

    private ItemButton cancelButton() {
        ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        item.editMeta(m -> m.displayName(plugin.miniMessage.deserialize("<red>Cancel").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)));
        return ItemButton.create(item, e -> {
            player.sendMessage(plugin.miniMessage.deserialize("<red>Unbreakable token cancelled."));
            this.refund();
            player.closeInventory();
        });
    }

    private void tryConfirm() {
        ItemStack inItemSlot = gui.getInventory().getItem(ITEM_SLOT);
        if (inItemSlot == null || inItemSlot.isEmpty()) {
            player.sendMessage(plugin.miniMessage.deserialize("<red>Unbreakable token cancelled."));
            refund();
            return;
        }
        if (inItemSlot.getType().getMaxDurability() == 0) {
            player.sendMessage(plugin.miniMessage.deserialize("<red>This item doesn't have durability."));
            refund();
            return;
        }
        ItemMeta slotMeta = inItemSlot.getItemMeta();
        if (slotMeta.isUnbreakable()) {
            player.sendMessage(plugin.miniMessage.deserialize("<red>This item is already unbreakable."));
            refund();
            return;
        }
        gui.setReturnsItems(false);
        // Send before setting unbreakable so the
        // message doesn't have the item as unbreakable
        player.sendMessage(
                plugin.miniMessage.deserialize(
                        "<green>Unbreakable token used on <item> successfully.",
                        Placeholder.component("item", inItemSlot.displayName())
                )
        );
        slotMeta.setUnbreakable(true);
        inItemSlot.setItemMeta(slotMeta);
        actionCompleted = true;
        ItemUtils.give(player, inItemSlot);
    }

    private ItemButton confirmButton() {
        ItemStack item = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        item.editMeta(m -> m.displayName(plugin.miniMessage.deserialize("<green>Confirm").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)));
        return ItemButton.create(item, e -> {
            tryConfirm();
            player.closeInventory();
        });
    }

    @Override
    public @NotNull Inventory getInventory() {
        return gui.getInventory();
    }
}
