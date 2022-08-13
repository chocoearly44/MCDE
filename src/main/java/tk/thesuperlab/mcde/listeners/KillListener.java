package tk.thesuperlab.mcde.listeners;

import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class KillListener implements Listener {
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		LivingEntity entity = event.getEntity();

		if(entity.getKiller() != null && !StringUtils.isEmpty(entity.getCustomName())) {
			event.getEntity().getServer().broadcastMessage(entity.getCustomName() + " was slain by " + entity.getKiller().getDisplayName());

			try {
				Runtime.getRuntime().exec("killall " + entity.getCustomName());
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
