package tk.thesuperlab.mcde.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class ChatListener implements Listener {
	public static String workingDir;

	@EventHandler
	public void onChatSent(AsyncPlayerChatEvent event) throws IOException {
		String command = event.getMessage();

		if(command.startsWith("cd ")) {
			String[] params = command.split(" ");
			StringBuilder path = new StringBuilder();

			for(int i = 1; i < params.length; i++) {
				path.append(params[i]);
			}

			workingDir = path.toString();
			return;
		}

		ProcessBuilder processBuilder = new ProcessBuilder();
		processBuilder.directory(new File(workingDir));
		processBuilder.command("sh", "-c", command);

		Process process = processBuilder.start();
		BufferedReader r = new BufferedReader(new InputStreamReader(process.getInputStream()));

		String line = null;
		while((line = r.readLine()) != null) {
			event.getPlayer().getServer().broadcastMessage(line);
		}
	}
}
