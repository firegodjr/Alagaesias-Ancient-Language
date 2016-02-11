package com.firegodjr.ancientlanguage.magic.words;

import com.firegodjr.ancientlanguage.magic.Utilities;

public class WordHandler {

	public static void initalizeWords() {
		Utilities.registerWord("skolir", new WordAction.ShieldWord());
		Utilities.registerWord("naina", new WordAction.BrightenWord());
		Utilities.registerWord("jierda", new WordAction.BreakWord());
		Utilities.registerWord("vergari", new WordAction.KillWord());
		Utilities.registerWord("brisingr", new WordAction.FireWord());
		Utilities.registerWord("heill", new WordAction.HealWord());
		Utilities.registerWord("blothr", new WordAction.HaltWord());
		Utilities.registerWord("risa", new WordAction.RiseWord());
	}
	
}
