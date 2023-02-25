package fr.maxlego08.template.listener;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.maxlego08.template.Template;
import fr.maxlego08.template.zcore.utils.ZUtils;

@SuppressWarnings("deprecation")
public class AdapterListener extends ZUtils implements Listener {

	private final Template plugin;

	public AdapterListener(Template plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onConnect(PlayerJoinEvent event) {
		this.plugin.getListenerAdapters().forEach(adapter -> adapter.onConnect(event, event.getPlayer()));
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		this.plugin.getListenerAdapters().forEach(adapter -> adapter.onQuit(event, event.getPlayer()));
	}

	/*
	 * @EventHandler public void onMove(PlayerMoveEvent event) {
	 * this.plugin.getListenerAdapters().forEach(adapter ->
	 * adapter.onMove(event, event.getPlayer())); if
	 * (event.getFrom().getBlockX() >> 1 == event.getTo().getBlockX() >> 1 &&
	 * event.getFrom().getBlockZ() >> 1 == event.getTo().getBlockZ() >> 1 &&
	 * event.getFrom().getWorld() == event.getTo().getWorld()) return;
	 * this.plugin.getListenerAdapters().forEach(adapter ->
	 * adapter.onPlayerWalk(event, event.getPlayer(), 1)); }
	 */

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		this.plugin.getListenerAdapters()
				.forEach(adapter -> adapter.onInventoryClick(event, (Player) event.getWhoClicked()));
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		this.plugin.getListenerAdapters().forEach(adapter -> adapter.onBlockBreak(event, event.getPlayer()));
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		this.plugin.getListenerAdapters().forEach(adapter -> adapter.onBlockPlace(event, event.getPlayer()));
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		this.plugin.getListenerAdapters().forEach(adapter -> adapter.onEntityDeath(event, event.getEntity()));
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		this.plugin.getListenerAdapters().forEach(adapter -> adapter.onInteract(event, event.getPlayer()));
	}

	@EventHandler
	public void onPlayerTalk(AsyncPlayerChatEvent event) {
		this.plugin.getListenerAdapters().forEach(adapter -> adapter.onPlayerTalk(event, event.getMessage()));
	}

	@EventHandler
	public void onCraftItem(CraftItemEvent event) {
		this.plugin.getListenerAdapters().forEach(adapter -> adapter.onCraftItem(event));
	}

	@EventHandler
	public void onDrag(InventoryDragEvent event) {
		this.plugin.getListenerAdapters()
				.forEach(adapter -> adapter.onInventoryDrag(event, (Player) event.getWhoClicked()));
	}

	@EventHandler
	public void onClose(InventoryCloseEvent event) {
		this.plugin.getListenerAdapters()
				.forEach(adapter -> adapter.onInventoryClose(event, (Player) event.getPlayer()));
	}

	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event) {
		this.plugin.getListenerAdapters()
				.forEach(adapter -> adapter.onCommand(event, event.getPlayer(), event.getMessage()));
	}

	@EventHandler
	public void onGamemodeChange(PlayerGameModeChangeEvent event) {
		this.plugin.getListenerAdapters().forEach(adapter -> adapter.onGamemodeChange(event, event.getPlayer()));
	}

	/*
	 * @EventHandler public void onDrop(PlayerDropItemEvent event) {
	 * this.plugin.getListenerAdapters().forEach(adapter ->
	 * adapter.onDrop(event, event.getPlayer())); if (!Config.useItemFallEvent)
	 * return; Item item = event.getItemDrop(); AtomicBoolean hasSendEvent = new
	 * AtomicBoolean(false); scheduleFix(100, (task, isActive) -> { if
	 * (!isActive) return; this.plugin.getListenerAdapters().forEach(adapter ->
	 * adapter.onItemMove(event, event.getPlayer(), item, item.getLocation(),
	 * item.getLocation().getBlock())); if (item.isOnGround() &&
	 * !hasSendEvent.get()) { task.cancel(); hasSendEvent.set(true);
	 * this.plugin.getListenerAdapters().forEach( adapter ->
	 * adapter.onItemisOnGround(event, event.getPlayer(), item,
	 * item.getLocation())); } }); }
	 */

	@EventHandler
	public void onPick(PlayerPickupItemEvent event) {
		this.plugin.getListenerAdapters().forEach(adapter -> adapter.onPickUp(event, event.getPlayer()));
	}

	@EventHandler
	public void onMobSpawn(CreatureSpawnEvent event) {
		this.plugin.getListenerAdapters().forEach(adapter -> adapter.onMobSpawn(event));
	}

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {

		if (event.getEntity() instanceof LivingEntity && event.getDamager() instanceof LivingEntity) {
			this.plugin.getListenerAdapters().forEach(adapter -> adapter.onDamageByEntity(event, event.getCause(),
					event.getDamage(), (LivingEntity) event.getDamager(), (LivingEntity) event.getEntity()));
		}

		if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
			this.plugin.getListenerAdapters().forEach(adapter -> adapter.onPlayerDamagaByPlayer(event, event.getCause(),
					event.getDamage(), (Player) event.getDamager(), (Player) event.getEntity()));
		}

		if (event.getEntity() instanceof Player && event.getDamager() instanceof Projectile) {
			this.plugin.getListenerAdapters().forEach(adapter -> adapter.onPlayerDamagaByArrow(event, event.getCause(),
					event.getDamage(), (Projectile) event.getDamager(), (Player) event.getEntity()));
		}
	}
}
