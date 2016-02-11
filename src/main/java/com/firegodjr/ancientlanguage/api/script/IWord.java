package com.firegodjr.ancientlanguage.api.script;

import java.util.List;

import com.firegodjr.ancientlanguage.magic.ScriptInstance;

public interface IWord {
	
	@SuppressWarnings("rawtypes")
	public void onStart(ScriptInstance script, List selectors);
}
