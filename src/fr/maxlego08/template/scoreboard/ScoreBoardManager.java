package fr.maxlego08.template.scoreboard;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import fr.maxlego08.template.zcore.utils.ZUtils;
import fr.maxlego08.template.zcore.utils.interfaces.CollectionConsumer;

public class ScoreBoardManager extends ZUtils {

	private final Plugin plugin;
	private final Map<Player, FastBoard> boards = new HashMap<Player, FastBoard>();
	private final long schedulerMillisecond;
	private boolean isRunning = false;
	private CollectionConsumer<Player> lines;

	public ScoreBoardManager(Plugin plugin, long schedulerMillisecond) {
		super();
		this.schedulerMillisecond = schedulerMillisecond;
		this.plugin = plugin;
	}

	/**
	 * Start scheduler
	 */
	public void schedule() {

		if (this.isRunning) {
			return;
		}

		this.isRunning = true;

		scheduleFix(this.plugin, this.schedulerMillisecond, (task, canRun) -> {

			// If the task cannot continue then we do not update the scoreboard
			if (!canRun)
				return;

			if (!this.isRunning) {
				task.cancel();
				return;
			}

			// if the addition of the lines is null then we stop the task
			if (this.lines == null) {
				task.cancel();
				return;
			}

			Iterator<FastBoard> iterator = this.boards.values().iterator();
			while (iterator.hasNext()) {
				FastBoard b = iterator.next();
				if (b.isDeleted() || !b.getPlayer().isOnline()) {
					this.boards.remove(b.getPlayer());
				}
			}

			this.boards.forEach((player, board) -> board.updateLines(this.lines.accept(player)));

		});
	}

	/**
	 * Create a scoreboard for a player
	 * 
	 * @param player
	 * @param title
	 * @return {@link FastBoard}
	 */
	public FastBoard createBoard(Player player, String title) {

		if (this.hasBoard(player)) {
			return this.getBoard(player);
		}

		FastBoard board = new FastBoard(player);
		board.updateTitle(title);

		if (this.lines != null) {
			board.updateLines(this.lines.accept(player));
		}

		this.boards.put(player, board);

		return board;

	}

	/**
	 * Delete player board
	 * 
	 * @param player
	 * @return
	 */
	public boolean delete(Player player) {

		if (!this.hasBoard(player)) {
			return false;
		}

		FastBoard board = getBoard(player);
		if (!board.isDeleted()) {
			board.delete();
			return true;
		}

		return false;
	}

	/**
	 * Update board title
	 * 
	 * @param player
	 * @param title
	 * @return boolean
	 */
	public boolean updateTitle(Player player, String title) {

		if (!hasBoard(player)) {
			return false;
		}

		FastBoard board = getBoard(player);
		if (!board.isDeleted()) {
			board.updateTitle(title);
			return true;
		}
		return false;
	}

	/**
	 * Update board line
	 * 
	 * @param player
	 * @param title
	 * @return boolean
	 */
	public boolean updateLine(Player player, int line, String string) {

		if (!hasBoard(player)) {
			return false;
		}

		FastBoard board = getBoard(player);
		if (!board.isDeleted()) {
			board.updateLine(line, string);
			return true;
		}
		return false;
	}

	/**
	 * Check if player has board
	 * 
	 * @param player
	 * @return {@link Boolean}
	 */
	public boolean hasBoard(Player player) {
		return this.boards.containsKey(player);
	}

	/**
	 * Return player's board
	 * 
	 * @param player
	 * @return {@link FastBoard}
	 */
	public FastBoard getBoard(Player player) {
		return this.boards.getOrDefault(player, null);
	}

	/**
	 * @return the boards
	 */
	public Map<Player, FastBoard> getBoards() {
		return this.boards;
	}

	/**
	 * @return the schedulerMillisecond
	 */
	public long getSchedulerMillisecond() {
		return this.schedulerMillisecond;
	}

	/**
	 * @return the isRunning
	 */
	public boolean isRunning() {
		return this.isRunning;
	}

	/**
	 * @return the lines
	 */
	public CollectionConsumer<Player> getLines() {
		return this.lines;
	}

	/**
	 * @param isRunning
	 *            the isRunning to set
	 */
	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	/**
	 * @param lines
	 *            the lines to set
	 */
	public void setLines(CollectionConsumer<Player> lines) {
		this.lines = lines;
	}

	/**
	 * @param lines
	 *            the lines to set
	 */
	public void setLinesAndSchedule(CollectionConsumer<Player> lines) {
		this.lines = lines;
		this.schedule();
	}

}
