package com.firegodjr.ancientlanguage.magic;

import com.firegodjr.ancientlanguage.api.magic.IEnergyProducer;

public class MagicEnergy {
	
	private IEnergyProducer producer;
	
	public MagicEnergy(IEnergyProducer producer) {
		this.producer = producer;
	}
	
	public int performMagic(int requestedEnergy) {
		return this.producer.useMagic(requestedEnergy);
	}
}
