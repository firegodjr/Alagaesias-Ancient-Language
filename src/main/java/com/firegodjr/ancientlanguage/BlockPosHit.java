package com.firegodjr.ancientlanguage;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

/**
 * A storage object that holds both BlockPos and EnumFacing objects
 * @author Ethan
 *
 */
public class BlockPosHit {
	public BlockPos pos = new BlockPos(0, 0, 0);
	public EnumFacing side;
	
	public BlockPosHit(BlockPos posIn, EnumFacing sideIn)
	{
		pos = posIn;
		side = sideIn;
	}
}
