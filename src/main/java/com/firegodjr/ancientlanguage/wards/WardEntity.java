package com.firegodjr.ancientlanguage.wards;

import java.util.Collection;
import java.util.List;

import joptsimple.internal.Strings;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

import com.firegodjr.ancientlanguage.ParticleHandler;
import com.firegodjr.ancientlanguage.api.magic.IEnergyProducer;
import com.firegodjr.ancientlanguage.magic.ScriptInstance;
import com.firegodjr.ancientlanguage.utils.NBTUtils;
import com.firegodjr.ancientlanguage.utils.VersionUtils;

public class WardEntity extends TileEntity implements IUpdatePlayerListBox,
		IEnergyProducer {

	private float energy;
	private ScriptInstance script;
	private EnumFacing direction = EnumFacing.UP;

	public WardEntity() {
	}

	public WardEntity(List<String> argsIn, float energy) {
		script = new ScriptInstance(this, ScriptInstance.getStringFrom(argsIn));
		this.energy = energy;
	}

	public WardEntity setEnergy(float energy) {
		this.energy = energy;
		return this;
	}

	public WardEntity setSpell(String spell) {
		this.script = new ScriptInstance(this, spell);
		return this;
	}

	public WardEntity setSpell(Collection<String> spell) {
		this.script = new ScriptInstance(this,
				ScriptInstance.getStringFrom(spell));
		return this;
	}

	public WardEntity setSpell(String[] spell) {
		this.script = new ScriptInstance(this,
				ScriptInstance.getStringFrom(spell));
		return this;
	}

	public WardEntity setDirection(EnumFacing face) {
		this.direction = face;
		return this;
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setString("SpellScript", Strings.join(script.getChant(), " "));
		compound.setFloat("MagicEnergy", energy);
		NBTUtils.convertFacingToTag(compound, this.direction);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.script = new ScriptInstance(this,
				compound.getString("SpellScript"));
		this.energy = compound.getInteger("MagicEnergy");
		this.direction = NBTUtils.convertTagContentToFacing(compound);
	}

	@Override
	public void update() {
		VersionUtils.spawnParticle(worldObj, VersionUtils.createVec3(pos)
				.addVector(Math.random(), Math.random(), Math.random()),
				VersionUtils.createVec3(0, 0, 0), "enchantment_table");

		ParticleHandler.ghostLight(VersionUtils.createVec3(this.pos), 5,
				this.worldObj);

		AxisAlignedBB bb = VersionUtils.getAABBFor(this.pos, this.pos);

		if (!this.worldObj.getEntitiesWithinAABB(EntityLiving.class, bb)
				.isEmpty()) {
			this.script.onExecute(this.worldObj, new Vec3(this.pos.getX(),
					this.pos.getY(), this.pos.getZ()));
		}
	}

	@Override
	public float useMagic(float energyToPull) {
		float result = this.energy;
		if (energyToPull > this.energy) {
			this.energy = 0;
		} else {
			this.energy -= energyToPull;
			result = energyToPull;
		}
		return result;
	}

}
