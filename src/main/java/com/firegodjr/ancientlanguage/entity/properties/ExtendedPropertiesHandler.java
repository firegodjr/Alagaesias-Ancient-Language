package com.firegodjr.ancientlanguage.entity.properties;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameRules.ValueType;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class ExtendedPropertiesHandler {

	@SubscribeEvent
	public void onEntityConstruct(EntityConstructing evt) {
		if (evt.entity instanceof EntityPlayer) evt.entity.registerExtendedProperties(ExtPropMagicExperience.NAME, new ExtPropMagicExperience());
	}

	@SubscribeEvent
	public void onPlayerClone(PlayerEvent.Clone evt) {
		if (evt.wasDeath && !evt.original.worldObj.getGameRules().getGameRuleBooleanValue("aal.preserveDeadExperience"))
			return;
		NBTTagCompound compound = new NBTTagCompound();
		ExtPropMagicExperience.getExtProp(evt.original).saveNBTData(compound);
		ExtPropMagicExperience.getExtProp(evt.entityPlayer).loadNBTData(compound);
	}

	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load evt) {
		GameRules gr = evt.world.getGameRules();
		if (!gr.hasRule("aal.preserveDeadExperience")) gr.addGameRule("aal.preserveDeadExperience", "true", ValueType.BOOLEAN_VALUE);
	}
}
