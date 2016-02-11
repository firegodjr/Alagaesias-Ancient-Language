package com.firegodjr.ancientlanguage.magic;

import java.util.HashMap;
import java.util.Map;

import com.firegodjr.ancientlanguage.api.magic.IEnergyWrapper;
import com.firegodjr.ancientlanguage.api.script.IWord;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.FoodStats;

public final class Utilities {

	public static final float ENERGY_DAMAGE_DIVIDER = 20F;
	public static final float ENERGY_DRAIN_DIVIDER = 5F;

	@SuppressWarnings("rawtypes")
	private static final Map<Class, IEnergyWrapper> classToWrapper = new HashMap<Class, IEnergyWrapper>();
	
	private static final Map<String, IWord> wordToInterface = new HashMap<String, IWord>();
	
	@SuppressWarnings("rawtypes")
	public static void registerEnergyWrapper(Class clazz, IEnergyWrapper wrapper) {
		classToWrapper.put(clazz, wrapper);
	}
	
	@SuppressWarnings("rawtypes")
	public static IEnergyWrapper getWrapperFor(Class clazz) {
		return classToWrapper.get(clazz);
	}
	
	@SuppressWarnings("rawtypes")
	public static boolean hasWrapper(Class clazz) {
		return classToWrapper.containsKey(clazz);
	}
	
	public static void initalizeWrappers() {
		registerEnergyWrapper(Entity.class, new EntityEnergyWrapper());
		registerEnergyWrapper(EntityPlayer.class, new EntityPlayerEnergyWrapper());
	}
	
	public static void registerWord(String str, IWord word) {
		wordToInterface.put(str, word);
	}
	
	public static IWord getWord(String str) {
		return wordToInterface.get(str);
	}
	public static class EntityEnergyWrapper implements IEnergyWrapper<Entity> {
		@Override
		public IProducerWrapper<Entity> createProducerFor(Entity entity) {
			return new IProducerWrapper<Entity>() {
				@Override
				public int addExahustion(int energyExahust) {
					entity.attackEntityFrom(DamageSource.magic, energyExahust/ENERGY_DAMAGE_DIVIDER);
					return energyExahust;
				}

				@Override
				public Entity getRepresent() {
					return entity;
				}};
		}
	}
	
	public static class EntityPlayerEnergyWrapper implements IEnergyWrapper<EntityPlayer> {
		@Override
		public IProducerWrapper<EntityPlayer> createProducerFor(EntityPlayer player) {
			return new IProducerWrapper<EntityPlayer>() {
				@Override
				public int addExahustion(int energyExahust) {
					FoodStats foodStats = player.getFoodStats();
					if(foodStats.getFoodLevel() != 0) {
						foodStats.addExhaustion(energyExahust/ENERGY_DRAIN_DIVIDER);
					} else player.attackEntityFrom(DamageSource.magic, energyExahust/ENERGY_DAMAGE_DIVIDER);
					return energyExahust;
				}

				@Override
				public EntityPlayer getRepresent() {
					return player;
				}};
		}
		
	}
	
}
