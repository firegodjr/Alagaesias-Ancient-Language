package com.firegodjr.ancientlanguage.wards;

import java.util.Collection;
import java.util.List;

import com.firegodjr.ancientlanguage.ParticleHandler;
import com.firegodjr.ancientlanguage.api.magic.IEnergyProducer;
import com.firegodjr.ancientlanguage.magic.ScriptInstance;
import com.firegodjr.ancientlanguage.utils.NBTUtils;

import joptsimple.internal.Strings;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Vec3;

public class WardEntity extends TileEntity implements IUpdatePlayerListBox, IEnergyProducer {

	private int energy;
	private ScriptInstance script;
	private EnumFacing direction = EnumFacing.UP;

	public WardEntity() {
	}
	
	public WardEntity(List<String> argsIn, int energy) {
		script = new ScriptInstance(this, ScriptInstance.getStringFrom(argsIn));
		this.energy = energy;
	}
	
	public WardEntity setEnergy(int energy) {
		this.energy = energy;
		return this;
	}
	
	public WardEntity setSpell(String spell) {
		this.script = new ScriptInstance(this, spell);
		return this;
	}
	
	public WardEntity setSpell(Collection<String> spell) {
		this.script = new ScriptInstance(this, ScriptInstance.getStringFrom(spell));
		return this;
	}
	
	public WardEntity setSpell(String[] spell) {
		this.script = new ScriptInstance(this, ScriptInstance.getStringFrom(spell));
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
		compound.setInteger("MagicEnergy", energy);
		NBTUtils.convertFacingToTag(compound, this.direction);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.script = new ScriptInstance(this, compound.getString("SpellScript"));
		this.energy = compound.getInteger("MagicEnergy");
		this.direction = NBTUtils.convertTagContentToFacing(compound);
	}

	@Override
	public void update() {
		worldObj.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, this.pos.getX() + Math.random(),
				this.pos.getY() + Math.random(), this.pos.getZ() + Math.random(), 0, 0, 0, 0);
		
		ParticleHandler.ghostLight(this.pos.getX(), this.pos.getY(), this.pos.getZ(), 5, this.worldObj);

		AxisAlignedBB bb = new AxisAlignedBB(this.pos, this.pos);
		
		if (!this.worldObj.getEntitiesWithinAABB(EntityLiving.class, bb).isEmpty()) {
			this.script.onExecute(this.worldObj, new Vec3(this.pos.getX(), this.pos.getY(), this.pos.getZ()));
		}
	}

	@Override
	public int useMagic(int energyToPull) {
		int result = this.energy;
		if (energyToPull > this.energy) {
			this.energy = 0;
		} else {
			this.energy -= energyToPull;
			result = energyToPull;
		}
		return result;
	}

}
