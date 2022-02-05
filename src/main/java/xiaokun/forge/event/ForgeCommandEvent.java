package xiaokun.forge.event;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * 锻造命令事件
 *
 * @author 小坤
 */
public class ForgeCommandEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    @Getter
    @Setter
    private List<String> commandList;
    @Getter
    private ItemStack map;
    @Getter
    private Player player;

    public ForgeCommandEvent(final Player player, final List<String> commandList, final ItemStack map) {
        this.player = player;
        this.commandList = commandList;
        this.map = map;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
