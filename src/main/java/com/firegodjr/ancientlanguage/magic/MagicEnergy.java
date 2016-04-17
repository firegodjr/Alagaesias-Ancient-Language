package com.firegodjr.ancientlanguage.magic;

import com.firegodjr.ancientlanguage.api.magic.IEnergyProducer;

/**
 * Class for storing energy and performing magic using energy stores
 */
public class MagicEnergy {

	private IEnergyProducer producer;
	private Object actualUser;
	private boolean auIsSet;

	public MagicEnergy(IEnergyProducer producer, Object user) {
		this.producer = producer;
		this.actualUser = user;
		this.auIsSet = true;
	}

	public MagicEnergy(IEnergyProducer producer) {
		this(producer, producer);
		this.auIsSet = false;
	}

	public int performMagic(int requestedEnergy) {
		return this.producer.useMagic(requestedEnergy);
	}

	public Object getActualUser() {
		return this.actualUser;
	}

	public MagicEnergy setActualUser(Object user) {
		if (this.auIsSet)
			return this;
		this.actualUser = user;
		this.auIsSet = true;
		return this;
	}
}
