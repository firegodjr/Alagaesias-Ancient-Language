package com.firegodjr.ancientlanguage;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Configuration handler for Anicent Language
 */
public class Config {

	private final Configuration forgeConfig;
	
	private boolean debugOutput;
	
	private boolean allowWerelight;
	
	public Configuration getForgeConfig() {
		return forgeConfig;
	}
	
	public boolean shouldDebugOutput() {
		return debugOutput;
	}
	
	public boolean allowWerelight() {
		return allowWerelight;
	}
	
	public Config(FMLPreInitializationEvent event) {
		forgeConfig =  new Configuration(event.getSuggestedConfigurationFile());
		syncConfig();
	}
	
	public void syncConfig() {
		forgeConfig.load();
		
		debugOutput = forgeConfig.getBoolean("Debug Output", Configuration.CATEGORY_GENERAL, false, "Produces debug output");
		
		forgeConfig.save();
	}
}
