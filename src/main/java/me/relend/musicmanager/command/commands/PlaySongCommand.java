package me.relend.musicmanager.command.commands;

import me.relend.musicmanager.command.Command;
import me.relend.musicmanager.menu.menus.SongMenu;
import org.bukkit.entity.Player;

public class PlaySongCommand extends Command {

	public PlaySongCommand() {
		super("playsong", null, "Play a song!");
	}

	@Override
	public void execute(Player player, String[] args) {
		new SongMenu().open(player);
	}
}