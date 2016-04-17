package com.firegodjr.ancientlanguage.api.script;

import java.util.List;

import com.firegodjr.ancientlanguage.magic.MagicEnergy;
import com.firegodjr.ancientlanguage.utils.ScriptData;

/**
 * A word modifier interface
 */
public interface IModifier extends IScriptObject {

	/**
	 * Allows modification of words in the script
	 * 
	 * @param energy
	 *            The script object energy data
	 * @param data
	 *            The ScriptInstance data to check or modify
	 * @param objects
	 *            The script objects to be modified
	 */
	public void modifyWord(MagicEnergy energy, ScriptData data, List<IScriptObject> objects);

}
