package me.relend.musicmanager.menu;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Page {

	public static List<ItemStack> getPageItems(List<ItemStack> items, int page, int availableSlots) {
		int upper = page * availableSlots;
		int lower = upper - availableSlots;

		List<ItemStack> newItems = new ArrayList<>();
		for (int i = lower; i < upper; i++) {
			try {
				newItems.add(items.get(i));
			} catch (IndexOutOfBoundsException e) {
				break;
			}
		}
		return newItems;
	}

	public static boolean isPageValid(List<ItemStack> items, int page, int availableSlots) {
		if (page <= 0) return false;
		int upper = page * availableSlots;
		int lower = upper - availableSlots;

		return items.size() > lower;
	}
}