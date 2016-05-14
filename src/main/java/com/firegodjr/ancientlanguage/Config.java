package com.firegodjr.ancientlanguage;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import com.google.common.collect.Lists;

/**
 * Configuration handler for Ancient Language
 */
public class Config {

	private final Configuration forgeConfig;

	private boolean debugOutput;
	private boolean allowWerelight;
	private List<String> usernameDescovers = Lists.newArrayList();

	private static final Pattern descoverRegex = Pattern.compile("[^\\\\][\\\\][^\\\\]");

	/**
	 * Retrieves the actual Forge Config class
	 */
	public Configuration getForgeConfig() {
		return forgeConfig;
	}

	/**
	 * Returns whether debug should be output
	 */
	public boolean shouldDebugOutput() {
		return debugOutput;
	}

	/**
	 * Whether Werelight is allowed?
	 */
	public boolean allowWerelight() {
		return allowWerelight;
	}

	/**
	 * Returns a name version of string
	 */
	public String getNameVersion(String string) {
		int sIndex, eIndex = -2;
		for(String s : this.usernameDescovers) {
			sIndex = string.indexOf(s.charAt(0));
			if (s.length() > 1) eIndex = string.indexOf(s.charAt(1));
			if (sIndex != -1) {
				if (eIndex < -1) return string.substring(sIndex+1, string.length());
				else if (eIndex != -1) return string.substring(sIndex+1, eIndex);
			}
			eIndex = -2;
		}
		return "";
	}

	public Config(FMLPreInitializationEvent event) {
		forgeConfig =  new Configuration(event.getSuggestedConfigurationFile());
		syncConfig();
	}

	private void parseDescoverString(String string) {
		this.usernameDescovers.clear();
		int start = 0;
		Matcher m = descoverRegex.matcher(string);
		for(int i = 1; i <= m.groupCount(); i++) {
			this.usernameDescovers.add(string.substring(start, m.start(i)+1));
			start = m.start(i)+2;
		}
	}

	/**
	 * Sync the config when changed from GUI
	 */
	public void syncConfig() {
		forgeConfig.load();

		debugOutput = forgeConfig.getBoolean("Debug Output", Configuration.CATEGORY_GENERAL, false, "Produces debug output");
		parseDescoverString(forgeConfig.getString("Username Descover Pattern", Configuration.CATEGORY_GENERAL, "@\\\"\"",
				"The first character is where to enter the name, the second character closes it, seperate with a \\ for mutiple"));

		forgeConfig.save();
	}
}
