package fr.maxlego08.template.zcore.utils.map;

import java.util.HashMap;
import java.util.Optional;

public class OptionalHashMap<K, V> extends HashMap<K, V> implements OptionalMap<K, V>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1389669310403530512L;

	/**
	 * 
	 * @param key
	 * @return {@link Optional}
	 */
	public Optional<V> getOptional(K key) {
		V value = super.getOrDefault(key, null);
		return value == null ? Optional.empty() : Optional.of(value);
	}

	/**
	 * 
	 * @param key
	 * @return true if is present
	 */
	public boolean isPresent(K key) {
		return getOptional(key).isPresent();
	}

}
