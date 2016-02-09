package com.firegodjr.ancientlanguage.wards;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;

public class WardSender extends EntityLiving implements ICommandSender{

	public WardSender(World worldIn) {
		super(worldIn);
	}

}
