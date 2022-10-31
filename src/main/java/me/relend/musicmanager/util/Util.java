package me.relend.musicmanager.util;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class Util {

	public static List<String> colorizeList(List<String> list) {
		List<String> strings = new ArrayList<>();
		for (String s : list) {
			strings.add(colorize(s));
		}
		return strings;
	}

	public static String colorize(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}
}