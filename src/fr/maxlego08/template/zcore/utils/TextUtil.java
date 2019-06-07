package fr.maxlego08.template.zcore.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;

public class TextUtil {

	/*
	 * Material name
	 * */
	
	public static String getMaterialLower(Material material){
		return material.name().toLowerCase();
	}
	
	public static String getMaterialLowerAndSpace(Material material){
		return material.name().replace("_", " ").toLowerCase();
	}
	
	public static String getMaterialLowerAndMaj(Material material){
		return material.name().toLowerCase().replace(material.name().toCharArray()[0], Character.toUpperCase(material.name().toCharArray()[0]));
	}
	public static String getMaterialLowerAndMajAndSpace(Material material){
		return material.name().replace("_", " ").toLowerCase().replace(material.name().toCharArray()[0], Character.toUpperCase(material.name().toCharArray()[0]));
	}
	
	/*
	 * Parse text
	 * */
	
	public static String parse(String str, Object... args)
	{
		return String.format(parse(str), args);
	}

	/*
	 * Parce color
	 * */
	
	public static String color(String message){
		return message.replace("&", "§");
	}
	
	public static List<String> color(List<String >messages){
		List<String> newList = new ArrayList<>();
		messages.forEach(message -> newList.add(color(message)));
		return newList;
	}

	public static String m(String message, Object... obj){
		return String.format(message, obj);
		
	}
	
}


