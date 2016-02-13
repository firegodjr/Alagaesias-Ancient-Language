package com.firegodjr.ancientlanguage.magic.words;

import java.util.List;

import com.firegodjr.ancientlanguage.Main;
import com.firegodjr.ancientlanguage.magic.Utilities;
import com.firegodjr.ancientlanguage.wards.WardBlock;

import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class WordHandler {

	public static void initalizeWords() {
		Main.getLogger().info("Intialize Words");
		/// Actions ///
		// Adds resistance
		Utilities.registerWord("skolir", new WordAction.ShieldWord());
		// Brightens target blocks with werelight
		Utilities.registerWord("naina", new WordAction.BrightenWord());
		// Breaks target blocks or harms targeted entities
		Utilities.registerWord("jierda", new WordAction.BreakWord());
		// Kills target entities under 30 health
		Utilities.registerWord("vergari", new WordAction.KillWord());
		// Ignites target
		Utilities.registerWord("brisingr", new WordAction.FireWord());
		// Heals targeted entities
		Utilities.registerWord("heill", new WordAction.HealWord());
		// Prevents targeted entities from moving
		Utilities.registerWord("blothr", new WordAction.HaltWord());
		// Throws target blocks upwards
		Utilities.registerWord("risa", new WordAction.RiseWord());
		
		/// Selectors ///
		// Selects user
		Utilities.registerWord("edtha", new WordSelector.MeSelector());
		// Selects surrounding entities (may include user)
		Utilities.registerWord("nosu", new WordSelector.UsSelector());
		Utilities.registerWord("allr", new WordSelector.UsSelector());
		// Selects closest player within a 4 block radius
		Utilities.registerWord("fricai", new WordSelector.FriendSelector());
		// Selects surrounding entities that aren't players
		Utilities.registerWord("thorna", new WordSelector.ThoseSelector());
		Utilities.registerWord("fjandi", new WordSelector.ThoseSelector());
		// Selects target block
		Utilities.registerWord("sem", new WordSelector.ThatSelector());
		// Selects random stone blocks surrounding fired position
		Utilities.registerWord("stenr", new WordSelector.StoneSelector());
	}
	
	public static void placeWard(World world, Vec3 target, List<String> args, int charge) {
		world.setBlockState(new BlockPos(target), new WardBlock(args, charge).getDefaultState());
	}	
}
