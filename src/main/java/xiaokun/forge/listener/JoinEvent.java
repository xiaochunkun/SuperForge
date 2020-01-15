package xiaokun.forge.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import xiaokun.forge.config.PlayerData;

/**
 * @author 小坤
 */
public class JoinEvent implements Listener {

    /**
     * 玩家加入服务器  如果是第一次 生成玩家配置文件
     * @param event
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        if(!PlayerData.getPlayerFile(player).exists()) {
            PlayerData.loadPlayerData(player);
        }
    }
}
