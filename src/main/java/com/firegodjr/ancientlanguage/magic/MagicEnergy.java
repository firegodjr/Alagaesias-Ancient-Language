package com.firegodjr.ancientlanguage.magic;

import com.firegodjr.ancientlanguage.api.magic.IEnergyProducer;

/**
 * Class for storing energy and performing magic using energy stores
 */
public class MagicEnergy {

	private IEnergyProducer producer;

	public MagicEnergy(IEnergyProducer producer) {
		this.producer = producer;
	}

	public int performMagic(int requestedEnergy) {
		return this.producer.useMagic(requestedEnergy);
	}
}
