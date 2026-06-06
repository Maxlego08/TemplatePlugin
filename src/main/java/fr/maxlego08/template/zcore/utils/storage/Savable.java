package fr.maxlego08.template.zcore.utils.storage;

/**
 * The Savable interface represents objects that can be saved and loaded using a provided Persist object.
 * Implement this interface in classes that need to be serialized and deserialized.
 */
public interface Savable {

	/**
	 * Saves the state of the object using the provided Persist object.
	 *
	 * @param persist The Persist object used to save the state.
	 */
	void save(Persist persist);

	/**
	 * Loads the state of the object from the provided Persist object.
	 *
	 * @param persist The Persist object used to load the state.
	 */
	void load(Persist persist);
}
