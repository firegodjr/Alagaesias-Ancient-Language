package com.firegodjr.ancientlanguage.api.script;

import java.util.List;
import java.util.Map;

import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import com.firegodjr.ancientlanguage.magic.MagicEnergy;

/**
 * An interface for selecting objects
 */
public interface ISelector extends IScriptObject {

	/**
	 * Retrieves selected objects from script
	 * 
	 * @param energy
	 *            The script object energy data
	 * @param modData
	 *            The specific immutable data for this {@link IScriptObject}
	 * @param world
	 *            World executed in
	 * @param position
	 *            Position executed at
	 * 
	 * @return All selected entities, or null for to designate nothing
	 */
	public List<?> getSelected(MagicEnergy energy, Map<String, String> modData, World world, Vec3 position);

}
