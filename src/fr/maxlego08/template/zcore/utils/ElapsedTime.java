package fr.maxlego08.template.zcore.utils;

import fr.maxlego08.template.save.Config;

public class ElapsedTime extends ZUtils {

	private long start;
	private long end;

	private final String name;

	/**
	 * @param name
	 */
	public ElapsedTime(String name) {
		super();
		this.name = name;
	}

	/**
	 * Start
	 */
	public void start() {
		this.start = System.nanoTime();
	}

	/**
	 * Stop
	 */
	public void end() {
		this.end = System.nanoTime();
	}

	/**
	 * @return the start
	 */
	public long getStart() {
		return start;
	}

	/**
	 * @return the end
	 */
	public long getEnd() {
		return end;
	}

	public long getElapsedTime() {
		return this.end - this.start;
	}

	public void endDisplay() {
		this.end();
		if (Config.enableDebugTime)
			System.out.println("[ElapsedTime] " + name + " -> " + super.format(this.getElapsedTime(), ' '));
	}

}
