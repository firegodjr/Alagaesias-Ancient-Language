package com.firegodjr.ancientlanguage.magic;

import java.util.HashMap;
import java.util.Map;

import com.firegodjr.ancientlanguage.Main;
import com.firegodjr.ancientlanguage.api.script.IScriptObject;

/**
 * A Script Registry Class for storing interface and word objects for script use
 */
public class ScriptRegistry {

	private static final Map<String, IScriptObject> wordToInterface = new HashMap<String, IScriptObject>();
	private static final Map<IScriptObject, String> interfaceToWord = new HashMap<IScriptObject, String>();

	/**
	 * Registers word to interface registry
	 * 
	 * @param str
	 *            The string to register to the interface
	 * @param inter
	 *            The interface to register to the string
	 */
	public static void registerWord(String str, IScriptObject inter) {
		wordToInterface.put(str, inter);
		interfaceToWord.put(inter, str);
	}

	/**
	 * Retrieves the interface for a string
	 * 
	 * @param str
	 *            The string to get the interface for
	 */
	public static IScriptObject getInterfaceForString(String str) {
		Main.getLogger().info("Retieving Script Object Interface from word '" + str + "'");
		return wordToInterface.get(str);
	}

	/**
	 * Retrieves the string for an interface
	 * 
	 * @param obj
	 *            The interface to get the string for
	 */
	public static String getStringForInterface(IScriptObject obj) {
		return interfaceToWord.get(obj);
	}

}
