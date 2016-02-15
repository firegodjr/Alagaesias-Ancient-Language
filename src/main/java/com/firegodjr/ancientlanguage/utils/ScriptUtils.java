package com.firegodjr.ancientlanguage.utils;

import com.firegodjr.ancientlanguage.Main;
import com.firegodjr.ancientlanguage.api.magic.IEnergyProducer;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.FoodStats;

/**
 * A Script Utility Helper Class
 */
public class ScriptUtils {

	public static final float ENERGY_DAMAGE_DIVIDER = 20F;
	public static final float ENERGY_DRAIN_DIVIDER = 5F;

	/**
	 * Creates an IEnergyProducer wrapper for an entity
	 * 
	 * @param entity
	 *            The entity to create a wrapper for
	 */
	public static IEnergyProducer createProducerFor(final Entity entity) {
		Main.getLogger().info("Creating Producer for Entity: " + entity);
		return new IEnergyProducer() {
			@Override
			public int useMagic(int energyToPull) {
				entity.attackEntityFrom(DamageSource.magic, energyToPull / ENERGY_DAMAGE_DIVIDER);
				return energyToPull;
			}
		};
	}

	/**
	 * Creates an IEnergyProducer wrapper for a player
	 * 
	 * @param player
	 *            The player to create a wrapper for
	 */
	public static IEnergyProducer createProducerFor(final EntityPlayer player) {
		Main.getLogger().info("Creating Producer for Player: " + player);
		return new IEnergyProducer() {
			@Override
			public int useMagic(int energyToPull) {
				FoodStats foodStats = player.getFoodStats();
				if (foodStats.getFoodLevel() != 0) {
					foodStats.addExhaustion(energyToPull / ENERGY_DRAIN_DIVIDER);
				} else
					player.attackEntityFrom(DamageSource.magic, energyToPull / ENERGY_DAMAGE_DIVIDER);
				return energyToPull;
			}
		};
	}
}
