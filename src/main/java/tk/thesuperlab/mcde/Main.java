package tk.thesuperlab.mcde;

import com.samjakob.spigui.SpiGUI;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;
import tk.thesuperlab.mcde.listeners.ChatListener;
import tk.thesuperlab.mcde.listeners.KillListener;
import tk.thesuperlab.mcde.listeners.PlayerInteractionListener;
import tk.thesuperlab.mcde.threads.ProcessThread;

public final class Main extends JavaPlugin {
	public static SpiGUI spiGUI;

	@Override
	public void onEnable() {
		// Clear villagers
		getServer().getWorld("world").getEntities().stream()
				.filter(entity -> entity.getType().equals(EntityType.VILLAGER))
				.forEach(Entity::remove);

		ChatListener.workingDir = "/";
		spiGUI = new SpiGUI(this);

		getServer().getPluginManager().registerEvents(new ChatListener(), this);
		getServer().getPluginManager().registerEvents(new KillListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerInteractionListener(), this);

		getServer().getScheduler().scheduleSyncRepeatingTask(this, new ProcessThread(), 0, 100);
	}

	@Override
	public void onDisable() {
	}
}
