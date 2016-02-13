package com.firegodjr.ancientlanguage.wards;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class RegisterWard {
	
	public static WardBlock wardblock;
	
	public static void registerWardComponents()
	{
		GameRegistry.registerBlock(wardblock = new WardBlock("wardblock"), "wardblock");
		GameRegistry.registerTileEntity(WardEntity.class, "wardentity");
	}

}
