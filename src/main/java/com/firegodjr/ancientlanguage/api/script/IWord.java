package com.firegodjr.ancientlanguage.api.script;

import java.util.List;
import java.util.Map;

import com.firegodjr.ancientlanguage.magic.MagicEnergy;

/**
 * An Interface for perform actions in the script
 */
public interface IWord extends IScriptObject {

	/**
	 * Actions to perform on word use
	 * 
	 * @param energy
	 *            The script object energy data
	 * @param modData
	 *            The specific immutable data for this {@link IScriptObject}
	 * @param selectors
	 *            The selectors to perform check/perform tasks on
	 */
	public void onUse(MagicEnergy energy, Map<String, String> modData, List<?> selectors);
}
