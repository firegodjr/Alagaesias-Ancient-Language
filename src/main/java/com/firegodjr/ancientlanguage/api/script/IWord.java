package com.firegodjr.ancientlanguage.api.script;

import java.util.List;

import com.firegodjr.ancientlanguage.magic.ScriptInstance;

/**
 * An Interface for perform actions in the script
 */
public interface IWord extends IScriptObject {

	/**
	 * Actions to perform on word use
	 * 
	 * @param script
	 *            The script instance
	 * @param selectors
	 *            The selectors to perform check/perform tasks on
	 */
	public void onUse(ScriptInstance script, List<?> selectors);
}
