package com.firegodjr.ancientlanguage.entities;

import com.firegodjr.ancientlanguage.ParticleHandler;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GhostLightEntity extends TileEntity implements IUpdatePlayerListBox{

	@Override
	public void update() {
		ParticleHandler.ghostLight(this.pos.getX(), this.pos.getY(), this.pos.getZ(), 1, worldObj);
		
	}

}
