package com.firegodjr.ancientlanguage.api.script;

import java.util.List;

import com.firegodjr.ancientlanguage.magic.ScriptInstance;

import net.minecraft.util.Vec3;
import net.minecraft.world.World;

/**
 * An interface for selecting objects
 */
public interface ISelector extends IScriptObject {

	/**
	 * Retrieves selected objects from script
	 * 
	 * @param script
	 *            Executing script instance
	 * @param world
	 *            World executed in
	 * @param position
	 *            Position executed at
	 * 
	 * @return All selected entities, or null for to designate nothing
	 */
	public List<?> getSelected(ScriptInstance script, World world, Vec3 position);

}
