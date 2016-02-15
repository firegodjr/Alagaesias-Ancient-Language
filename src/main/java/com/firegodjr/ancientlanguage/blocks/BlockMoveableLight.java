package com.firegodjr.ancientlanguage.blocks;

import com.firegodjr.ancientlanguage.tileentity.TileEntityMoveableLight;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * A block to handle a moving light 
 */
public class BlockMoveableLight extends Block implements ITileEntityProvider {

	protected BlockMoveableLight(String unlocalName, int lightLevel) {
		super(Material.air);
		setDefaultState(blockState.getBaseState());
		setTickRandomly(false);
		setLightLevel(lightLevel);
		setBlockBounds(0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityMoveableLight();
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
		return null;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean isFullCube() {
		return false;
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		return true;
	}

	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ,
			int meta, EntityLivingBase placer) {
		return getDefaultState();
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		return;
	}

	/**
	 * Called when a neighboring block changes.
	 */
	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
		return;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState();
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.CUTOUT;
	}

	@Override
	public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
		return;
	}

	@Override
	public void onLanded(World worldIn, Entity entityIn) {
		return;
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this);
	}

	public boolean shouldEmit(TileEntityMoveableLight entity) {
		return true;
	}
}
