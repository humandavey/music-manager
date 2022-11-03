package me.relend.musicmanager;

import me.relend.musicmanager.command.commands.PlaySongCommand;
import me.relend.musicmanager.listener.MusicListener;
import me.relend.musicmanager.manager.Music;
import me.relend.musicmanager.listener.PageListener;
import org.bukkit.plugin.java.JavaPlugin;


public final class MusicManager extends JavaPlugin {
	private static MusicManager instance;
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
		music = new Music();
	}

	private void registerCommands() {
		new PlaySongCommand();
	}

	private void registerListeners() {
		getServer().getPluginManager().registerEvents(new PageListener(), this);
		getServer().getPluginManager().registerEvents(new MusicListener(), this);
		getServer().getPluginManager().registerEvents(music, this);
	}


	public Music getMusic() {
		return music;
	}

	public static MusicManager getInstance() {
		return instance;
	}
}