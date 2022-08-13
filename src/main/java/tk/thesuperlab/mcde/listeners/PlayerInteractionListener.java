package tk.thesuperlab.mcde.listeners;

import com.samjakob.spigui.SGMenu;
import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import tk.thesuperlab.mcde.Main;
import tk.thesuperlab.mcde.filesystem.FileButton;

import java.io.File;
import java.util.Arrays;

public class PlayerInteractionListener implements Listener {
	private static File workingDir = new File("/");

	@EventHandler
	public void onPlayerInteractionEvent(PlayerInteractEvent event) {
		if(!(event.getClickedBlock().getState() instanceof Chest)) {
			return;
		}

		event.setCancelled(true);
		listFiles(event.getPlayer());
	}

	private void listFiles(Player player) {
		try {
			SGMenu fileExplorer = Main.spiGUI.create(workingDir.getAbsolutePath(), 5);

			SGButton upButton = new SGButton(new ItemBuilder(Material.REDSTONE_TORCH).name("..").build());
			upButton.withListener(event -> {
				workingDir = workingDir.getParentFile();
				listFiles(player);
			});
			fileExplorer.addButton(upButton);

			Arrays.stream(workingDir.listFiles()).forEach(file -> {
				SGButton fileButton;

				if(file.isDirectory()) {
					fileButton = new SGButton(new ItemBuilder(Material.CHEST).name(file.getName()).build());
					fileButton.withListener(event -> {
						workingDir = new File(file.getPath());
						listFiles(player);
					});
				} else {
					fileButton = new SGButton(new ItemBuilder(Material.PAPER).name(file.getName()).build());
					fileButton.withListener(new FileButton(file, player));
				}

				fileExplorer.addButton(fileButton);
			});

			player.openInventory(fileExplorer.getInventory());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
