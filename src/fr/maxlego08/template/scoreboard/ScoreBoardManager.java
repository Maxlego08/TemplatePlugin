package fr.maxlego08.template.scoreboard;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import fr.maxlego08.template.zcore.utils.ZUtils;
import fr.maxlego08.template.zcore.utils.interfaces.CollectionConsumer;

public class ScoreBoardManager extends ZUtils {

	private final Map<Player, FastBoard> boards = new HashMap<Player, FastBoard>();
	private final long schedulerMillisecond;
	private boolean isRunning = false;
	private CollectionConsumer<Player> lines;

	public ScoreBoardManager(long schedulerMillisecond) {
		super();
		this.schedulerMillisecond = schedulerMillisecond;
	}

	/**
	 * Start scheduler
	 */
	public void schedule() {

		if (isRunning)
			return;

		isRunning = true;

		scheduleFix(schedulerMillisecond, (task, canRun) -> {

			// If the task cannot continue then we do not update the scoreboard
			if (!canRun)
				return;

			if (!isRunning) {
				task.cancel();
				return;
			}

			// if the addition of the lines is null then we stop the task
			if (lines == null) {
				task.cancel();
				return;
			}

			boards.forEach((player, board) -> board.updateLines(lines.accept(player)));

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

		if (hasBoard(player))
			return getBoard(player);

		FastBoard board = new FastBoard(player);
		board.updateTitle(title);

		if (lines != null)
			board.updateLines(lines.accept(player));

		boards.put(player, board);

		return board;

	}

	/**
	 * Delete player board
	 * 
	 * @param player
	 * @return
	 */
	public boolean delete(Player player) {
		if (!hasBoard(player))
			return false;
		FastBoard board = getBoard(player);
		board.delete();
		return true;
	}

	/**
	 * Update board title
	 * 
	 * @param player
	 * @param title
	 * @return
	 */
	public boolean updateTitle(Player player, String title) {
		if (!hasBoard(player))
			return false;
		FastBoard board = getBoard(player);
		board.updateTitle(title);
		return true;
	}

	/**
	 * Update board line
	 * 
	 * @param player
	 * @param title
	 * @return
	 */
	public boolean updateLine(Player player, int line, String string) {
		if (!hasBoard(player))
			return false;
		FastBoard board = getBoard(player);
		board.updateLine(line, string);
		return true;
	}

	/**
	 * 
	 * @param player
	 * @return {@link Boolean}
	 */
	public boolean hasBoard(Player player) {
		return boards.containsKey(player);
	}

	/**
	 * 
	 * @param player
	 * @return {@link FastBoard}
	 */
	public FastBoard getBoard(Player player) {
		return boards.getOrDefault(player, null);
	}

	/**
	 * @return the boards
	 */
	public Map<Player, FastBoard> getBoards() {
		return boards;
	}

	/**
	 * @return the schedulerMillisecond
	 */
	public long getSchedulerMillisecond() {
		return schedulerMillisecond;
	}

	/**
	 * @return the isRunning
	 */
	public boolean isRunning() {
		return isRunning;
	}

	/**
	 * @return the lines
	 */
	public CollectionConsumer<Player> getLines() {
		return lines;
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
