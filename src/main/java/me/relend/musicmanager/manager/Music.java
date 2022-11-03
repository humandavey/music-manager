package me.relend.musicmanager.manager;

import com.xxmicloxx.NoteBlockAPI.event.SongEndEvent;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import me.relend.musicmanager.MusicManager;
import me.relend.musicmanager.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.UUID;

public class Music implements Listener {

	private final TreeMap<String, Song> songs;
	private final HashMap<UUID, RadioSongPlayer> nowPlaying;
	private final HashMap<UUID, ArrayList<Song>> playlists;
	private final ArrayList<UUID> loop;

	public Music() {
		songs = new TreeMap<>();
		nowPlaying = new HashMap<>();
		playlists = new HashMap<>();
		loop = new ArrayList<>();

		for (File f : new File(MusicManager.getInstance().getDataFolder() + "/songs").listFiles()) {
			if (f.getName().endsWith(".nbs")) {
				Song s = NBSDecoder.parse(f);
				if (s != null) {
					songs.put(s.getTitle().toUpperCase(), s);
				}
			}
		}

		for (Player player : Bukkit.getOnlinePlayers()) {
			playlists.put(player.getUniqueId(), new ArrayList<>());
		}
	}

	public Song getSong(String name) {
		return songs.get(name.toUpperCase());
	}

	public void setPlaying(Player player, RadioSongPlayer rsp, boolean clear) {
		if (nowPlaying.containsKey(player.getUniqueId()))
			nowPlaying.get(player.getUniqueId()).destroy();
		nowPlaying.remove(player.getUniqueId());
		nowPlaying.put(player.getUniqueId(), rsp);
		if (clear)
			playlists.get(player.getUniqueId()).clear();
		playlists.get(player.getUniqueId()).add(rsp.getSong());
		rsp.addPlayer(player);
		rsp.setPlaying(true);
	}

	public void pause(Player player) {
		if (nowPlaying.containsKey(player.getUniqueId())) {
			nowPlaying.get(player.getUniqueId()).setPlaying(false);
		}
	}

	public void setLooping(Player player, boolean loop) {
		if (loop) {
			this.loop.add(player.getUniqueId());
		} else {
			this.loop.remove(player.getUniqueId());
		}
	}

	public boolean isLoop(Player player) {
		return this.loop.contains(player.getUniqueId());
	}

	public void unpause(Player player) {
		if (nowPlaying.containsKey(player.getUniqueId())) {
			nowPlaying.get(player.getUniqueId()).setPlaying(true);
		}
	}

	public void quitPlaying(Player player) {
		if (nowPlaying.containsKey(player.getUniqueId()))
			nowPlaying.get(player.getUniqueId()).destroy();
		nowPlaying.remove(player.getUniqueId());
		playlists.get(player.getUniqueId()).clear();
	}

	public void addSongToPlaylist(Player player, Song song) {
		playlists.get(player.getUniqueId()).add(song);
	}

	public void removeSongFromPlaylist(Player player, Song song) {
		playlists.get(player.getUniqueId()).remove(song);
	}

	public void clearPlaylist(Player player) {
		playlists.get(player.getUniqueId()).clear();
	}

	public boolean isPlaying(Player player) {
		return nowPlaying.get(player.getUniqueId()).isPlaying();
	}

	public boolean inPlaylist(Player player, Song song) {
		return playlists.get(player.getUniqueId()).contains(song);
	}

	public void nextSong(Player player) {
		if (hasNext(player)) {
			Song song = playlists.get(player.getUniqueId()).get(playlists.get(player.getUniqueId()).indexOf(getPlaying(player).getSong()) + 1);
			RadioSongPlayer rsp = new RadioSongPlayer(song);
			if (nowPlaying.containsKey(player.getUniqueId()))
				nowPlaying.get(player.getUniqueId()).destroy();
			nowPlaying.remove(player.getUniqueId());
			nowPlaying.put(player.getUniqueId(), rsp);
			rsp.addPlayer(player);
			rsp.setPlaying(true);
		}
	}

	public void restartSong(Player player) {
		RadioSongPlayer rsp = new RadioSongPlayer(getPlaying(player).getSong());
		if (nowPlaying.containsKey(player.getUniqueId()))
			nowPlaying.get(player.getUniqueId()).destroy();
		nowPlaying.remove(player.getUniqueId());
		nowPlaying.put(player.getUniqueId(), rsp);
		rsp.addPlayer(player);
		rsp.setPlaying(true);
	}

	public boolean hasNext(Player player) {
		if (playlists.get(player.getUniqueId()) == null || playlists.get(player.getUniqueId()).size() == 0) return false;
		return playlists.get(player.getUniqueId()).indexOf(getPlaying(player).getSong()) + 1 < playlists.get(player.getUniqueId()).size();
	}

	public boolean hasLast(Player player) {
		return playlists.get(player.getUniqueId()).indexOf(getPlaying(player).getSong()) - 1 < playlists.get(player.getUniqueId()).size() && playlists.get(player.getUniqueId()).indexOf(getPlaying(player).getSong()) - 1 >= 0;
	}

	public void lastSong(Player player) {
		if (hasLast(player)) {
			Song song = playlists.get(player.getUniqueId()).get(playlists.get(player.getUniqueId()).indexOf(getPlaying(player).getSong()) - 1);
			if (nowPlaying.containsKey(player.getUniqueId()))
				nowPlaying.get(player.getUniqueId()).destroy();
			RadioSongPlayer rsp = new RadioSongPlayer(song);
			nowPlaying.remove(player.getUniqueId());
			nowPlaying.put(player.getUniqueId(), rsp);
			rsp.addPlayer(player);
			rsp.setPlaying(true);
		}
	}

	public RadioSongPlayer getPlaying(Player player) {
		return nowPlaying.get(player.getUniqueId());
	}

	public TreeMap<String, Song> getSongs() {
		return songs;
	}

	public ArrayList<Song> getPlaylist(Player player) {
		return playlists.get(player.getUniqueId());
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		if (!playlists.containsKey(event.getPlayer().getUniqueId())) {
			playlists.put(event.getPlayer().getUniqueId(), new ArrayList<>());
		}
		if (nowPlaying.containsKey(event.getPlayer().getUniqueId())) {
			unpause(event.getPlayer());
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		if (nowPlaying.containsKey(event.getPlayer().getUniqueId())) {
			pause(event.getPlayer());
		}
	}

	@EventHandler
	public void onSongStop(SongEndEvent event) {
		Player player = Bukkit.getPlayer(UUID.fromString(event.getSongPlayer().getPlayerUUIDs().toArray()[0].toString()));
		if (playlists.get(player.getUniqueId()).indexOf(event.getSongPlayer().getSong()) + 1 < playlists.get(player.getUniqueId()).size()) {
			Song song = playlists.get(player.getUniqueId()).get(playlists.get(player.getUniqueId()).indexOf(event.getSongPlayer().getSong()) + 1);
			setPlaying(player, new RadioSongPlayer(song), false);
			player.sendMessage(Util.colorize("&aNow playing &2" + song.getTitle() + " &aby &2" + song.getOriginalAuthor() + "&a!"));
			return;
		}
		if (isLoop(player)) {
			Song song = playlists.get(player.getUniqueId()).get(0);
			setPlaying(player, new RadioSongPlayer(song), false);
			player.sendMessage(Util.colorize("&aNow playing &2" + song.getTitle() + " &aby &2" + song.getOriginalAuthor() + "&a!"));
			return;
		}
		quitPlaying(player);
	}
}