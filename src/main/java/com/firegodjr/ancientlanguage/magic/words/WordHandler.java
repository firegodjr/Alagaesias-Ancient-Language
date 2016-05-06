package com.firegodjr.ancientlanguage.magic.words;

import java.util.List;

import com.firegodjr.ancientlanguage.Main;
import com.firegodjr.ancientlanguage.api.script.IModifier;
import com.firegodjr.ancientlanguage.api.script.IScriptObject;
import com.firegodjr.ancientlanguage.magic.MagicData;
import com.firegodjr.ancientlanguage.magic.ScriptRegistry;
import com.firegodjr.ancientlanguage.utils.ScriptData;
import com.firegodjr.ancientlanguage.wards.WardBlock;

import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class WordHandler {

	public static void initalizeWords() {
		Main.getLogger().info("Intialize Words");
		/// Actions ///
		// Adds resistance
		ScriptRegistry.registerWord("skolir", new WordAction.ShieldWord());
		// Brightens target blocks with werelight
		ScriptRegistry.registerWord("naina", new WordAction.BrightenWord());
		// Breaks target blocks or harms targeted entities
		ScriptRegistry.registerWord("jierda", new WordAction.BreakWord());
		// Kills target entities under 30 health
		ScriptRegistry.registerWord("vergari", new WordAction.KillWord());
		// Ignites target
		ScriptRegistry.registerWord("brisingr", new WordAction.FireWord());
		// Heals targeted entities
		ScriptRegistry.registerWord("heill", new WordAction.HealWord());
		// Prevents targeted entities from moving
		ScriptRegistry.registerWord("blothr", new WordAction.HaltWord());
		// Throws target blocks upwards
		ScriptRegistry.registerWord("risa", new WordAction.RiseWord());

		/// Selectors ///
		// Selects user
		ScriptRegistry.registerWord("edtha", new WordSelector.MeSelector());
		// Selects surrounding entities (may include user)
		ScriptRegistry.registerWord("nosu", new WordSelector.UsSelector());
		ScriptRegistry.registerWord("allr", new WordSelector.UsSelector());
		// Selects closest player within a 4 block radius
		ScriptRegistry.registerWord("fricai", new WordSelector.FriendSelector());
		// Selects surrounding entities that aren't players
		ScriptRegistry.registerWord("thorna", new WordSelector.ThoseSelector());
		ScriptRegistry.registerWord("fjandi", new WordSelector.ThoseSelector());
		// Selects target block
		ScriptRegistry.registerWord("sem", new WordSelector.ThatSelector());
		// Selects random stone blocks surrounding fired position
		ScriptRegistry.registerWord("stenr", new WordSelector.StoneSelector());

		//Semi-colon seperator
		ScriptRegistry.registerWord(";", new IModifier() {
			@Override
			public boolean modifyWord(MagicData energy, ScriptData data, List<IScriptObject> objects) {
				return true;
			}});
	}

	public static void placeWard(World world, Vec3 target, List<String> args, int charge) {
		world.setBlockState(new BlockPos(target), new WardBlock(args, charge).getDefaultState());
	}
}
