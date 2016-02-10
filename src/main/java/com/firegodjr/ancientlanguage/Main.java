package com.firegodjr.ancientlanguage;

import org.apache.logging.log4j.Logger;

import com.firegodjr.ancientlanguage.command.CommandCast;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = Main.MODID, name = Main.MODNAME, version = Main.VERSION)
public class Main {
	
	
	public static final String MODID = "ancientlanguage";
	public static final String MODNAME = "Ancient Language";
	public static final String VERSION = "1.0";

	@Instance(MODID)
	public static Main instance;

	@SidedProxy(clientSide="com.firegodjr.ancientlanguage.ClientProxy", serverSide="com.firegodjr.ancientlanguage.ServerProxy")
	public static CommonProxy proxy;
	
	private Config config;
	private Logger logger;

	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		proxy.preInit(e);
		
		//Configuration file settings and variables
		config = new Config(e); 
		logger = e.getModLog();
	}

	@EventHandler
	public void init(FMLInitializationEvent e) {
		proxy.init(e);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		proxy.postInit(e);
	}
	
	@EventHandler
	public void serverLoad(FMLServerStartingEvent e)
	{
		e.registerServerCommand(new CommandCast());
	}
	
	public static Config getConfig() {
		return instance.config;
	}
	
	public static Logger getLogger() {
		return instance.logger;
	}
}