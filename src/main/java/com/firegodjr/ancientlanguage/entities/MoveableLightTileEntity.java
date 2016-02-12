package com.firegodjr.ancientlanguage.entities;

import java.util.Iterator;
import java.util.List;

import com.firegodjr.ancientlanguage.blocks.MoveableLightBlock;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class MoveableLightTileEntity extends TileEntity implements IUpdatePlayerListBox {

	private Entity follow;

	public MoveableLightTileEntity() {
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
		return (oldState.getBlock() != newSate.getBlock());
	}

	@Override
	public void update() {
		if(!follow.getPosition().equals(this.getPos())) {
			follow = this.findNearest();
		}
		Block block = worldObj.getBlockState(getPos()).getBlock();
		if (this.follow == null || !(block instanceof MoveableLightBlock) || !((MoveableLightBlock)block).shouldEmit(this) || 
				!this.shouldEmit()) {
			worldObj.setBlockToAir(getPos());
		}
	}

	public boolean shouldEmit() {
		return this.follow.getPosition().equals(this.getPos());
	}

	public void setFollowEntity(Entity follow) {
		this.follow = follow;
	}

	public Entity getFollowEntity() {
		return this.follow;
	}

	@SuppressWarnings("unchecked")
	private Entity findNearest() {
		List<Entity> list = worldObj.getEntitiesWithinAABB(Entity.class,
				new AxisAlignedBB(this.getPos().add(0.5F, 0.5F, 0.5F), this.getPos().add(-0.5F, -0.5F, -0.5F)));
		Entity entity1 = null;
		double d0 = Double.MAX_VALUE;

		for (Iterator<Entity> it = list.iterator(); it.hasNext();) {
			Entity entity2 = it.next();

			if (!entity2.getPosition().equals(this.getPos())) {
				BlockPos pos = entity2.getPosition();
				double d1 = this.getDistanceSq(pos.getX(), pos.getY(), pos.getZ());

				if (d1 <= d0) {
					entity1 = entity2;
					d0 = d1;
				}
			}
		}

		return entity1;
	}
}
