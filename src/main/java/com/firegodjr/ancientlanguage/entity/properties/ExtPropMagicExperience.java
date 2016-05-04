package com.firegodjr.ancientlanguage.entity.properties;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

import java.text.NumberFormat;

public final class ExtPropMagicExperience implements IExtendedEntityProperties {

	public static final String NAME = "ExtendedPropMagicExp";

	public static final String EXP_NBT = "Experience";
	public static final String CURRENTLEVEL_NBT = "CurrentLevel";

	protected Entity entity;

	private float exp = 0.0f;
	private int currentLevel = 1;

	@Override
	public void saveNBTData(NBTTagCompound compound) {
		compound.setFloat(EXP_NBT, this.getExperience());
		compound.setInteger(CURRENTLEVEL_NBT, this.getLevel());
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		this.setExperience(compound.getFloat(EXP_NBT));
		this.setLevel(compound.getInteger(CURRENTLEVEL_NBT));
	}

	@Override
	public void init(Entity entity, World world) {
		this.entity = entity;
	}

	public static ExtPropMagicExperience getExtProp(Entity ent) {
		if (ent == null)
			return null;
		return (ExtPropMagicExperience) ent.getExtendedProperties(NAME);
	}

	/**
	 * Adds to the experience level of {@link #entity}
	 * 
	 * @param exp
	 *            The experience to add
	 */
	public void addExperience(float exp) {
		this.exp += exp;
		while (this.exp > 1) {
			this.currentLevel++;
			this.exp -= 1;
		}
		if(this.exp == -0) { // Prevent negative zero, it just looks weird later and adds random sign for no reason
			this.exp = 0;
			return;
		}
		while (this.exp < 0) {
			this.currentLevel--;
			this.exp += 1;
		}
	}

	/**
	 * Sets the experience level of {@link #entity}
	 * 
	 * @param exp
	 *            The experience to set
	 */
	public void setExperience(float exp) {
		this.exp = exp;
		while (this.exp > 1) {
			this.currentLevel++;
			this.exp -= 1;
		}
		if(this.exp == -0) { // Prevent negative zero, it just looks weird later and adds random sign for no reason
			this.exp = 0;
			return;
		}
		while (this.exp < 0) {
			this.currentLevel--;
			this.exp += 1;
		}
	}

	public void setLevel(int level) {
		this.currentLevel = Math.max(1, level);
	}

	/**
	 * Retrieves {@link #entity}'s current level
	 */
	public int getLevel() {
		return this.currentLevel;
	}

	/**
	 * Retrieves {@link #entity}'s current experience
	 */
	public float getExperience() {
		return this.exp;
	}

	/**
	 * Retrieves {@link #entity}'s current experience in percentage form
	 */
	public String getExperiencePercent() {
		return NumberFormat.getPercentInstance().format(this.getExperience());
	}
}
