package xiaokun.forge.event;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/**
 * 锻造物品事件
 *
 * @author 小坤
 */
public class ForgeItemEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    @Getter
    @Setter
    private ItemStack itemStack;
    @Getter
    private ItemStack map;

    @Getter
    private Player player;

    public ForgeItemEvent(final Player player, final ItemStack itemStack, final ItemStack map) {
        this.player = player;
        this.itemStack = itemStack;
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
