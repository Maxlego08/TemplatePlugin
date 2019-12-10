package fr.maxlego08.template.zcore.utils;

@FunctionalInterface
public interface StringConsumer<T> {

	String accept(T t);
	
}
