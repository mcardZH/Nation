package cn.minezone.nation;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author mcard
 */
public class UpdatePlayerTag extends BukkitRunnable {

    @Override
    public void run() {
        PlayerNameAPI api = new PlayerNameAPI();
        for (Player player : Bukkit.getOnlinePlayers()) {
            //&f[&aXXX&f]
            try {
                api.setTag(player, "§f[§a" + NationAPI.getPlayerNation(player) + "§f]", "§f[§a" + NationAPI.getPlayerTag(player) + "§f]");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
