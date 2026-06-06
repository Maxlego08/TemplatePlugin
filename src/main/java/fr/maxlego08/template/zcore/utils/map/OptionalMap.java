package fr.maxlego08.template.zcore.utils.map;

import java.util.Map;
import java.util.Optional;

public interface OptionalMap<K, V> extends Map<K, V> {

	/**
	 * 
	 * @param key
	 * @return
	 */
	Optional<V> getOptional(K key);
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	boolean isPresent(K key);
	
}
