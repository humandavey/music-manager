package me.relend.musicmanager.manager;

import me.relend.musicmanager.menu.Menu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MenuManager {

	private final Map<UUID, Menu> openMenus;

	public MenuManager() {
		this.openMenus = new HashMap<>();
	}

	public void registerMenu(UUID register, Menu menu) {
		openMenus.put(register, menu);
	}

	public void unregisterMenu(UUID unregister) {
		openMenus.remove(unregister);
	}

	public Menu matchMenu(UUID uuid) {
		return openMenus.get(uuid);
	}

	public Player getPlayer(Menu menu) {
		for (UUID uuid : openMenus.keySet()) {
			if (openMenus.get(uuid).equals(menu)) {
				return Bukkit.getPlayer(uuid);
			}
		}
		return null;
	}
}