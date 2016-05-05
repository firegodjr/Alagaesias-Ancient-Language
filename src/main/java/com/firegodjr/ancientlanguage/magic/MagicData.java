package com.firegodjr.ancientlanguage.magic;

import net.minecraft.util.MathHelper;

import com.firegodjr.ancientlanguage.api.magic.IEnergyProducer;

/**
 * Class for handling magic and energy related data
 */
public class MagicData {

	private IEnergyProducer producer;
	private Object actualUser;
	private boolean auIsSet;
	private float magicMultiplier = 1;

	public MagicData(IEnergyProducer producer, Object user) {
		this.producer = producer;
		this.actualUser = user;
		this.auIsSet = true;
	}

	public MagicData(IEnergyProducer producer, boolean actualUser) {
		this(producer, producer);
		this.auIsSet = actualUser;
	}

	public MagicData(IEnergyProducer producer) {
		this(producer, false);
	}

	public void addMagicMultipler(float multipler) {
		this.magicMultiplier += multipler;
	}

	public float performMagic(float reqEnergy) {
		return this.producer.useMagic(MathHelper.clamp_float(reqEnergy * this.magicMultiplier, 0, 1));
	}

	public float performMagic(int reqEnergy) {
		return this.producer.useMagic(Math.max(reqEnergy * this.magicMultiplier, 1));
	}

	public Object getActualUser() {
		return this.actualUser;
	}

	public MagicData setActualUser(Object user) {
		if (this.auIsSet)
			return this;
		this.actualUser = user;
		this.auIsSet = true;
		return this;
	}
}
