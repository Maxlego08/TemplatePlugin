package fr.maxlego08.hopper.zcore.enums;

public enum EnumInventory {

	INVENTORY_TEST(1),
	
	;
	
	private final int id;

	private EnumInventory(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

}
