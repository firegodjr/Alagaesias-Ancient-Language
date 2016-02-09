package com.firegodjr.ancientlanguage.wards;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class Ward extends BlockContainer {
	
	public List<String> args = new ArrayList();
	public String target = "";
	public int charge;
	private WardEntity ward;

	public Ward(String name) {
		super(Material.air);
		this.setUnlocalizedName(name);
	}
	
	@Override
	public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase player)
	{
		ward = new WardEntity(args, target, charge);
		return (IBlockState) this.getBlockState();
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return ward;
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
	public boolean isSolidFullCube()
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
