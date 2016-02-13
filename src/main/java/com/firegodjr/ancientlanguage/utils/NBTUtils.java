package com.firegodjr.ancientlanguage.utils;

import java.util.Locale;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public class NBTUtils {

	public static NBTTagCompound convertFacingToTag(NBTTagCompound tag, EnumFacing face) {
		if(face == null || tag == null) return new NBTTagCompound();
		tag.setString("EnumFacing", face.toString().toUpperCase(Locale.US));
		return tag;
	}
	
	public static EnumFacing convertTagContentToFacing(NBTTagCompound tag) {
		if(tag == null || !tag.hasKey("EnumFacing")) return null;
		return EnumFacing.valueOf(tag.getString("EnumFacing"));
	}
	
}
