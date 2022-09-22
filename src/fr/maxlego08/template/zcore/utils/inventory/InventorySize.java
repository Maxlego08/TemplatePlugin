package fr.maxlego08.template.zcore.utils.inventory;

public enum InventorySize {

	FULL_INVENTORY(53, 45, 50, 48, 0),
	FULL_45_INVENTORY(45, 36, 42, 40, 0),
	
	;

	private final int size;
	private final int paginationSize;
	private final int nextSlot;
	private final int previousSlot;
	private final int defaultSlot;

	/**
	 * 
	 * @param size
	 * @param paginationSize
	 * @param nextSlot
	 * @param previousSlot
	 * @param defaultSlot
	 */
	private InventorySize(int size, int paginationSize, int nextSlot, int previousSlot, int defaultSlot) {
		this.size = size;
		this.paginationSize = paginationSize;
		this.nextSlot = nextSlot;
		this.previousSlot = previousSlot;
		this.defaultSlot = defaultSlot;
	}

	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @return the paginationSize
	 */
	public int getPaginationSize() {
		return paginationSize;
	}

	/**
	 * @return the nextSlot
	 */
	public int getNextSlot() {
		return nextSlot;
	}

	/**
	 * @return the previousSlot
	 */
	public int getPreviousSlot() {
		return previousSlot;
	}

	/**
	 * @return the defaultSlot
	 */
	public int getDefaultSlot() {
		return defaultSlot;
	}

}
