package fr.maxlego08.template.zcore.utils.storage;

public interface Saveable {
	
	void save(Persist persist);
	void load(Persist persist);
}
