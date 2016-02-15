package com.firegodjr.ancientlanguage.api.magic;

/**
 * An interface for objects implementing magical energy production
 */
public interface IEnergyProducer {
	
	/**
	 * Handles magic use
	 * 
	 * @param energyToPull
	 *            The amount of energy to pull
	 * @return The amount of energy retrieved from energy request
	 */
	public int useMagic(int energyToPull);
	
}
