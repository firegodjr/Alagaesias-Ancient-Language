package com.firegodjr.ancientlanguage.magic.words;

import java.util.List;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import com.firegodjr.ancientlanguage.api.script.IWardPlacer;
import com.firegodjr.ancientlanguage.api.script.IWord;
import com.firegodjr.ancientlanguage.blocks.ModBlocks;
import com.firegodjr.ancientlanguage.magic.MagicData;
import com.firegodjr.ancientlanguage.utils.BlockPosHit;

public class WordAction {

	/**
	 * An IWord for creating a shield resistance effect on entities
	 */
	public static class ShieldWord implements IWord {
		@Override
		public void onUse(MagicData energy, Map<String, String> modData, List<?> selectors) {
			int fatigue = 0;
			for (Object obj : selectors) {
				if (obj instanceof EntityLivingBase) {
					fatigue++;
					// Adds "Shield" Effect to selected entities
					((EntityLivingBase) obj)
							.addPotionEffect(new PotionEffect(Potion.resistance.id, 1200, 2, true, false));
				}
			}
			energy.performMagic(fatigue * 0.1f); // 10 fatigue here = 100% energy request
		}
	}

	/**
	 * An IWord for breaking blocks and harming entities
	 */
	public static class BreakWord implements IWord {
		@Override
		public void onUse(MagicData energy, Map<String, String> modData, List<?> selectors) {
			int fatigue = 0;
			for (Object obj : selectors) {
				if (obj instanceof Entity) {
					// Attacks entities with break word
					((Entity) obj).attackEntityFrom(DamageSource.magic, 5F);
					fatigue++;
				} else if (obj instanceof BlockPosHit && energy.getActualUser() instanceof Entity) {
					Entity entity = (Entity) energy.getActualUser();
					World world = entity.worldObj;
					BlockPos pos = ((BlockPosHit) obj).pos;
					fatigue += world.getBlockState(pos).getBlock().getBlockHardness(world, pos) / 4;
					// Breaks blocks with break word
					world.destroyBlock(pos, false);
				}
			}
			energy.performMagic(fatigue * 0.2f); // 5 fatigue here = 100% energy request
		}
	}

	/**
	 * An IWord which instantly kills entities with 30 or less health
	 */
	public static class KillWord implements IWord {
		@Override
		public void onUse(MagicData energy, Map<String, String> modData, List<?> selectors) {
			int fatigue = 0;
			for (Object obj : selectors) {
				if (obj instanceof EntityLivingBase) {
					EntityLivingBase entity = (EntityLivingBase) obj;
					fatigue++; // users must be careful, easy to get harmed by magic here
					if (entity.getHealth() <= 30) {
						entity.setHealth(0.1F);
						// Vergari/Kill Actions
						entity.attackEntityFrom(DamageSource.magic, 0.2F);
					} else {
						if (energy.getActualUser() instanceof EntityPlayer) {
							EntityPlayer player = (EntityPlayer) energy.getActualUser();
							player.attackEntityFrom(DamageSource.magic, 0.1f);
							player.addChatComponentMessage(
									new ChatComponentText(EnumChatFormatting.GRAY + "" + EnumChatFormatting.ITALIC
											+ entity.getName() + " is too powerful to kill with magic!"));
						}
					}
				}
			}
			energy.performMagic(fatigue * 0.25f); // 4 fatigue  = 100% energy request
		}
	}

	/**
	 * An IWord the creates a fire on blocks and ignites entities
	 */
	public static class FireWord implements IWord {
		@Override
		public void onUse(MagicData energy, Map<String, String> modData, List<?> selectors) {
			int fatigue = 0;
			for (Object obj : selectors) {
				if (obj instanceof EntityLivingBase) {
					fatigue++;
					// Sets selected entities on fire
					((EntityLivingBase) obj).setFire(10);
				} else if (obj instanceof BlockPosHit && energy.getActualUser() instanceof Entity) {
					World world = ((Entity) energy.getActualUser()).worldObj;
					BlockPosHit pos = (BlockPosHit) obj;
					// Sets block on fire
					if (world.getBlockState(pos.pos.offset(pos.side)).getBlock() == Blocks.air)
						world.setBlockState(pos.pos.offset(pos.side), Blocks.fire.getDefaultState());
				}
			}
			energy.performMagic(fatigue * 0.1f); // 10 fatigue  = 100% energy request
		}
	}

	/**
	 * An IWord that heals entities
	 */
	public static class HealWord implements IWord {
		@Override
		public void onUse(MagicData energy, Map<String, String> modData, List<?> selectors) {
			int fatigue = 0;
			for (Object obj : selectors) {
				if (obj instanceof EntityLivingBase) {
					fatigue++;
					EntityLivingBase ent = (EntityLivingBase) obj;
					// Heals selected entities
					ent.setHealth(ent.getHealth() + 6);
				}
			}
			energy.performMagic(fatigue * 0.1f); // 10 fatigue = 100% energy request
		}
	}

	/**
	 * An IWord that freezes entities
	 */
	public static class HaltWord implements IWord {
		@Override
		public void onUse(MagicData energy, Map<String, String> modData, List<?> selectors) {
			int fatigue = 0;
			for (Object obj : selectors) {
				if (obj instanceof EntityLivingBase) {
					fatigue++;
					// Prevents entities from moving
					((EntityLivingBase) obj)
							.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 200, 99, true, false));
				}
			}
			energy.performMagic(fatigue * 0.2f); // 5 fatigue = 100% energy request
		}
	}

	/**
	 * An IWord that throws a block very fast upwards
	 */
	public static class RiseWord implements IWord {
		@Override
		public void onUse(MagicData energy, Map<String, String> modData, List<?> selectors) {
			int fatigue = 0;
			for (Object obj : selectors) {
				if (obj instanceof BlockPosHit && energy.getActualUser() instanceof Entity) {
					fatigue++;
					BlockPosHit pos = (BlockPosHit) obj;
					World world = ((Entity) energy.getActualUser()).worldObj;
					world.setBlockState(pos.pos, Blocks.air.getDefaultState());
					EntityFallingBlock blockEntity = new EntityFallingBlock(world, pos.pos.getX() + 0.5,
							pos.pos.getY() + 0.5, pos.pos.getZ() + 0.5, world.getBlockState(pos.pos));
					world.spawnEntityInWorld(blockEntity);
					blockEntity.setVelocity(0, 100, 0);
					// Causes blocks to fly upwards
				}
				if(obj instanceof Entity) {
					fatigue++;
					((Entity)obj).addVelocity(0, 100, 0);
				}
			}
			energy.performMagic(fatigue * 0.2f); // 5 fatigue = 100% energy request
		}
	}

	/**
	 * An IWord that creates a ghostlight on blocks
	 */
	public static class BrightenWord implements IWord {
		@Override
		public void onUse(MagicData energy, Map<String, String> modData, List<?> selectors) {
			for (Object obj : selectors) {
				if (obj instanceof BlockPosHit && energy.getActualUser() instanceof Entity) {
					BlockPosHit pos = (BlockPosHit) obj;
					World world = ((Entity) energy.getActualUser()).worldObj;
					// Creates a ghost/werelight
					if (world.getBlockState(pos.pos.offset(pos.side)).getBlock() == Blocks.air) {
						world.setBlockState(pos.pos.offset(pos.side), ModBlocks.ghostLight.getDefaultState());
					}
				}
			}
			energy.performMagic(0.1f); // no matter what, only uses 10% of energy
		}
	}

	/**
	 * @deprecated Does not work as expected
	 */
	public static class PlaceWardWord implements IWord, IWardPlacer {
		@Override
		public void onUse(MagicData energy, Map<String, String> modData, List<?> selectors) {
			for (Object obj : selectors) {
				if (obj instanceof BlockPosHit && energy.getActualUser() instanceof Entity) {
					BlockPosHit bph = (BlockPosHit) obj;
					@SuppressWarnings("unused")
					Vec3 v = new Vec3(bph.pos.getX(), bph.pos.getY(), bph.pos.getZ());
					//WordHandler.placeWard(((Entity) energy.getActualUser()).worldObj, v, energy.getWardlessChant(), 50);
					return;
				}
			}
		}
	}
}
