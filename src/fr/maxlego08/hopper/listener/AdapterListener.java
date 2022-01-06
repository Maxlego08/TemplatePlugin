package fr.maxlego08.hopper.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.maxlego08.hopper.HopperPlugin;
import fr.maxlego08.hopper.zcore.utils.ZUtils;

public class AdapterListener extends ZUtils implements Listener {

	private final HopperPlugin template;

	public AdapterListener(HopperPlugin template) {
		this.template = template;
	}

	@EventHandler
	public void onConnect(PlayerJoinEvent event) {
		template.getListenerAdapters().forEach(adapter -> adapter.onConnect(event, event.getPlayer()));
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		template.getListenerAdapters().forEach(adapter -> adapter.onQuit(event, event.getPlayer()));
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		template.getListenerAdapters()
				.forEach(adapter -> adapter.onInventoryClick(event, (Player) event.getWhoClicked()));
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent event) {

		if (event.isCancelled()) {
			return;
		}

		template.getListenerAdapters().forEach(adapter -> adapter.onBlockBreak(event, event.getPlayer()));
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockPlace(BlockPlaceEvent event) {

		if (event.isCancelled()) {
			return;
		}

		template.getListenerAdapters().forEach(adapter -> adapter.onBlockPlace(event, event.getPlayer()));
	}

	@EventHandler
	public void onDrag(InventoryDragEvent event) {
		template.getListenerAdapters()
				.forEach(adapter -> adapter.onInventoryDrag(event, (Player) event.getWhoClicked()));
	}

	@EventHandler
	public void onClose(InventoryCloseEvent event) {
		template.getListenerAdapters().forEach(adapter -> adapter.onInventoryClose(event, (Player) event.getPlayer()));
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onExplode(EntityExplodeEvent event) {

		if (event.isCancelled()) {
			return;
		}

		this.template.getListenerAdapters()
				.forEach(adapter -> adapter.onExplode(event, event.blockList(), event.getEntity()));
	}

}
