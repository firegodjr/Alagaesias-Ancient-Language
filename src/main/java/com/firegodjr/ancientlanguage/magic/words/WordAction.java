package com.firegodjr.ancientlanguage.magic.words;

import java.util.List;

import com.firegodjr.ancientlanguage.BlockPosHit;
import com.firegodjr.ancientlanguage.api.script.IWord;
import com.firegodjr.ancientlanguage.blocks.ModBlocks;
import com.firegodjr.ancientlanguage.magic.ScriptInstance;

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
import net.minecraft.world.World;

@SuppressWarnings("rawtypes")
public class WordAction {

	public static class ShieldWord implements IWord {
		@Override
		public void onUse(ScriptInstance script, List selectors) {
			int fatigue = 0;
			for(Object obj : selectors) {
				if(obj instanceof EntityLivingBase) {
					fatigue++;
					// Adds "Shield" Effect to selected entities
					((EntityLivingBase)obj).addPotionEffect(new PotionEffect(Potion.resistance.id, 1200, 2, true, false));
				}
			}
			script.getEnergy().performMagic(fatigue*20); // TODO: Balance Fatigue
		}
	}
	
	public static class BreakWord implements IWord {
		@Override
		public void onUse(ScriptInstance script, List selectors) {
			int fatigue = 0;
			for(Object obj : selectors) {
				if(obj instanceof Entity) {
					// Attacks entities with break word
					((Entity)obj).attackEntityFrom(DamageSource.magic, 5F);
					fatigue++;
				} else if(obj instanceof BlockPosHit && script.getActualUser() instanceof Entity) {
					Entity entity = (Entity)script.getActualUser();
					World world = entity.worldObj;
					BlockPos pos = ((BlockPosHit)obj).pos;
					fatigue += world.getBlockState(pos).getBlock().getBlockHardness(world, pos) / 4;
					// Breaks blocks with break word
					world.destroyBlock(pos, false);
				}
			}
			script.getEnergy().performMagic(fatigue*20); // TODO: Balance Fatigue
		}
	}
	
	public static class KillWord implements IWord {
		@Override
		public void onUse(ScriptInstance script, List selectors) {
			int fatigue = 0;
			for(Object obj : selectors) {
				if(obj instanceof EntityLivingBase) {
					EntityLivingBase entity = (EntityLivingBase) obj;
					if(entity.getHealth() <= 30) {
						fatigue++;
						entity.setHealth(0.1F);
						// Vergari/Kill Actions
						entity.attackEntityFrom(DamageSource.magic, 0.2F);
					} else {
						if(script.getActualUser() instanceof EntityPlayer) {
							EntityPlayer player = (EntityPlayer) script.getActualUser();
							player.attackEntityFrom(DamageSource.magic, 0.1f);
							player.addChatComponentMessage(
									new ChatComponentText(EnumChatFormatting.GRAY + "" + EnumChatFormatting.ITALIC
											+ entity.getName() + " is too powerful to kill with magic!"));
						}
					}
				}
			}
			script.getEnergy().performMagic(fatigue*20); // TODO: Balance Fatigue
		}
	}
	
	public static class FireWord implements IWord {
		@Override
		public void onUse(ScriptInstance script, List selectors) {
			int fatigue = 0;
			for(Object obj : selectors) {
				if(obj instanceof EntityLivingBase) {
					fatigue++;
					// Sets selected entities on fire
					((EntityLivingBase)obj).setFire(10);
				} else if(obj instanceof BlockPosHit && script.getActualUser() instanceof Entity) {
					World world = ((Entity)script.getActualUser()).worldObj;
					BlockPosHit pos = (BlockPosHit) obj;
					// Sets block on fire
					if(world.getBlockState(pos.pos.offset(pos.side)).getBlock() == Blocks.air)
						world.setBlockState(pos.pos.offset(pos.side), Blocks.fire.getDefaultState());
				}
			}
			script.getEnergy().performMagic(fatigue*20); // TODO: Balance Fatigue
		}
	}
	
	public static class HealWord implements IWord {
		@Override
		public void onUse(ScriptInstance script, List selectors) {
			int fatigue = 0;
			for(Object obj : selectors) {
				if(obj instanceof EntityLivingBase) {
					fatigue++;
					EntityLivingBase ent = (EntityLivingBase) obj;
					// Heals selected entities
					ent.setHealth(ent.getHealth()+6);
				}
			}
			script.getEnergy().performMagic(fatigue*20); // TODO: Balance Fatigue
		}
	}
	
	public static class HaltWord implements IWord {
		@Override
		public void onUse(ScriptInstance script, List selectors) {
			int fatigue = 0;
			for(Object obj : selectors) {
				if(obj instanceof EntityLivingBase) {
					fatigue++;
					// Prevents entities from moving
					((EntityLivingBase)obj).addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 200, 99, true, false));
				}
			}
			script.getEnergy().performMagic(fatigue*20); // TODO: Balance Fatigue
		}
	}
	
	public static class RiseWord implements IWord {
		@Override
		public void onUse(ScriptInstance script, List selectors) {
			for(Object obj : selectors) {
				if(obj instanceof BlockPosHit && script.getActualUser() instanceof Entity) {
					BlockPosHit pos = (BlockPosHit) obj;
					World world = ((Entity)script.getActualUser()).worldObj;
					world.setBlockState(pos.pos, Blocks.air.getDefaultState());
					EntityFallingBlock blockEntity = new EntityFallingBlock(world, pos.pos.getX() + 0.5, pos.pos.getY() + 0.5,
							pos.pos.getZ() + 0.5, world.getBlockState(pos.pos));
					world.spawnEntityInWorld(blockEntity);
					blockEntity.setVelocity(0, 100, 0);
					// Causes blocks to fly upwards
				}
			}
			// TODO: Fatigue
		}	
	}
	
	public static class BrightenWord implements IWord {
		@Override
		public void onUse(ScriptInstance script, List selectors) {
			for(Object obj : selectors) {
				if(obj instanceof BlockPosHit && script.getActualUser() instanceof Entity) {
					BlockPosHit pos = (BlockPosHit) obj;
					World world = ((Entity)script.getActualUser()).worldObj;
					// Creates a ghost/werelight
					if (world.getBlockState(pos.pos.offset(pos.side)).getBlock() == Blocks.air) {
						world.setBlockState(pos.pos.offset(pos.side), ModBlocks.ghostLight.getDefaultState());
					}
				}
			}
			// TODO: Fatigue
		}		
	}
}
