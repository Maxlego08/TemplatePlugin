package fr.maxlego08.hopper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bukkit.Chunk;
import org.bukkit.Location;

import fr.maxlego08.hopper.zcore.utils.ZUtils;
import fr.maxlego08.hopper.zcore.utils.storage.Persist;
import fr.maxlego08.hopper.zcore.utils.storage.Saveable;

public class HopperBoard extends ZUtils implements Saveable {

	public static Map<String, List<SHopper>> hoppers = new HashMap<>();

	@Override
	public void save(Persist persist) {
		persist.save(this, "board");
	}

	@Override
	public void load(Persist persist) {
		persist.loadOrSaveDefault(this, HopperBoard.class, "board");
	}

	/**
	 * Permet de récupérer une liste de hopper
	 * 
	 * @param chunk
	 * @return
	 */
	public List<SHopper> getHoppers(Chunk chunk) {
		return hoppers.getOrDefault(changeChunkToString(chunk), new ArrayList<SHopper>());
	}

	/**
	 * Permet de récupérer un hopper
	 * 
	 * @param location
	 * @return
	 */
	public Optional<SHopper> getHopper(Location location) {
		Chunk chunk = location.getChunk();
		List<SHopper> hoppers = this.getHoppers(chunk);
		return hoppers.stream().filter(e -> same(location, e.getLocation())).findFirst();
	}

	/**
	 * Permet de créer un hopper
	 * 
	 * @param hopper
	 */
	public void createHopper(SHopper hopper) {
		Chunk chunk = hopper.getChunk();
		List<SHopper> hoppers = this.getHoppers(chunk);
		hoppers.add(hopper);
		HopperBoard.hoppers.put(changeChunkToString(chunk), hoppers);
	}

	/**
	 * Permet de supprimer un hopper
	 * 
	 * @param hopper
	 */
	public void remove(SHopper hopper) {
		Chunk chunk = hopper.getChunk();
		List<SHopper> hoppers = this.getHoppers(chunk);
		hoppers.remove(hopper);
		HopperBoard.hoppers.put(changeChunkToString(chunk), hoppers);
	}

	/**
	 * Retourne la liste des hopper load
	 * 
	 * @return
	 */
	public Collection<SHopper> getActiveHoppers() {
		return hoppers.values().stream().flatMap(e -> e.stream()).filter(SHopper::isLoad).collect(Collectors.toList());
	}

	/**
	 * Retourne la liste des hopper load
	 * 
	 * @return
	 */
	public long countHoppers() {
		return hoppers.values().stream().flatMap(e -> e.stream()).count();
	}

}