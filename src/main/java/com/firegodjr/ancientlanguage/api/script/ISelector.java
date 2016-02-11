package com.firegodjr.ancientlanguage.api.script;

import java.util.List;

import com.firegodjr.ancientlanguage.magic.ScriptInstance;

import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public interface ISelector extends IWord {

	public List<?> getSelected(ScriptInstance script, World world, Vec3 position);
	
}
