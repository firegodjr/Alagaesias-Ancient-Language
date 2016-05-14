package test.firegodjr.ancientlanguage;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.firegodjr.ancientlanguage.api.script.IScriptObject;
import com.firegodjr.ancientlanguage.api.script.events.ScriptEvent;

public class TypeActiveEventTest {

	private static final boolean ENABLE = false;

	public static void init() {
		if (ENABLE)
			MinecraftForge.EVENT_BUS.register(new TypeActiveEventTest());
	}

	@SubscribeEvent
	public void onScriptTypeActive(ScriptEvent.TypeActive<IScriptObject> event) {
		System.out.println(event.getString());
		event.setCanceled(true);
	}
}