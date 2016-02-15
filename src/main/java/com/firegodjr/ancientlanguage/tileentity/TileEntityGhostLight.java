package com.firegodjr.ancientlanguage.tileentity;

import com.firegodjr.ancientlanguage.ParticleHandler;

import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;

/**
 * A tile entity to handle the updating particle effects of the ghostlight
 */
public class TileEntityGhostLight extends TileEntity implements IUpdatePlayerListBox {

	@Override
	public void update() {
		ParticleHandler.ghostLight(this.pos.getX(), this.pos.getY(), this.pos.getZ(), 1, worldObj);
	}

}
