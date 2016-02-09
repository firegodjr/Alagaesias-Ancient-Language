package com.firegodjr.ancientlanguage.blocks;

import java.util.Random;

import com.firegodjr.ancientlanguage.ParticleHandler;
import com.firegodjr.ancientlanguage.entities.GhostLightEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class GhostLight extends BlockContainer {

	public GhostLight(String unlocalizedName, Material material) {
		super(material);
		this.setUnlocalizedName(unlocalizedName);
		this.setLightLevel(0.9f);
		this.setLightOpacity(0);
		this.minY = 0.33D;
		this.minX = 0.33D;
		this.minZ = 0.33D;
		this.maxY = 0.67D;
		this.maxX = 0.67D;
		this.maxZ = 0.67D;
	}

	public GhostLight(String unlocalizedName) {
		this(unlocalizedName, Material.circuits);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return new GhostLightEntity();
	}
	@Override
	public void onBlockDestroyedByPlayer(World world, BlockPos pos, IBlockState state)
	{
		ParticleHandler.burst(pos.add(0.5, 0.5, 0.5), world, 10, 0.5, EnumParticleTypes.FIREWORKS_SPARK);
	}
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return null;
		
	}
	
	@Override
	public boolean isOpaqueCube()
    {
        return false;
    }
	
	@Override
    public boolean isFullCube()
    {
        return false;
    }
	
	@Override
	public boolean canDropFromExplosion(Explosion explosion)
	{
		return false;
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        return null;
    }

	
	
}
