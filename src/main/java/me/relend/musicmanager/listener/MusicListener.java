package me.relend.musicmanager.listener;

import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import me.relend.musicmanager.MusicManager;
import me.relend.musicmanager.util.Util;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MusicListener implements Listener {

	@EventHandler
	public void onClick(InventoryClickEvent event) {
		if (event.getClickedInventory() != null && event.getCurrentItem() != null && event.getView().getTitle().contains("Music Menu")) {
			Player player = (Player) event.getWhoClicked();
			event.setCancelled(true);
			if (event.getCurrentItem().getType() == Material.MUSIC_DISC_MELLOHI) {
				Song song = MusicManager.getInstance().getMusic().getSong(event.getCurrentItem().getItemMeta().getLocalizedName());
				if (event.getClick() == ClickType.LEFT) {
					player.sendMessage(Util.colorize("&aNow playing &2" + song.getTitle() + " &aby &2" + song.getOriginalAuthor() + "&a!"));
					RadioSongPlayer rsp = new RadioSongPlayer(song);
					MusicManager.getInstance().getMusic().setPlaying(player, rsp, true);
				} else if (event.getClick() == ClickType.RIGHT) {
					if (MusicManager.getInstance().getMusic().inPlaylist(player, song)) {
						player.sendMessage(Util.colorize("&aRemoved &2" + song.getTitle() + " &afrom your playlist!"));
						MusicManager.getInstance().getMusic().removeSongFromPlaylist(player, song);
					} else {
						player.sendMessage(Util.colorize("&aAdded &2" + song.getTitle() + " &ato your playlist!"));
						MusicManager.getInstance().getMusic().addSongToPlaylist(player, song);
					}
				}
			} else if (event.getCurrentItem().getType() == Material.ARROW && event.getCurrentItem().getItemMeta().getDisplayName().contains("Last Song")) {
				if (MusicManager.getInstance().getMusic().getPlaying(player) == null) {
					player.sendMessage(Util.colorize("&cThere is no last song to play."));
					return;
				}
				if (MusicManager.getInstance().getMusic().hasLast(player)) {
					MusicManager.getInstance().getMusic().lastSong(player);
					Song song = MusicManager.getInstance().getMusic().getPlaying(player).getSong();
					player.sendMessage(Util.colorize("&aNow playing &2" + song.getTitle() + " &aby &2" + song.getOriginalAuthor() + "&a!"));
					return;
				}
				if (MusicManager.getInstance().getMusic().getPlaying(player) != null) {
					MusicManager.getInstance().getMusic().restartSong(player);
					player.sendMessage(Util.colorize("&aRestarting song."));
				}
			} else if (event.getCurrentItem().getType() == Material.BARRIER && event.getCurrentItem().getItemMeta().getDisplayName().contains("Close")) {
				player.closeInventory();
			} else if (event.getCurrentItem().getType() == Material.ARROW && event.getCurrentItem().getItemMeta().getDisplayName().contains("Next Song")) {
				if (MusicManager.getInstance().getMusic().hasNext(player)) {
					MusicManager.getInstance().getMusic().nextSong(player);
					Song song = MusicManager.getInstance().getMusic().getPlaying(player).getSong();
					player.sendMessage(Util.colorize("&aNow playing &2" + song.getTitle() + " &aby &2" + song.getOriginalAuthor() + "&a!"));
					return;
				}
				player.sendMessage(Util.colorize("&cThere is no next song to play."));
			} else if (event.getCurrentItem().getType() == Material.OAK_SIGN && event.getCurrentItem().getItemMeta().getDisplayName().contains("View Playlist")) {
				if (MusicManager.getInstance().getMusic().getPlaylist(player).size() == 0) {
					player.sendMessage(Util.colorize("&cYour playlist is empty."));
					return;
				}
				player.sendMessage(Util.colorize("&6Your Playlist:"));
				for (Song song : MusicManager.getInstance().getMusic().getPlaylist(player)) {
					if (MusicManager.getInstance().getMusic().getPlaying(player).getSong().equals(song)) {
						player.sendMessage(Util.colorize("&7- &a" + song.getTitle()));
						continue;
					}
					player.sendMessage(Util.colorize("&7- &e" + song.getTitle()));
				}
			} else if (event.getCurrentItem().getType() == Material.SUNFLOWER && event.getCurrentItem().getItemMeta().getDisplayName().contains("Loop Songs")) {
				if (MusicManager.getInstance().getMusic().isLoop(player)) {
					MusicManager.getInstance().getMusic().setLooping(player, false);
					player.sendMessage(Util.colorize("&aLooping songs is now disabled."));
				} else {
					MusicManager.getInstance().getMusic().setLooping(player, true);
					player.sendMessage(Util.colorize("&aLooping songs is now enabled."));
				}
			} else if (event.getCurrentItem().getType() == Material.RED_DYE && event.getCurrentItem().getItemMeta().getDisplayName().contains("Clear Playlist")) {
				MusicManager.getInstance().getMusic().clearPlaylist(player);
				player.sendMessage(Util.colorize("&aCleared your playlist."));
			} else if (event.getCurrentItem().getType() == Material.PINK_DYE && event.getCurrentItem().getItemMeta().getDisplayName().contains("Stop")) {
				if (MusicManager.getInstance().getMusic().getPlaying(player) != null) {
					MusicManager.getInstance().getMusic().quitPlaying(player);
					player.sendMessage(Util.colorize("&aStopped playing."));
					return;
				}
				player.sendMessage(Util.colorize("&cNo song is playing."));
			} else if (event.getCurrentItem().getType() == Material.LIME_DYE && event.getCurrentItem().getItemMeta().getDisplayName().contains("Play")) {
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
			} else if (event.getCurrentItem().getType() == Material.YELLOW_DYE && event.getCurrentItem().getItemMeta().getDisplayName().contains("Pause")) {
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
			}
		}
	}
}