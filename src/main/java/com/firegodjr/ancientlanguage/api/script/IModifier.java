package com.firegodjr.ancientlanguage.api.script;

import com.firegodjr.ancientlanguage.magic.ScriptInstance;

/**
 * A word modifier interface
 */
public interface IModifier extends IScriptObject {

	/**
	 * Allows modification of word before this word
	 * 
	 * @param instance
	 *            The instance of the script
	 * @param object
	 *            The script object that comes before
	 */
	public void modifyWord(ScriptInstance instance, IScriptObject object);

}
