package com.firegodjr.ancientlanguage;

import com.firegodjr.ancientlanguage.blocks.ModBlocks;
import com.firegodjr.ancientlanguage.event.LanguageEventHandler;
import com.firegodjr.ancientlanguage.items.ModItems;
import com.firegodjr.ancientlanguage.magic.words.WordHandler;
import com.firegodjr.ancientlanguage.tileentity.TileEntityGhostLight;
import com.firegodjr.ancientlanguage.wards.RegisterWard;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent e) 
	{
		ModItems.createItems();
		ModBlocks.registerBlocks();
		RegisterWard.registerWardComponents();
		GameRegistry.registerTileEntity(TileEntityGhostLight.class, "nainaLightEntity");
		WordHandler.initalizeWords();
	}

	public void init(FMLInitializationEvent e) {
		LanguageEventHandler handler = new LanguageEventHandler();
		MinecraftForge.EVENT_BUS.register(handler);
		FMLCommonHandler.instance().bus().register(handler);
	}

	public void postInit(FMLPostInitializationEvent e) {

	}
}
