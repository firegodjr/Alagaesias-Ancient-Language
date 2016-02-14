package com.firegodjr.ancientlanguage.entities;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

@SuppressWarnings("unused")
public class WereLight extends Entity{
	
	public EntityPlayer player;
	
	private World world;
	private BlockPos pos;
	private Block posBlock;
	private Block currentBlock;
	private int ticker;

	public WereLight(World worldin) {
		super(worldin);
		world = worldin;
		ticker = 0;
		pos = new BlockPos(0, 0, 0);
		this.noClip = true;
		this.height = 0;
		this.width = 0;
	}
	
	@Override
	public void onUpdate()
	{
		
	}
	
	public void remove()
	{
		
	}

	@Override
	protected void entityInit() {
		this.setInvisible(true); //Doesn't appear to work, but you can never be too careful. :P
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound tagCompund) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound tagCompound) {
		// TODO Auto-generated method stub
		
	}

}
