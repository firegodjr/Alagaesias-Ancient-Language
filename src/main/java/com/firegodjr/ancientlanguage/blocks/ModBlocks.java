package com.firegodjr.ancientlanguage.blocks;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks {
	
	public static Block ghostLight;

	public static void registerBlocks()
	{
		GameRegistry.registerBlock(ghostLight = new GhostLight("ghostLight"), "ghostLight");
	}
}
