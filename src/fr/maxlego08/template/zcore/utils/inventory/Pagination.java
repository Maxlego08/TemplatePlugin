package fr.maxlego08.template.zcore.utils.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Pagination<T> {

	/**
	 * Allows you to sort a list of items according to the number of items and the page
	 * Here the pagination will allow to invert the list of elements
	 * The system can be used to create inventories with several pages for example
	 * 
	 * @param list - List of element
	 * @param size - Pagination size
	 * @param page - Current pagination page
	 */
	public List<T> paginateReverse(List<T> list, int inventorySize, int page) {
		List<T> currentList = new ArrayList<>();
		if (page == 0)
			page = 1;
		int idStart = list.size() - 1 - ((page - 1) * inventorySize);
		int idEnd = idStart - inventorySize;
		if (idEnd < list.size() - inventorySize && list.size() < inventorySize * page)
			idEnd = -1;
		for (int a = idStart; a != idEnd; a--)
			currentList.add(list.get(a));
		return currentList;
	}

	/**
	 * Allows you to sort a list of items according to the number of items and the page
	 * The system can be used to create inventories with several pages for example
	 * 
	 * @param list - List of element
	 * @param size - Pagination size
	 * @param page - Current pagination page
	 */
	public List<T> paginate(List<T> list, int size, int page) {
		List<T> currentList = new ArrayList<>();
		if (page <= 0)
			page = 1;
		int idStart = 0 + ((page - 1)) * size;
		int idEnd = idStart + size;
		if (idEnd > list.size())
			idEnd = list.size();
		for (int a = idStart; a != idEnd; a++)
			currentList.add(list.get(a));
		return currentList;
	}
	
	/**
	 * The pagination will be done on the values of the map
	 *
	 * @param map of element
	 * @parma size
	 * @param page
	 */
	public List<T> paginateReverse(Map<?, T> map, int size, int page) {
		return paginateReverse(new ArrayList<>(map.values()), size, page);
	}

	/**
	 * The pagination will be done on the values of the map
	 * 
	 * @param map of element
	 * @parma size
	 * @param page
	 */
	public List<T> paginate(Map<?, T> map, int inventorySize, int page) {
		return paginate(new ArrayList<>(map.values()), inventorySize, page);
	}

}
