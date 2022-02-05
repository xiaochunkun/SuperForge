package xiaokun.forge.util;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ForgeListInventory extends PageableInventory{
    public ForgeListInventory(int size, String title, List<ItemStack> items, int page, int itemsPerPage) {
        super(size, title, items, page, itemsPerPage);
    }
}
