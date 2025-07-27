package gecko10000.unbreakabletoken;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.strokkur.commands.annotations.Command;
import net.strokkur.commands.annotations.Executes;
import net.strokkur.commands.annotations.Permission;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

@Command("unbreakabletoken")
@Permission("unbreakabletoken.command")
public class CommandHandler {

    private final UnbreakableToken plugin = JavaPlugin.getPlugin(UnbreakableToken.class);

    public void register() {
        plugin.getLifecycleManager()
                .registerEventHandler(
                        LifecycleEvents.COMMANDS.newHandler(
                                event -> CommandHandlerBrigadier.register(event.registrar())
                        )
                );
    }

    private void give(CommandSender sender, Player target) {
        target.give(List.of(plugin.getItem()), true);
        sender.sendMessage(
                plugin.miniMessage.deserialize(
                        "<green>Gave <player> an unbreakable token.",
                        Placeholder.component("player", target.name())
                )
        );
    }

    @Executes("give")
    @Permission("unbreakabletoken.give")
    public void giveSelf(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(plugin.miniMessage.deserialize("<red>Specify a player."));
            return;
        }
        give(sender, player);
    }

    @Executes("give")
    @Permission("unbreakabletoken.give.other")
    public void giveOther(CommandSender sender, List<Player> players) {
        players.forEach(p -> give(sender, p));
    }

    @Executes("reload")
    @Permission("unbreakabletoken.reload")
    public void reload(CommandSender sender) {
        plugin.reloadConfig();
        sender.sendMessage(plugin.miniMessage.deserialize("<green>Configs reloaded."));
    }

}
