package xiaokun.forge.api;

import org.bukkit.plugin.java.JavaPlugin;
import xiaokun.forge.Forge;

/**
 * @author 小坤
 */
public class ForgeApi {

    public static JavaPlugin getApi(){
        return Forge.getPlugin();
    }
}
