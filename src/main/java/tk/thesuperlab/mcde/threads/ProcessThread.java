package tk.thesuperlab.mcde.threads;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static org.bukkit.Bukkit.getServer;

public class ProcessThread implements Runnable {
	public static ArrayList<String> oldArr = new ArrayList<>();
	public static ArrayList<String> newArr = new ArrayList<>();

	@Override
	public void run() {
		World world = getServer().getWorld("world");

		try {
			newArr.clear();

			oldArr = (ArrayList<String>) world.getEntities().stream()
					.filter(entity -> entity.getType().equals(EntityType.VILLAGER))
					.filter(entity -> !StringUtils.isEmpty(entity.getCustomName()))
					.map(entity -> entity.getCustomName())
					.collect(Collectors.toList());

			Process process = Runtime.getRuntime().exec("ps -e");
			BufferedReader r = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;

			while((line = r.readLine()) != null) {
				String[] processes = line.split(" ");
				newArr.add(processes[processes.length - 1]);
			}

			ArrayList<String> stoppedProcesses = (ArrayList<String>) oldArr.clone();
			stoppedProcesses.removeAll(newArr);

			world.getEntities().stream()
					.filter(entity -> stoppedProcesses.contains(entity.getCustomName()))
					.forEach(Entity::remove);

			newArr.removeAll(oldArr);
			newArr.forEach(pname -> {
				Villager tank = (Villager) world.spawnEntity(new Location(world, -25.0, -60.0, 11.0), EntityType.VILLAGER);
				tank.setCustomName(pname);
			});
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
