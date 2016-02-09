package com.firegodjr.ancientlanguage.wards;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class RegisterWard {
	
	public static Block wardblock;
	
	public static void RegisterWardComponents()
	{
		GameRegistry.registerBlock(wardblock = new Ward("wardblock"), "wardblock");
		GameRegistry.registerTileEntity(WardEntity.class, "wardentity");
	}

}
