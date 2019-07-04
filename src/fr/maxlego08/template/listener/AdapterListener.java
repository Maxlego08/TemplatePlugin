package fr.maxlego08.template.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.maxlego08.template.Template;

public class AdapterListener implements Listener {

	private final Template template;

	public AdapterListener(Template template) {
		this.template = template;
	}

	@EventHandler
	public void onConnect(PlayerJoinEvent event) {
		template.getListenerAdapters().forEach(adapter -> adapter.onConnect(event));
	}

	@EventHandler
	public void onQuite(PlayerQuitEvent event) {
		template.getListenerAdapters().forEach(adapter -> adapter.onQuit(event));
	}

	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		template.getListenerAdapters().forEach(adapter -> adapter.onMove(event));
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		template.getListenerAdapters().forEach(adapter -> adapter.onInventoryClick(event));
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		template.getListenerAdapters().forEach(adapter -> adapter.onBlockBreak(event));
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		template.getListenerAdapters().forEach(adapter -> adapter.onBlockPlace(event));
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		template.getListenerAdapters().forEach(adapter -> adapter.onEntityDeath(event));
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		template.getListenerAdapters().forEach(adapter -> adapter.onInteract(event));
	}

	@EventHandler
	public void onPlayerTalk(AsyncPlayerChatEvent event) {
		template.getListenerAdapters().forEach(adapter -> adapter.onPlayerTalk(event));
	}

	@EventHandler
	public void onCraftItem(CraftItemEvent event) {
		template.getListenerAdapters().forEach(adapter -> adapter.onCraftItem(event));
	}
	
	@EventHandler
	public void onDrag(InventoryDragEvent event){
		template.getListenerAdapters().forEach(adapter -> adapter.onInventoryDrag(event));
	}

	@EventHandler
	public void onClose(InventoryCloseEvent event){
		template.getListenerAdapters().forEach(adapter -> adapter.onInventoryClose(event));
	}
	
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event){
		template.getListenerAdapters().forEach(adapter -> adapter.onCommand(event));
	}
	
	@EventHandler
	public void onGamemodeChange(PlayerGameModeChangeEvent event){
		template.getListenerAdapters().forEach(adapter -> adapter.onGamemodeChange(event));
	}	
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent event){
		template.getListenerAdapters().forEach(adapter -> adapter.onDrop(event));
	}
	
	@EventHandler
	public void onPick(PlayerPickupItemEvent event){
		template.getListenerAdapters().forEach(adapter -> adapter.onPickUp(event));
	}
	
	@EventHandler
	public void onMobSpawn(CreatureSpawnEvent event){
		template.getListenerAdapters().forEach(adapter -> adapter.onMobSpawn(event));
	}
}
