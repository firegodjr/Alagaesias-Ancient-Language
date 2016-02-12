package com.firegodjr.ancientlanguage.entities;

import com.firegodjr.ancientlanguage.ParticleHandler;

import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;

public class GhostLightEntity extends TileEntity implements IUpdatePlayerListBox{

	@Override
	public void update() {
		ParticleHandler.ghostLight(this.pos.getX(), this.pos.getY(), this.pos.getZ(), 1, worldObj);
		
	}

}
