package com.firegodjr.ancientlanguage.magic;

import java.util.HashMap;
import java.util.Map;

import com.firegodjr.ancientlanguage.Main;
import com.firegodjr.ancientlanguage.api.magic.IEnergyProducer;
import com.firegodjr.ancientlanguage.api.script.IScriptObject;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.FoodStats;

public final class Utilities {

	public static final float ENERGY_DAMAGE_DIVIDER = 20F;
	public static final float ENERGY_DRAIN_DIVIDER = 5F;

	private static final Map<String, IScriptObject> wordToInterface = new HashMap<String, IScriptObject>();
	
	public static void registerWord(String str, IScriptObject word) {
		wordToInterface.put(str, word);
	}
	
	public static IScriptObject getWord(String str) {
		Main.getLogger().info("Retieving Script Object Interface from word '"+str+"'");
		return wordToInterface.get(str);
	}
	
	public static IEnergyProducer createProducerFor(final Entity entity) {
		Main.getLogger().info("Creating Producer for Entity: "+entity);
		return new IEnergyProducer() {
			@Override
			public int useMagic(int energyToPull) {
				entity.attackEntityFrom(DamageSource.magic, energyToPull/ENERGY_DAMAGE_DIVIDER);
				return energyToPull;
			}};
	}
	
	public static IEnergyProducer createProducerFor(final EntityPlayer player) {
		Main.getLogger().info("Creating Producer for Player: "+player);
		return new IEnergyProducer(){
			@Override
			public int useMagic(int energyToPull) {
				FoodStats foodStats = player.getFoodStats();
				if(foodStats.getFoodLevel() != 0) {
					foodStats.addExhaustion(energyToPull/ENERGY_DRAIN_DIVIDER);
				} else player.attackEntityFrom(DamageSource.magic, energyToPull/ENERGY_DAMAGE_DIVIDER);
				return energyToPull;
			}};
	}
}
