package com.firegodjr.ancientlanguage.api.magic;

/**
 * An interface for objects implementing magical energy production
 */
public interface IEnergyProducer {

	/**
	 * Handles magic use
	 *
	 * @param energyToPull
	 *            The percentage of energy to pull
	 *
	 * @return The percentage of asked energy retrieved from energy request
	 */
	public float useMagic(float energyToPull);

}
