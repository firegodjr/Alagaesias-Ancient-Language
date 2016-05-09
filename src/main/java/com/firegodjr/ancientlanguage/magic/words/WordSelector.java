package com.firegodjr.ancientlanguage.magic.words;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import com.firegodjr.ancientlanguage.Main;
import com.firegodjr.ancientlanguage.api.script.ISelector;
import com.firegodjr.ancientlanguage.magic.MagicData;
import com.firegodjr.ancientlanguage.utils.BlockPosHit;
import com.firegodjr.ancientlanguage.utils.VersionUtils;
import com.google.common.base.Predicate;

public class WordSelector {

	/**
	 * Selects user
	 */
	public static class MeSelector implements ISelector {
		@Override
		public List<?> getSelected(MagicData energy, Map<String, String> modData, World world, Vec3 position) {
			return Collections.singletonList(energy.getActualUser());
		}
	}

	/**
	 * Selects surrounding entities, possibly user
	 */
	public static class UsSelector implements ISelector {
		public static final int RADIUS = 4;

		@Override
		public List<?> getSelected(MagicData energy, Map<String, String> modData, World world, Vec3 position) {
			Vec3 pos1 = position.addVector(-RADIUS, -RADIUS / 3, -RADIUS),
					pos2 = position.addVector(RADIUS, RADIUS / 3, RADIUS);
			energy.addMagicMultipler(0.2f);
			return world.getEntitiesWithinAABB(EntityLivingBase.class, VersionUtils.getAABBFor(pos1, pos2));
		}
	}

	/**
	 * Selects closest player
	 */
	public static class FriendSelector implements ISelector {
		public static final int MAX_RADIUS = 4;

		@Override
		public List<?> getSelected(MagicData energy, Map<String, String> modData, World world, Vec3 position) {
			return Collections.singletonList(getClosestUnrelatedPlayer(world, energy.getActualUser(), position));
		}

		private EntityPlayer getClosestUnrelatedPlayer(World world, final Object possiblePlayer, final Vec3 position) {
			List<?> list = VersionUtils.findPlayersIn(world, new Predicate<Object>() {
				double distanceSq = -1;

				@Override
				public boolean apply(Object input) {
					if (!(input instanceof EntityPlayer))
						return false;
					EntityPlayer player = (EntityPlayer) input;
					if (distanceSq == -1) {
						distanceSq = VersionUtils.getEntityPosition(player).squareDistanceTo(position);
						return !player.equals(possiblePlayer);
					}
					double newDist = VersionUtils.getEntityPosition(player).squareDistanceTo(position);
					if (distanceSq > newDist) {
						distanceSq = newDist;
						return!player.equals(possiblePlayer);
					}
					return false;
				}
			});
			if (list == null || list.isEmpty() || !(list.get(list.size() - 1) instanceof EntityPlayer))
				return null;
			EntityPlayer p = (EntityPlayer) list.get(list.size() - 1);
			if (VersionUtils.getEntityPosition(p).squareDistanceTo(position) > MAX_RADIUS * MAX_RADIUS)
				return null;
			return p;
		}
	}

	/**
	 * Selects all surround EntityLiving based entities
	 */
	public static class ThoseSelector implements ISelector {
		public static final int RADIUS = 4;

		@Override
		public List<?> getSelected(MagicData energy, Map<String, String> modData, World world, Vec3 position) {
			Vec3 pos1 = position.addVector(-RADIUS, -RADIUS / 3, -RADIUS),
					pos2 = position.addVector(RADIUS, RADIUS / 3, RADIUS);
			return world.getEntitiesWithinAABB(EntityLiving.class, VersionUtils.getAABBFor(pos1, pos2));
		}
	}

	/**
	 * Selects looked at block
	 */
	public static class ThatSelector implements ISelector {
		@Override
		public List<?> getSelected(MagicData energy, Map<String, String> modData, World world, Vec3 position) {
			if (!(energy.getActualUser() instanceof Entity))
				return null;
			return Collections.singletonList(
					new BlockPosHit(VersionUtils.rayTraceFor((Entity) energy.getActualUser(), 64, 1)));
		}
	}

	/**
	 * Selects looked at stone
	 */
	public static class StoneSelector implements ISelector {
		public static final int MAX_RADIUS = 2;

		@Override
		public List<?> getSelected(MagicData energy, Map<String, String> modData, World world, Vec3 position) {
			energy.addMagicMultipler(0.1f);
			return getAllBlockHits(energy.getActualUser(), Blocks.stone);
		}

		private List<BlockPosHit> getAllBlockHits(Object check, Block block) {
			if (!(check instanceof Entity))
				return Collections.emptyList();
			return getBlocks(block, MAX_RADIUS, new BlockPosHit(VersionUtils.rayTraceFor((Entity) check, 64, 1)));
		}
	}

	private static List<BlockPosHit> getBlocks(Block block, int radius, BlockPosHit centerpoint) {
		// TODO: Fix the facing code, it's broken. (now commented out)
		List<BlockPosHit> out = new ArrayList<BlockPosHit>();
		World world = Minecraft.getMinecraft().theWorld;
		int trueX = 0;
		int trueY = 0;
		int trueZ = 0;
		int relX = 0;
		int relY = 0;
		int relZ = 0;
		// int absX = 0;
		// int absY = 0;
		// int absZ = 0;
		for (int x = 0; x < radius * 2 + 1; x++) {
			relX = x - radius;
			trueX = relX + centerpoint.getIX();
			for (int y = 0; y < radius * 2 + 1; y++) {
				relY = y - radius;
				trueY = relY + centerpoint.getIY();
				for (int z = 0; z < radius * 2 + 1; z++) {
					relZ = z - radius;
					trueZ = relZ + centerpoint.getIZ();

					Main.getLogger().info("Checking " + trueX + ", " + trueY + ", " + trueZ + " for blocks matching "
							+ block.getLocalizedName());
					if (VersionUtils.getBlock(world, trueX, trueY, trueZ) == block) {
						// absX = Math.abs(relX);
						// absY = Math.abs(relY);
						// absZ = Math.abs(relZ);
						EnumFacing face = EnumFacing.UP;
						// if(absX > absY)
						// {
						// if(absX > absZ)
						// face = relX > 0 ? EnumFacing.WEST : EnumFacing.EAST;
						// }
						// else if(absY > absZ)
						// {
						// if(absY > absX)
						// face = relY > 0 ? EnumFacing.DOWN : EnumFacing.UP;
						// }
						// else if(absZ > absX)
						// {
						// if(absZ > absY)
						// face = relZ > 0 ? EnumFacing.NORTH :
						// EnumFacing.SOUTH;
						// }
						Main.getLogger().info("   -found matching block and calculated facing side");
						out.add(new BlockPosHit(VersionUtils.createVec3(trueX, trueY, trueZ), face));
					}
				}
			}
		}
		return out;
	}
}
