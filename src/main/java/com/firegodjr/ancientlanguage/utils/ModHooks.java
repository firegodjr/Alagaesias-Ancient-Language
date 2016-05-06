package com.firegodjr.ancientlanguage.utils;

import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.List;

import net.minecraftforge.common.MinecraftForge;

import com.firegodjr.ancientlanguage.api.script.IScriptObject;
import com.firegodjr.ancientlanguage.api.script.events.ScriptEvent.*;
import com.firegodjr.ancientlanguage.magic.ScriptInstance;

public class ModHooks {

	public static boolean onPreParse(ScriptInstance instance, String chant) {
		PreParse event = new PreParse(instance, Arrays.asList(chant.split(" ")));
		return MinecraftForge.EVENT_BUS.post(event);
	}
	
	public static boolean onPostParse(ScriptInstance instance, String chant, List<IScriptObject> objs) {
		PostParse event = new PostParse(instance, Arrays.asList(chant.split(" ")), objs);
		return MinecraftForge.EVENT_BUS.post(event);
	}
	
	public static void onScriptEnd(ScriptInstance instance, String chant, List<IScriptObject> objs) {
		End event = new End(instance, Arrays.asList(chant.split(" ")), objs);
		MinecraftForge.EVENT_BUS.post(event);
	}
	
	public static <T extends IScriptObject> boolean onTypeActivation(ScriptInstance instance, String chant, List<IScriptObject> objs, T object, String word) {
		TypeActive<T> event = new TypeActive<T>(instance, Arrays.asList(chant.split(" ")), objs, new SimpleEntry<String, T>(word, object));
		return MinecraftForge.EVENT_BUS.post(event);
	}
}
