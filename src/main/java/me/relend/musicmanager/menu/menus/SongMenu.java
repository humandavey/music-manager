package me.relend.musicmanager.menu.menus;

import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import me.relend.musicmanager.MusicManager;
import me.relend.musicmanager.item.ItemBuilder;
import me.relend.musicmanager.menu.Menu;
import me.relend.musicmanager.menu.MenuButton;
import me.relend.musicmanager.util.Util;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;

public class SongMenu extends Menu {


	public SongMenu() {
		super("Song Menu", 6);

		int i = 0;
		for (Song song : MusicManager.getInstance().getMusic().getSongs().values()) {
			if (i < 45) {
				registerButton(i, new MenuButton(new ItemBuilder(Material.MUSIC_DISC_MELLOHI)
						.setItemName(Util.colorize("&6" + song.getTitle() + " &7- &e" + song.getOriginalAuthor()))
						.setLore("", Util.colorize("&a&lRIGHT CLICK &7to toggle in playlist"), Util.colorize("&a&lLEFT CLICK &7to play"))
						.build()).setWhenLeftClicked((player) -> {
					player.sendMessage(Util.colorize("&aNow playing &2" + song.getTitle() + " &aby &2" + song.getOriginalAuthor() + "&a!"));
					RadioSongPlayer rsp = new RadioSongPlayer(song);
					MusicManager.getInstance().getMusic().setPlaying(player, rsp);
				}).setWhenRightClicked((player) -> {
					if (MusicManager.getInstance().getMusic().inPlaylist(player, song)) {
						player.sendMessage(Util.colorize("&aRemoved &2" + song.getTitle() + " &afrom your playlist!"));
						MusicManager.getInstance().getMusic().removeSongFromPlaylist(player, song);
					} else {
						player.sendMessage(Util.colorize("&aAdded &2" + song.getTitle() + " &ato your playlist!"));
						MusicManager.getInstance().getMusic().addSongToPlaylist(player, song);
					}
				}));
				i++;
			}
		}
		registerButton(48, new MenuButton(new ItemBuilder(Material.ARROW)
			.setItemName(Util.colorize("&aLast Song"))
			.build()).setWhenClicked((player) -> {
				if (MusicManager.getInstance().getMusic().getPlaying(player) == null) {
					player.sendMessage(Util.colorize("&cThere is no last song to play."));
					return;
				}
				if (MusicManager.getInstance().getMusic().getPlaying(player) != null) {
					MusicManager.getInstance().getMusic().restartSong(player);
					player.sendMessage(Util.colorize("&aRestarting song."));
					return;
				}
				if (MusicManager.getInstance().getMusic().hasLast(player)) {
					MusicManager.getInstance().getMusic().lastSong(player);
					Song song = MusicManager.getInstance().getMusic().getPlaying(player).getSong();
					player.sendMessage(Util.colorize("&aNow playing &2" + song.getTitle() + " &aby &2" + song.getOriginalAuthor() + "&a!"));
				}
		}));
		registerButton(49, new MenuButton(new ItemBuilder(Material.BARRIER)
			.setItemName(Util.colorize("&cClose"))
			.build()).setWhenClicked(HumanEntity::closeInventory));
		registerButton(50, new MenuButton(new ItemBuilder(Material.ARROW)
			.setItemName(Util.colorize("&aNext Song"))
			.build()).setWhenClicked((player) -> {
				if (MusicManager.getInstance().getMusic().hasNext(player)) {
					MusicManager.getInstance().getMusic().nextSong(player);
					Song song = MusicManager.getInstance().getMusic().getPlaying(player).getSong();
					player.sendMessage(Util.colorize("&aNow playing &2" + song.getTitle() + " &aby &2" + song.getOriginalAuthor() + "&a!"));
					return;
				}
				player.sendMessage(Util.colorize("&cThere is no next song to play."));
		}));
		registerButton(45, new MenuButton(new ItemBuilder(Material.LIME_DYE)
			.setItemName(Util.colorize("&aPlay"))
			.build()).setWhenClicked((player) -> {
				if (MusicManager.getInstance().getMusic().getPlaying(player) == null) {
					player.sendMessage(Util.colorize("&cYou don't have any songs queued."));
				} else {
					if (!MusicManager.getInstance().getMusic().isPlaying(player)) {
						MusicManager.getInstance().getMusic().unpause(player);
						player.sendMessage(Util.colorize("&aMusic playing."));
						return;
					}
					player.sendMessage(Util.colorize("&aMusic is already playing."));
				}
		}));
		registerButton(46, new MenuButton(new ItemBuilder(Material.YELLOW_DYE)
			.setItemName(Util.colorize("&aPause"))
			.build()).setWhenClicked((player) -> {
				if (MusicManager.getInstance().getMusic().getPlaying(player) == null) {
					player.sendMessage(Util.colorize("&cYou don't have any songs queued."));
				} else {
					if (MusicManager.getInstance().getMusic().isPlaying(player)) {
						MusicManager.getInstance().getMusic().pause(player);
						player.sendMessage(Util.colorize("&aMusic paused."));
						return;
					}
					player.sendMessage(Util.colorize("&aMusic is already paused."));
				}
		}));
		registerButton(52, new MenuButton(new ItemBuilder(Material.PINK_DYE)
			.setItemName(Util.colorize("&aStop"))
			.build()).setWhenClicked((player) -> {
				if (MusicManager.getInstance().getMusic().getPlaying(player) != null) {
					MusicManager.getInstance().getMusic().quitPlaying(player);
					player.sendMessage(Util.colorize("&aStopped playing."));
					return;
				}
				player.sendMessage(Util.colorize("&cNo song is playing."));
		}));
		registerButton(53, new MenuButton(new ItemBuilder(Material.RED_DYE)
				.setItemName(Util.colorize("&aClear Playlist"))
				.build()).setWhenClicked((player) -> {
			MusicManager.getInstance().getMusic().clearPlaylist(player);
			player.sendMessage(Util.colorize("&aCleared your playlist."));
		}));
		registerButton(47, new MenuButton(new ItemBuilder(Material.SUNFLOWER)
			.setItemName(Util.colorize("&aLoop Songs"))
			.build()).setWhenClicked((player) -> {
				if (MusicManager.getInstance().getMusic().isLoop(player)) {
					MusicManager.getInstance().getMusic().setLooping(player, false);
					player.sendMessage(Util.colorize("&aLooping songs is now disabled."));
				} else {
					MusicManager.getInstance().getMusic().setLooping(player, true);
					player.sendMessage(Util.colorize("&aLooping songs is now enabled."));
				}
		}));
		registerButton(51, new MenuButton(new ItemBuilder(Material.OAK_SIGN)
			.setItemName(Util.colorize("&aView Playlist"))
			.build()).setWhenClicked((player) -> {
				if (MusicManager.getInstance().getMusic().getPlaylist(player).size() == 0) {
					player.sendMessage(Util.colorize("&cYour playlist is empty."));
					return;
				}
				player.sendMessage(Util.colorize("&6Your Playlist:"));
				for (Song song : MusicManager.getInstance().getMusic().getPlaylist(player)) {
					player.sendMessage(Util.colorize("&7- &e" + song.getTitle()));
				}
		}));
	}
}