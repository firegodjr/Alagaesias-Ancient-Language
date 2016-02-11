package com.firegodjr.ancientlanguage.magic;

import com.firegodjr.ancientlanguage.api.magic.IEnergyProducer;

public class MagicEnergy {
	
	private int amount;
	
	@SuppressWarnings("unchecked")
	public MagicEnergy(Object producer, int energyPullAmount) {
		if(!Utilities.hasWrapper(producer.getClass())) return;
		this.amount = Utilities.getWrapperFor(producer.getClass()).createProducerFor(producer).addExahustion(energyPullAmount);
	}
	
	public MagicEnergy(IEnergyProducer producer, int energyPullAmount) {
		this.amount = producer.addExahustion(energyPullAmount);
	}
	
	public int getEnergyAmount() {
		return this.amount;
	}
}
