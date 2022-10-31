package me.relend.musicmanager.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Consumer;

public class MenuButton {

	private ItemStack item;
	private Consumer<Player> whenLeftClicked;
	private Consumer<Player> whenRightClicked;
	private Consumer<Player> whenClicked;

	public MenuButton(ItemStack item) {
		this.item = item;
	}

	public MenuButton setWhenLeftClicked(Consumer<Player> whenLeftClicked) {
		this.whenLeftClicked = whenLeftClicked;
		return this;
	}

	public Consumer<Player> getWhenLeftClicked() {
		return whenLeftClicked;
	}

	public MenuButton setWhenRightClicked(Consumer<Player> whenRightClicked) {
		this.whenRightClicked = whenRightClicked;
		return this;
	}

	public Consumer<Player> getWhenClicked() {
		return whenClicked;
	}

	public MenuButton setWhenClicked(Consumer<Player> whenClicked) {
		this.whenClicked = whenClicked;
		return this;
	}

	public Consumer<Player> getWhenRightClicked() {
		return whenRightClicked;
	}

	public ItemStack getItem() {
		return item;
	}
}