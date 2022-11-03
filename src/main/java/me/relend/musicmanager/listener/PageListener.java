package me.relend.musicmanager.listener;

import me.relend.musicmanager.menu.menus.SongMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class PageListener implements Listener {

	@EventHandler
	public void onClick(InventoryClickEvent event) {
		if (event.getClickedInventory() != null && event.getCurrentItem() != null && event.getView().getTitle().contains("Music Menu")) {
			int page = Integer.parseInt(event.getInventory().getItem(0).getItemMeta().getLocalizedName());

			if (event.getRawSlot() == 0 && event.getCurrentItem().getType() == Material.ARROW) {
				new SongMenu((Player) event.getWhoClicked(), page - 1);
			} else if (event.getRawSlot() == 8 && event.getCurrentItem().getType() == Material.ARROW) {
				new SongMenu((Player) event.getWhoClicked(), page + 1);
			}
		}
	}

}