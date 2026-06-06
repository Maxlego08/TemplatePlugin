package fr.maxlego08.template.zcore.utils;

/**
 * This class represents a progress bar with customizable length, symbol, and colors for completed and not completed parts.
 */
public class ProgressBar {

	private final int length;
	private final char symbol;
	private final String completedColor;
	private final String notCompletedColor;

	/**
	 * Constructs a new ProgressBar with the specified attributes.
	 *
	 * @param length            the total length of the progress bar.
	 * @param symbol            the symbol used to represent the progress.
	 * @param completedColor    the color used for the completed portion of the bar.
	 * @param notCompletedColor the color used for the not completed portion of the bar.
	 */
	public ProgressBar(int length, char symbol, String completedColor, String notCompletedColor) {
		super();
		this.length = length;
		this.symbol = symbol;
		this.completedColor = completedColor;
		this.notCompletedColor = notCompletedColor;
	}

	/**
	 * Gets the total length of the progress bar.
	 *
	 * @return the length of the progress bar.
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Gets the symbol used to represent the progress.
	 *
	 * @return the symbol of the progress bar.
	 */
	public char getSymbol() {
		return symbol;
	}

	/**
	 * Gets the color used for the completed portion of the bar.
	 *
	 * @return the completed color as a string.
	 */
	public String getCompletedColor() {
		return completedColor;
	}

	/**
	 * Gets the color used for the not completed portion of the bar.
	 *
	 * @return the not completed color as a string.
	 */
	public String getNotCompletedColor() {
		return notCompletedColor;
	}
}
