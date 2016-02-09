package com.firegodjr.ancientlanguage;

import com.firegodjr.ancientlanguage.command.CommandCast;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = Main.MODID, name = Main.MODNAME, version = Main.VERSION)
public class Main {
	
	public static boolean verbose;
	
	public static final String MODID = "ancientlanguage";
	public static final String MODNAME = "Ancient Language";
	public static final String VERSION = "1.0";
	
	public static boolean allowWereLights;

	@Instance(MODID)
	public static Main instance = new Main();

	@SidedProxy(clientSide="com.firegodjr.ancientlanguage.ClientProxy", serverSide="com.firegodjr.ancientlanguage.ServerProxy")
	public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		proxy.preInit(e);
		
		//Configuration file settings and variables
		Configuration config = new Configuration(e.getSuggestedConfigurationFile());
		
			config.load();
			
			verbose = config.getBoolean("Is Verbose", config.CATEGORY_GENERAL, false, "Enables lots and loootttss of output");
			
			config.save();
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
}