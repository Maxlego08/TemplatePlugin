package fr.maxlego08.hopper;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import fr.maxlego08.hopper.listener.ListenerAdapter;

public class HopperListener extends ListenerAdapter {

	private final HopperManager manager;

	/**
	 * @param manager
	 */
	public HopperListener(HopperManager manager) {
		super();
		this.manager = manager;
	}

	@Override
	protected void onBlockBreak(BlockBreakEvent event, Player player) {

		Block block = event.getBlock();
		if (!block.getType().equals(Material.HOPPER)) {
			return;
		}

		this.manager.blockBreak(player, event, event.getBlock());

	}

	@Override
	protected void onBlockPlace(BlockPlaceEvent event, Player player) {
		this.manager.blockPlace(player, event, event.getBlock());
	}

	@Override
	public void onExplode(EntityExplodeEvent event, List<Block> blockList, Entity entity) {
		this.manager.explode(event, blockList);
	}

}
