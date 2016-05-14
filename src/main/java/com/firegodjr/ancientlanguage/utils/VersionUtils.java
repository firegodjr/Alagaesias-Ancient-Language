package com.firegodjr.ancientlanguage.utils;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;

import com.google.common.base.Predicate;

/**
 * Provides cross-version compatibility, abstracting the need to constantly update script objects
 */
public final class VersionUtils {

	private VersionUtils() {}

	@SuppressWarnings("unchecked")
	public static List<Object> findPlayersIn(World world, final Predicate<Object> predicate) {
		 return world.getPlayers(EntityPlayer.class, predicate);
	}

	public static MovingObjectPosition rayTraceFor(Entity tracefor, double distance, float ticks) {
		Vec3 vec3 = tracefor.getPositionEyes(ticks);
        Vec3 vec31 = tracefor.getLook(ticks);
        vec31 = vec3.addVector(vec31.xCoord * distance, vec31.yCoord * distance, vec31.zCoord * distance);
        return tracefor.worldObj.rayTraceBlocks(vec3, vec31, false, false, true);
	}

	public static AxisAlignedBB getAABBFor(Vec3 v1, Vec3 v2) {
		return new AxisAlignedBB(v1.xCoord, v1.yCoord, v1.zCoord, v2.xCoord, v2.yCoord, v2.zCoord);
	}

	public static AxisAlignedBB getAABBFor(Vec3i v1, Vec3i v2) {
		return new AxisAlignedBB(v1.getX(), v1.getY(), v1.getZ(), v2.getX(), v2.getY(), v2.getZ());
	}

	public static Vec3 getEntityPosition(Entity ent) {
		return ent.getPositionVector();
	}

	public static float getBlockHardness(World w, BlockPosHit pos) {
		BlockPos p = pos.getBlockPos();
		return w.getBlockState(p).getBlock().getBlockHardness(w, p);
	}

	public static boolean destroyBlock(World w, BlockPosHit pos, boolean drop) {
		return w.destroyBlock(pos.getBlockPos(), drop);
	}

	public static PotionEffect createEffectNoParticles(int id, int dur, int amp, boolean amb) {
		return new PotionEffect(id, dur, amp, amb, false);
	}

	public static boolean isAirBlock(World w, BlockPosHit pos) {
		return w.isAirBlock(pos.getBlockPos().offset(pos.side));
	}

	public static void setAir(World w, BlockPosHit pos) {
		w.setBlockToAir(pos.getBlockPos());
	}

	public static Block getBlock(World w, int x, int y, int z) {
		return w.getBlockState(createBlockPos(x,y,z)).getBlock();
	}

	public static Block getBlock(World w, BlockPosHit pos) {
		return w.getBlockState(createBlockPos(pos)).getBlock();
	}

	private static BlockPos createBlockPos(BlockPosHit v) {
		return createBlockPos(v.getIX(), v.getIY(), v.getIZ());
	}

	private static BlockPos createBlockPos(int x, int y, int z) {
		return new BlockPos(x, y, z);
	}

	public static void setBlock(World w, BlockPosHit pos, Block b) {
		setBlock(w, pos, b.getDefaultState());
	}

	public static void setBlock(World w, BlockPosHit pos, IBlockState b) {
		w.setBlockState(pos.getBlockPos().offset(pos.side), b);
	}

	public static Vec3 createVec3(double x, double y, double z) {
		return new Vec3(x,y,z);
	}

	public static Vec3 createVec3(Vec3i vec) {
		return new Vec3(vec.getX(), vec.getY(), vec.getZ());
	}

	public static void spawnParticle(World w, Vec3 pos, Vec3 offset, EnumParticleTypes particle) {
		w.spawnParticle(particle.toParticleType(), pos.xCoord, pos.yCoord,
				pos.zCoord, offset.xCoord, offset.yCoord, offset.zCoord);
	}

	public static void spawnParticle(World w, Vec3 pos, Vec3 offset, String particle) {
		w.spawnParticle(net.minecraft.util.EnumParticleTypes.valueOf(
				particle.toUpperCase()), pos.xCoord, pos.yCoord,
				pos.zCoord, offset.xCoord, offset.yCoord, offset.zCoord);
	}
	
	public static Entity getEntity(ICommandSender sender) {
		return sender instanceof Entity ? (Entity) sender : sender.getCommandSenderEntity();
	}

	/**
	 * Provides cross-version compatibility for Minecraft's {@link net.minecraft.util.EnumParticleTypes EnumParticleTypes}
	 */
	public static enum EnumParticleTypes
	{
		EXPLOSION_NORMAL("explode"),
		EXPLOSION_LARGE("largeexplode"),
		EXPLOSION_HUGE("hugeexplosion"),
		FIREWORKS_SPARK("fireworksSpark"),
		WATER_BUBBLE("bubble"),
		WATER_SPLASH("splash"),
		WATER_WAKE("wake"),
		SUSPENDED("suspended"),
		SUSPENDED_DEPTH("depthsuspend"),
		CRIT("crit"),
		CRIT_MAGIC("magicCrit"),
		SMOKE_NORMAL("smoke"),
		SMOKE_LARGE("largesmoke"),
		SPELL("spell"),
		SPELL_INSTANT("instantSpell"),
		SPELL_MOB("mobSpell"),
		SPELL_MOB_AMBIENT("mobSpellAmbient"),
		SPELL_WITCH("witchMagic"),
		DRIP_WATER("dripWater"),
		DRIP_LAVA("dripLava"),
		VILLAGER_ANGRY("angryVillager"),
		VILLAGER_HAPPY("happyVillager"),
		TOWN_AURA("townaura"),
		NOTE("note"),
		PORTAL("portal"),
		ENCHANTMENT_TABLE("enchantmenttable"),
		FLAME("flame"),
		LAVA("lava"),
		FOOTSTEP("footstep"),
		CLOUD("cloud"),
		REDSTONE("reddust"),
		SNOWBALL("snowballpoof"),
		SNOW_SHOVEL("snowshovel"),
		SLIME("slime"),
		HEART("heart"),
		BARRIER("barrier"),
		ITEM_CRACK("iconcrack_"),
		BLOCK_CRACK("blockcrack_"),
		BLOCK_DUST("blockdust_"),
		WATER_DROP("droplet"),
		ITEM_TAKE("take"),
		MOB_APPEARANCE("mobappearance");
		private final String particleName;

		private EnumParticleTypes(String particleNameIn) {
			this.particleName = particleNameIn;
		}

		public String getParticleName() {
			return this.particleName;
		}

		public net.minecraft.util.EnumParticleTypes toParticleType() {
			return net.minecraft.util.EnumParticleTypes
					.valueOf(this.toString());
		}
	}
}
