package gecko10000.unbreakabletoken;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Listeners implements Listener {

    private final UnbreakableToken plugin;

    public Listeners(UnbreakableToken plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(ignoreCancelled = false)
    public void onRightClickToken(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR) return;
        ItemStack item = event.getItem();
        if (item == null || item.isEmpty()) return;
        if (!item.getItemMeta().getPersistentDataContainer().has(plugin.tokenKey)) return;
        event.setCancelled(true);
        item.setAmount(item.getAmount() - 1);
        new UnbreakabilityGUI(plugin, event.getPlayer());
    }

}
