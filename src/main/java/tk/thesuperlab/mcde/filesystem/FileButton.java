package tk.thesuperlab.mcde.filesystem;

import com.samjakob.spigui.buttons.SGButtonListener;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import xyz.upperlevel.spigot.book.BookUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class FileButton implements SGButtonListener {
	private final File file;
	private final Player player;

	public FileButton(File file, Player player) {
		this.file = file;
		this.player = player;
	}

	@Override
	public void onClick(InventoryClickEvent event) {
		StringBuilder builder = new StringBuilder();

		try(BufferedReader buffer = new BufferedReader(new FileReader(file.getPath()))) {
			String str;

			while((str = buffer.readLine()) != null) {
				builder.append(str).append("\n");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}

		ItemStack book = BookUtil.writtenBook()
				.author(file.getAbsolutePath())
				.title(file.getName())
				.pages(new BaseComponent[] {
								new TextComponent(builder.toString())
						}
				)
				.build();

		BookUtil.openPlayer(player, book);
	}
}
