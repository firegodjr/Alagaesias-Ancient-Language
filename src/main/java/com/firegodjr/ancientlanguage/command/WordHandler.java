package com.firegodjr.ancientlanguage.command;

import java.util.ArrayList;
import java.util.List;

import com.firegodjr.ancientlanguage.BlockPosHit;
import com.firegodjr.ancientlanguage.EntListIterated;
import com.firegodjr.ancientlanguage.Main;
import com.firegodjr.ancientlanguage.blocks.ModBlocks;
import com.firegodjr.ancientlanguage.wards.Ward;
import com.firegodjr.ancientlanguage.wards.WardEntity;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class WordHandler {

	/**
	 * The square radius used for "nearby" targets
	 */
	private final static int NEAR_RADIUS = 4;
	private final static int RAY_TRACE_RADIUS = 3;
	/**
	 * The square radius a spell stops iterating at
	 */
	private final static int MAX_ATTACK_RADIUS = 8;

	/**
	 * The starting radius a spell will start iterating at
	 */
	private final static int MIN_ATTACK_RADIUS = 1;

	/**
	 * Array of action words in the Ancient Language
	 */
	public static final String[] actions = { 
			"skolir", // Shield ---- 0
			"naina", // Make Bright 1
			"jierda", // Break ----- 2
			"vergari", // Kill ------ 3
			"brisingr", // Ignite ---- 4
			"heill", // Heal ------ 5
			"blothr", // Stop ------ 6
			"vard", // Ward ------ 7
			"risa" // Rise ------ 8
	};

	/**
	 * Array of EntityLiving targeting words in the Ancient Language
	 */
	public static final String[] livingTargets = { 
			"edtha", // Me --- 0
			"nosu", // us --- 1
			"fricai", // friend 2
			"thorna" // those 3
	};

	/**
	 * Array of Block targeting words in the Ancient Language
	 */
	public static final String[] blockTargets = { 
			"sem", // That - 0
			"stenr" // Stone 1
	};

	/**
	 * Array of Ward targeting words in the Ancient Language
	 */
	public static final String[] wardTargets = { 
			"fjandi", // Enemy - 0
			"fricai", // Friend 1
			"allr" // All --- 2
	};

	public static final String[] wardFromVariants = { 
			"fra", // From - 0
			"wiol" // For -- 1
	};

	/**
	 * Gets the index of an action word if it's stored in String[] "actions".
	 * Returns -1 if index is unavailable.
	 * 
	 * @param word
	 * @return index
	 */
	public static int getActionIndex(String word) {
		int index = -1;
		for (int i = 0; i < actions.length && index == -1; i++) {
			if (word.equalsIgnoreCase(actions[i])) {
				index = i;
			}
		}
		return index;
	}

	public static int getWardTargetIndex(String word) {
		int index = -1;
		for (int i = 0; i < wardTargets.length && index == -1; i++) {
			if (word.equalsIgnoreCase(wardTargets[i])) {
				index = i;
			}
		}
		return index;
	}

	public static int getWardfromIndex(String word) {
		int index = -1;
		for (int i = 0; i < wardFromVariants.length && index == -1; i++) {
			if (word.equalsIgnoreCase(wardFromVariants[i])) {
				index = i;
			}
		}
		return index;
	}

	public static void placeWard(List<String> args, String target, int charge, BlockPos blockpos) {
		ScriptHandler scriptHandler = new ScriptHandler();
		String[] wardArgs = new String[args.size()];
		BlockPosHit pos = new BlockPosHit(new BlockPos(0, 0, 0), EnumFacing.UP);
		for (int i = 0; i < args.size(); i++) {
			String word = args.get(i);
			if (word.contains(":")) {
				wardArgs[i] = word.replace(':', ' ').trim();
				for (int j = i + 1; j < args.size(); j++) {
					wardArgs[j] = args.get(j);
				}
				break;
			}
		}
		Ward ward = new Ward("wardblock");
		ward.args = args;
		ward.target = target;
		ward.charge = charge;
		Minecraft.getMinecraft().theWorld.setBlockState(blockpos, ward.getDefaultState());
	}

	/**
	 * Gets whether or not "word" is the type of specified "wordType"
	 * 
	 * @param word
	 * @param wordType
	 * @return
	 */
	public static boolean isWord(String word, String wordType) {
		boolean success = false;
		if (wordType.equals("action")) {
			if (getActionIndex(word) != -1) {
				success = true;
			}
		} else if (wordType.equals("livingtarget")) {
			if (getLivingTargetIndex(word) != -1) {
				success = true;
			}
		} else if (wordType.equals("blocktarget")) {
			if (getBlockTargetIndex(word) != -1) {
				success = true;
			}
		} else if (wordType.equals("wardtarget")) {
			if (getWardTargetIndex(word) != -1) {
				success = true;
			}
		} else if (wordType.equals("from")) {
			if (getWardfromIndex(word) != -1) {
				success = true;
			}
		} else if (wordType.equals("any")) {
			if (getActionIndex(word) != -1 || getLivingTargetIndex(word) != -1 || getBlockTargetIndex(word) != -1) {
				success = true;
			}
		}
		return success;
	}

	/**
	 * Gets the index of an action word if it's stored in String[]
	 * "livingTargets". Returns -1 if index is unavailable.
	 * 
	 * @param word
	 * @return index
	 */
	public static int getLivingTargetIndex(String word) {
		int index = -1;

		for (int i = 0; i < livingTargets.length && index == -1; i++) {
			if (word.equalsIgnoreCase(livingTargets[i])) {
				index = i;
			}
		}
		return index;
	}

	/**
	 * Gets the index of an action word if it's stored in String[]
	 * "blockTargets". Returns -1 if index is unavailable.
	 * 
	 * @param word
	 * @return index
	 */
	public static int getBlockTargetIndex(String word) {
		int index = -1;

		for (int i = 0; i < blockTargets.length && index == -1; i++) {
			if (word.equalsIgnoreCase(blockTargets[i])) {
				index = i;
			}
		}

		return index;
	}

	/**
	 * Gets block targets and returns a List of BlockPos
	 * 
	 * @param word
	 * @param player
	 * @return List BlockPos
	 */
	public static List<BlockPosHit> getBlockTargetsFromWord(String word, ICommandSender sender) {
		List<BlockPosHit> blockList = new ArrayList();
		int wordIndex = getLivingTargetIndex(word);
		World world = sender.getEntityWorld();

		if (wordIndex == -1)
			wordIndex = getBlockTargetIndex(word);

		switch (wordIndex) {
		case 0: // sem, or that

			if (sender instanceof EntityPlayer) {
				MovingObjectPosition pos;
				pos = ((EntityPlayer) sender).rayTrace(64, 1.0f);
				blockList.add(new BlockPosHit(pos.getBlockPos(), pos.sideHit));
			} else if (sender instanceof WardEntity) {
				MovingObjectPosition pos = new MovingObjectPosition(MovingObjectType.BLOCK, new Vec3(0, 0, 0),
						EnumFacing.UP, ((WardEntity) sender).getPos());
			}
			break;

		case 1: // stenr, or stone
			blockList.addAll(GetSpecificBlockTargets(sender, Blocks.stone));
			break;
		}

		return blockList;
	}

	/**
	 * Returns a list of blocks of the specified type based on the sender.
	 * 
	 * @param sender
	 * @param block
	 * @return
	 */
	public static List GetSpecificBlockTargets(ICommandSender sender, Block block) {
		List blockList = new ArrayList();
		if (sender instanceof EntityPlayer) {
			MovingObjectPosition pos = ((EntityPlayer) sender).rayTrace(32, 1.0f);
			blockList.addAll(getBlocks(block, 2, pos.getBlockPos()));
		} else
			blockList.addAll(getBlocks(block, 2, sender.getPosition()));
		return blockList;
	}

	/**
	 * Gets the targets defined by a word of power
	 * 
	 * @param word
	 * @return EntListIterated
	 */
	public static EntListIterated getLivingTargetsFromWord(String word, ICommandSender sender) {
		EntListIterated entList = new EntListIterated();
		int wordIndex = getLivingTargetIndex(word);
		World world = sender.getEntityWorld();

		/*
		 * Find the target entity to apply an action to Can only be a pronoun
		 * from the ancient language Default is "edtha" or "me", which applies
		 * to the sender
		 */
		if (!world.isRemote) {
			switch (wordIndex) {
			case 0: // edtha, or me
				if (sender instanceof EntityPlayer)
					entList.add((EntityPlayer) sender);
				break;				
			case 1: //nosu, or us
				for(int i = 0; entList.isEmpty(); i++)
				{
					Main.getLogger().info("Nosu is iterating: " + i);
					entList.setList(findEntitiesWithinRadius(sender, NEAR_RADIUS + i -1));
				}
				if (sender instanceof EntityPlayer)
					entList.add((EntityPlayer) sender);
				break;

			case 2: // fricai, or friend
				entList = findNearestPlayer(sender, MIN_ATTACK_RADIUS, NEAR_RADIUS);
				break;

			case 3: // thorna, or those
				entList = findNearest(sender, MIN_ATTACK_RADIUS, MAX_ATTACK_RADIUS);
				break;
			}
		}

		return entList;
	}

	/**
	 * Performs an action against the passed in EntityLivingBase reference, then
	 * calls tirePlayer(player, (calculated fatigue), distance)
	 * 
	 * @param sender
	 * @param actionWord
	 * @param targetList
	 * @param distance
	 */
	public static void performEntityAction(ICommandSender sender, String actionWord, List<EntityLivingBase> targetList, int distance) {
		Main.getLogger().info("Performing action \"" + actionWord + "\" against targets: " + targetList);
		int action = getActionIndex(actionWord);
		float fatigue = 0;
		if (!sender.getEntityWorld().isRemote)
			switch (action) // perform a different action for each action word
			{

			case 0: // skolir, or shield
				for (int i = 0; i < targetList.size(); i++) {
					fatigue = i;
					targetList.get(i).addPotionEffect(new PotionEffect(Potion.resistance.id, 1200, 2, true, false));
				}
				tirePlayer(sender, fatigue, distance);
				break;

			case 1: // naina, or make bright
				break;

			case 2: // jierda, or break
				for (int i = 0; i < targetList.size(); i++) {
					fatigue = i;
					targetList.get(i).attackEntityFrom(DamageSource.magic, 5f);
				}
				break;

			case 3: // vergari, or kill
				for (int i = 0; i < targetList.size(); i++) {
					fatigue = i * 2;
					if (targetList.get(i).getHealth() <= 30) {
						targetList.get(i).setHealth(0.1f);
						targetList.get(i).attackEntityFrom(DamageSource.magic, 0.2f);
					} else {
						if (sender instanceof EntityPlayer) {
							EntityPlayer player = (EntityPlayer) sender;
							player.attackEntityFrom(DamageSource.magic, 0.1f);
							player.addChatComponentMessage(
									new ChatComponentText(EnumChatFormatting.GRAY + "" + EnumChatFormatting.ITALIC
											+ targetList.get(i).getName() + " is too powerful to kill with magic!"));
						}
					}
					Main.getLogger().info(sender.getName() + " cast vergari on target: " + targetList.get(i));
				}
			}
			tirePlayer(sender, fatigue, distance);
			break;
			
		case 4: //brisingr, or ignite
			for(int i = 0; i < targetList.size(); i++)
			{
				fatigue = i;
				targetList.get(i).setFire(10);
			}
			tirePlayer(sender, fatigue, distance);
			break;
			
		case 5: //heill, or heal
			for(int i = 0; i < targetList.size(); i++)
			{
				fatigue = i;
				targetList.get(i).setHealth(targetList.get(i).getHealth() + 6);
			}
			tirePlayer(sender, fatigue, distance);
			System.out.println("Player is tired by a factor of " + fatigue);
			break;
		
		case 6: //blothr, or halt
			for(int i = 0; i < targetList.size(); i++)
			{
				fatigue = i;
				targetList.get(i).addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 200, 99, true, false));
			}
		tirePlayer(sender, fatigue, distance);
		for (EntityLivingBase ent : targetList) {
			if (ent == sender) {

			} else if (sender instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) sender;
				player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.GRAY + ""
						+ EnumChatFormatting.ITALIC + ent.getName() + " flinches as you say the word \""
						+ EnumChatFormatting.RESET + EnumChatFormatting.AQUA + actionWord + EnumChatFormatting.GRAY + ""
						+ EnumChatFormatting.ITALIC + "\"."));
			}
			if (ent instanceof EntityPlayer) {
				((EntityPlayer) ent).addChatComponentMessage(new ChatComponentText(EnumChatFormatting.GRAY + ""
						+ EnumChatFormatting.ITALIC + "The word \"" + EnumChatFormatting.RESET + EnumChatFormatting.AQUA
						+ actionWord + EnumChatFormatting.GRAY + "" + EnumChatFormatting.ITALIC
						+ "\" echoes in your mind..."));
			}
		}
	}

	/**
	 * Performs an action against the passed in BlockPos's, then calls
	 * tirePlayer
	 * 
	 * @param sender
	 * @param word
	 * @param blockTargets
	 */
	public static void performBlockAction(ICommandSender sender, String actionWord, List<BlockPosHit> blockTargets) {
		Main.getLogger().info("Performing action \"" + actionWord + "\" against targets: " + blockTargets);
		World world = sender.getEntityWorld();
		int distance = 0;
		int action = getActionIndex(actionWord);
		float fatigue = 0;
		if (!sender.getEntityWorld().isRemote)
			switch (action) // perform a different action for each action word
			{
			case 1: // naina, or make bright
				for (int i = 0; i < blockTargets.size(); i++) {
					fatigue = i / 3;
					BlockPosHit pos = blockTargets.get(i);
					if(world.getBlockState(pos.pos.offset(pos.side)).getBlock() == Blocks.air)
					{
						Main.getLogger().info("Placing ghostlight!");
						world.setBlockState(pos.pos.offset(pos.side), ModBlocks.ghostLight.getDefaultState());
					}

				}
				break;
			case 2: // jierda, or break
				for (int i = 0; i < blockTargets.size(); i++) {
					BlockPos pos = blockTargets.get(i).pos;
					fatigue += world.getBlockState(pos).getBlock().getBlockHardness(world, pos) / 4;
					IBlockState state = world.getBlockState(pos);
					world.destroyBlock(pos, false);

				}
				break;

			case 4: // brisingr, or ignite
				for (int i = 0; i < blockTargets.size(); i++) {
					fatigue = i;
					BlockPos pos = blockTargets.get(i).pos;
					IBlockState state = world.getBlockState(pos);
					EnumFacing facing = blockTargets.get(i).side;
					if (world.getBlockState(pos.offset(facing)).getBlock() == Blocks.air)
						world.setBlockState(pos.offset(facing), Blocks.fire.getDefaultState());

				}
				break;
			case 8: // risa, or rise
				for (int i = 0; i < blockTargets.size(); i++) {
					BlockPos pos = blockTargets.get(i).pos;
					IBlockState state = world.getBlockState(pos);
					world.setBlockState(pos, Blocks.air.getDefaultState());
					EntityFallingBlock blockEntity = new EntityFallingBlock(world, pos.getX() + 0.5, pos.getY() + 0.5,
							pos.getZ() + 0.5, state);
					world.spawnEntityInWorld(blockEntity);
					blockEntity.setVelocity(0, 100, 0);
				}
				break;
			}
		tirePlayer(sender, fatigue, distance);
	}

	/**
	 * Finds entities within a certain radius using
	 * player.worldObj.getEntitiesWithinAABB
	 * 
	 * @param sender
	 * @param radius
	 * @return
	 */
	private static List findEntitiesWithinRadius(ICommandSender sender, int radius) {
		List entList = new ArrayList();
		entList = sender.getEntityWorld().getEntitiesWithinAABB(EntityLiving.class,
				new AxisAlignedBB(sender.getPosition().add(-radius, -(int) radius / 3, -radius),
						sender.getPosition().add(radius, (int) radius / 3, radius)));
		return entList;
	}

	/**
	 * Finds the nearest entities that extend EntityLiving by iterating from
	 * startRadius to maxRadius
	 * 
	 * @param sender
	 * @param startRadius
	 * @param maxRadius
	 * @return A List of entities
	 */
	private static EntListIterated findNearest(ICommandSender sender, int startRadius, int maxRadius) {
		EntListIterated entList = new EntListIterated();
		for(int i = 0; entList.isEmpty() && i <= maxRadius; i++) {
			Main.getLogger().info("Finding entities, iterating: " + i);
			entList.setList(findEntitiesWithinRadius(sender, startRadius + i));
		}
		return entList;
	}

	/**
	 * Finds the nearest EntityPlayer by iterating from startRadius to maxRadius
	 * 
	 * @param sender
	 * @param startRadius
	 * @param maxRadius
	 * @return An EntListIterated, containing only the first EntityPlayer found
	 *         and the iterations
	 */
	private static EntListIterated findNearestPlayer(ICommandSender sender, int startRadius, int maxRadius) {
		List entList = new ArrayList();
		EntListIterated output = new EntListIterated();
		for(int i = 0; entList.isEmpty() || i <= maxRadius; i++)
		{
			Main.getLogger().info("Finding nearest player, iterating: " + i);
			entList = findEntitiesWithinRadius(sender, startRadius + i);
			output.setIteration(i);
		}
		for (Object e : entList) {
			if (e instanceof EntityPlayer) {
				output.add((EntityLivingBase) e);
				break;
			}
		}
		return output;

	}

	/**
	 * Gets all nearby blocks matching the passed in block, and also returns the
	 * side facing the centerpoint
	 * 
	 * @param block
	 * @param radius
	 * @param centerpoint
	 * @return ArrayList of BlockPosHit
	 */
	private static List getBlocks(Block block, int radius, BlockPos centerpoint) {
		// TODO: Fix the facing code, it's broken. (now commented out)
		List out = new ArrayList();
		World world = Minecraft.getMinecraft().theWorld;
		int trueX = 0;
		int trueY = 0;
		int trueZ = 0;
		int relX = 0;
		int relY = 0;
		int relZ = 0;
		int absX = 0;
		int absY = 0;
		int absZ = 0;
		for (int x = 0; x < radius * 2 + 1; x++) {
			relX = x - radius;
			trueX = relX + centerpoint.getX();
			for (int y = 0; y < radius * 2 + 1; y++) {
				relY = y - radius;
				trueY = relY + centerpoint.getY();
				for (int z = 0; z < radius * 2 + 1; z++) {
					relZ = z - radius;
					trueZ = relZ + centerpoint.getZ();
					Main.getLogger().info("Checking " + trueX + ", " + trueY + ", " + trueZ + " for blocks matching " + block.getLocalizedName());
					if(world.getBlockState(new BlockPos(trueX, trueY, trueZ)).getBlock() == block)
					{
						absX = Math.abs(relX);
						absY = Math.abs(relY);
						absZ = Math.abs(relZ);
						EnumFacing face = EnumFacing.UP;
//						if(absX > absY)
//						{
//							if(absX > absZ)
//								face = relX > 0 ? EnumFacing.WEST : EnumFacing.EAST;
//						}
//						else if(absY > absZ)
//						{
//							if(absY > absX)
//								face = relY > 0 ? EnumFacing.DOWN : EnumFacing.UP;
//						}
//						else if(absZ > absX)
//						{
//							if(absZ > absY)
//								face = relZ > 0 ? EnumFacing.NORTH : EnumFacing.SOUTH;
//						}
						Main.getLogger().info("   -found matching block and calculated facing side");
						t.add(new BlockPosHit(new BlockPos(trueX, trueY, trueZ), face));
					}
				}
			}
		}
		return out;
	}

	/**
	 * Applies a hunger potion effect with a potency based on the amount of
	 * fatigue passed in; If hunger isn't full, damages the player with damage =
	 * 1/2 heart * fatigue + empty hunger drumsticks
	 * 
	 * @param sender
	 * @param fatigue
	 * @param distance
	 */
	private static void tirePlayer(ICommandSender sender, float fatigue, float distance) {
		if (sender instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender;
			if (!player.capabilities.isCreativeMode)
				if (player.getFoodStats().getFoodLevel() < 20) {
					player.setHealth(player.getHealth() + (player.getFoodStats().getFoodLevel() - 20) / 2);
				}

			if (player.isPotionActive(Potion.hunger.id)) {
				int duration = player.getActivePotionEffect(Potion.hunger).getDuration();
				player.addPotionEffect(new PotionEffect(Potion.hunger.id, 5,
						duration + 30 + 10 * (int) fatigue + 5 * (int) distance, true, false));
			} else {
				player.addPotionEffect(new PotionEffect(Potion.hunger.id, 10 + 10 * (int) fatigue,
						30 + 5 * (int) distance, true, false));
			}

			if (player.isPotionActive(Potion.moveSlowdown.id)) {
				int duration = player.getActivePotionEffect(Potion.moveSlowdown).getDuration();
				player.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 1 + (int) (fatigue / 2),
						duration + 20 + 20 * (int) distance));
			} else {
				player.addPotionEffect(
						new PotionEffect(Potion.moveSlowdown.id, 1 + (int) (fatigue / 2), 20 + 20 * (int) distance));
			}
		} else if (sender instanceof WardEntity) {

		}
	}
}