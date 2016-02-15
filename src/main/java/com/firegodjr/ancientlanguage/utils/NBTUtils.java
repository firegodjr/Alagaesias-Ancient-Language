package com.firegodjr.ancientlanguage.utils;

import java.util.Locale;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

/**
 * An NBT Utility Helper Class
 */
public class NBTUtils {

	/**
	 * Converts a EnumFacing to an NBT tag value
	 * 
	 * @param tag
	 *            The tag to add to
	 * @param face
	 *            The facing to convert
	 * @return tag
	 */
	public static NBTTagCompound convertFacingToTag(NBTTagCompound tag, EnumFacing face) {
		if (face == null || tag == null)
			return new NBTTagCompound();
		tag.setString("EnumFacing", face.toString().toUpperCase(Locale.US));
		return tag;
	}

	/**
	 * Converts contents of an NBT tag value to a EnumFacing
	 * 
	 * @param tag
	 *            The tag to retrieve from
	 */
	public static EnumFacing convertTagContentToFacing(NBTTagCompound tag) {
		if (tag == null || !tag.hasKey("EnumFacing"))
			return null;
		return EnumFacing.valueOf(tag.getString("EnumFacing"));
	}

}
