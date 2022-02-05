package xiaokun.forge.util;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ForgeHistoryInventory extends PageableInventory{
    public ForgeHistoryInventory(int size, String title, List<ItemStack> items, int page, int itemsPerPage) {
        super(size, title, items, page, itemsPerPage);
    }
}
