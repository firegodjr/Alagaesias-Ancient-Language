package test.firegodjr.ancientlanguage;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.firegodjr.ancientlanguage.api.script.events.ScriptEvent;

public class ScriptEndEventTest {

	private static final boolean ENABLE = false;

	public static void init() {
		if (ENABLE)
			MinecraftForge.EVENT_BUS.register(new ScriptEndEventTest());
	}

	@SubscribeEvent
	public void onScriptEnd(ScriptEvent.End event) {
		System.out.println(event.getScriptInstance().toString());
	}
}