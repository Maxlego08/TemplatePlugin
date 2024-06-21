package fr.maxlego08.template.zcore.utils;

import fr.maxlego08.template.save.Config;

/**
 * Utility class for measuring elapsed time between start and end points.
 * Extends {@link ZUtils}.
 */
public class ElapsedTime extends ZUtils {

	private long start;
	private long end;
	private final String name;

	/**
	 * Constructs an ElapsedTime object with a specified name.
	 *
	 * @param name the name of the timer.
	 */
	public ElapsedTime(String name) {
		super();
		this.name = name;
	}

	/**
	 * Starts the timer by recording the current time in nanoseconds.
	 */
	public void start() {
		this.start = System.nanoTime();
	}

	/**
	 * Stops the timer by recording the current time in nanoseconds.
	 */
	public void end() {
		this.end = System.nanoTime();
	}

	/**
	 * Gets the start time recorded by the timer.
	 *
	 * @return the start time in nanoseconds.
	 */
	public long getStart() {
		return start;
	}

	/**
	 * Gets the end time recorded by the timer.
	 *
	 * @return the end time in nanoseconds.
	 */
	public long getEnd() {
		return end;
	}

	/**
	 * Calculates the elapsed time between the start and end points.
	 *
	 * @return the elapsed time in nanoseconds.
	 */
	public long getElapsedTime() {
		return this.end - this.start;
	}

	/**
	 * Stops the timer and prints the elapsed time to the console if debugging is enabled.
	 */
	public void endDisplay() {
		this.end();
		if (Config.enableDebugTime) {
			System.out.println("[ElapsedTime] " + name + " -> " + super.format(this.getElapsedTime(), ' '));
		}
	}
}
