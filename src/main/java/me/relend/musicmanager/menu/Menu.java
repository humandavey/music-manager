package me.relend.musicmanager.menu;

import me.relend.musicmanager.MusicManager;
import me.relend.musicmanager.manager.MenuManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Consumer;

import java.util.HashMap;
import java.util.Map;

public class Menu {

	private Inventory inventory;
	public Map<Integer, MenuButton> buttonMap;

	private Consumer<Player> inventoryOpened;
	private Consumer<Player> inventoryClosed;

	public Menu(String title, int rows) {
		if (rows > 6 || rows < 1 || title.length() > 32) {
			throw new IllegalArgumentException("Invalid arguments for Menu class constructor");
		}

		this.inventory = Bukkit.createInventory(null, rows * 9, title);
		this.buttonMap = new HashMap<>();
	}

	public void registerButton(int slot, MenuButton button) {
		buttonMap.put(slot, button);
	}

	public void setInventoryClosed(Consumer<Player> inventoryClosed) {
		this.inventoryClosed = inventoryClosed;
	}

	public void setInventoryOpened(Consumer<Player> inventoryOpened) {
		this.inventoryOpened = inventoryOpened;
	}

	public void handleClose(Player player) {
		if (inventoryClosed != null) {
			inventoryClosed.accept(player);
		}
	}

	public void handleOpen(Player player) {
		if (inventoryOpened != null) {
			inventoryOpened.accept(player);
		}
	}

	public void handleClick(InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack clicked = event.getCurrentItem();
		if (clicked == null) {
			return;
		}
		if (buttonMap.containsKey(event.getRawSlot())) {
			if (event.isLeftClick()) {
				Consumer<Player> consumer = buttonMap.get(event.getRawSlot()).getWhenLeftClicked();
				if (consumer != null) {
					consumer.accept((Player) event.getWhoClicked());
				}
			} else if (event.isRightClick()) {
				Consumer<Player> consumer = buttonMap.get(event.getRawSlot()).getWhenRightClicked();
				if (consumer != null) {
					consumer.accept((Player) event.getWhoClicked());
				}
			}
			Consumer<Player> consumer = buttonMap.get(event.getRawSlot()).getWhenClicked();
			if (consumer != null) {
				consumer.accept((Player) event.getWhoClicked());
			}
		}
	}

	public void open(Player player) {
		MenuManager manager = MusicManager.getInstance().getMenuManager();
		buttonMap.forEach((slot, button) -> inventory.setItem(slot, button.getItem()));

		player.openInventory(inventory);
		manager.registerMenu(player.getUniqueId(), this);
		handleOpen(player);
	}
}