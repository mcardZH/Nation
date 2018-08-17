package cn.minezone.nation;

import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author mcard
 */
public class RankUpdateRunnable extends BukkitRunnable {
    @Override
    public void run() {
        NationAPI.updateRankAsy(null);
    }
}
