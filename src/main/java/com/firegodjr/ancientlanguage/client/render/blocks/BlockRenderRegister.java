package com.firegodjr.ancientlanguage.client.render.blocks;

import com.firegodjr.ancientlanguage.Main;
import com.firegodjr.ancientlanguage.blocks.ModBlocks;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;

public final class BlockRenderRegister {

	public static void registerBlockRenderer() {
		registerBlock(ModBlocks.ghostLight);
	}

	public static String modid = Main.MODID;

	public static void registerBlock(Block block) {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), 0, new ModelResourceLocation(modid + ":" + block.getUnlocalizedName().substring(5), "inventory"));
	}

}
