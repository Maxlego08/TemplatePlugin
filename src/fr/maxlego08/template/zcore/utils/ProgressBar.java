package fr.maxlego08.template.zcore.utils;

public class ProgressBar {

	private final int lenght;
	private final char symbol;
	private final String completedColor;
	private final String notCompletedColor;

	/**
	 * @param lenght
	 * @param symbol
	 * @param completedColor
	 * @param notCompletedColor
	 */
	public ProgressBar(int lenght, char symbol, String completedColor, String notCompletedColor) {
		super();
		this.lenght = lenght;
		this.symbol = symbol;
		this.completedColor = completedColor;
		this.notCompletedColor = notCompletedColor;
	}

	/**
	 * @return the lenght
	 */
	public int getLenght() {
		return lenght;
	}

	/**
	 * @return the symbol
	 */
	public char getSymbol() {
		return symbol;
	}

	/**
	 * @return the completedColor
	 */
	public String getCompletedColor() {
		return completedColor;
	}

	/**
	 * @return the notCompletedColor
	 */
	public String getNotCompletedColor() {
		return notCompletedColor;
	}

}
