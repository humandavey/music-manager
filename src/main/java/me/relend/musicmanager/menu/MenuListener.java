package me.relend.musicmanager.menu;

import me.relend.musicmanager.MusicManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class MenuListener implements Listener {

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Menu matchedMenu = MusicManager.getInstance().getMenuManager().matchMenu(event.getWhoClicked().getUniqueId());
		if (matchedMenu != null) {
			matchedMenu.handleClick(event);
		}
	}

	@EventHandler
	public void InventoryClose(InventoryCloseEvent event) {
		Menu matchedMenu = MusicManager.getInstance().getMenuManager().matchMenu(event.getPlayer().getUniqueId());
		if (matchedMenu != null) {
			matchedMenu.handleClose((Player) event.getPlayer());
		}
		MusicManager.getInstance().getMenuManager().unregisterMenu(event.getPlayer().getUniqueId());
	}

	@EventHandler
	public void PlayerQuit(PlayerQuitEvent event) {
		Menu matchedMenu = MusicManager.getInstance().getMenuManager().matchMenu(event.getPlayer().getUniqueId());
		if (matchedMenu != null) {
			matchedMenu.handleClose(event.getPlayer());
		}
		MusicManager.getInstance().getMenuManager().unregisterMenu(event.getPlayer().getUniqueId());
	}
}