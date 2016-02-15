package com.firegodjr.ancientlanguage.client;

import com.firegodjr.ancientlanguage.Main;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;

public class ModGuiConfig extends GuiConfig {

	public ModGuiConfig(GuiScreen parentScreen) {
		super(parentScreen, 
				new ConfigElement(Main.getConfig().getForgeConfig().getCategory(Configuration.CATEGORY_GENERAL))
				.getChildElements(), Main.MODID, true, false, "Ancient Language Config");
	}

}
