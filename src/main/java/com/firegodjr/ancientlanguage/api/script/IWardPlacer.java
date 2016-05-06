package com.firegodjr.ancientlanguage.api.script;

import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;

import com.firegodjr.ancientlanguage.magic.MagicData;

/**
 * An interface to handle ward placing words
 */
public interface IWardPlacer extends IScriptObject {

	/**
	 * Creates a ward entity when activated
	 *
	 * @param data
	 *            The script object energy data
	 * @param modData
	 *            The specific immutable data for this {@link IScriptObject}
	 * @param position
	 *            The position to place the entity at
	 * @param scriptToInsert
	 *            The script to insert into the ward entity
	 *
	 * @return The ward entity being placed
	 */
	public Entity onWardPlace(MagicData data, Map<String, String> modData, Vec3 position, String scriptToInsert);

}