package fr.maxlego08.template.zcore.utils.inventory;

public class IIventory {

	private final int id;
	private final int page;
	private final Object[] args;

	public IIventory(int id, int page, Object[] args) {
		super();
		this.id = id;
		this.page = page;
		this.args = args;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the page
	 */
	public int getPage() {
		return page;
	}

	/**
	 * @return the args
	 */
	public Object[] getArgs() {
		return args;
	}
	
}
