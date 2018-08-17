package cn.minezone.nation;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * @author mcard
 */
public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (NationAPI.getPlayerNation(e.getPlayer()).equals(NationAPI.getPlayerDefaultNation())) {
            NationAPI.setPlayerNation(e.getPlayer(), NationAPI.getPlayerDefaultNation());
        }
        NationAPI.updateRankAsy(null);
    }
}
