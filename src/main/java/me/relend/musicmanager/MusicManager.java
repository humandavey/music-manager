package me.relend.musicmanager;

import me.relend.musicmanager.command.commands.PlaySongCommand;
import me.relend.musicmanager.manager.MenuManager;
import me.relend.musicmanager.manager.Music;
import me.relend.musicmanager.menu.MenuListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class MusicManager extends JavaPlugin {
	private static MusicManager instance;
	private MenuManager menuManager;
	private Music music;

	@Override
	public void onEnable() {
		instance = this;

		setup();
	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}

	private void setup() {
		setupConfig();
		setupManagers();
		registerCommands();
		registerListeners();
	}

	private void setupConfig() {
		if (!this.getDataFolder().exists()) this.getDataFolder().mkdir();

		getConfig().options().copyDefaults();
		saveDefaultConfig();
	}


	private void setupManagers() {
		menuManager = new MenuManager();
		music = new Music();
	}

	private void registerCommands() {
		new PlaySongCommand();
	}

	private void registerListeners() {
		getServer().getPluginManager().registerEvents(new MenuListener(), this);
		getServer().getPluginManager().registerEvents(music, this);
	}

	public MenuManager getMenuManager() {
		return menuManager;
	}

	public Music getMusic() {
		return music;
	}

	public static MusicManager getInstance() {
		return instance;
	}
}