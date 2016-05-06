package test.firegodjr.ancientlanguage;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.firegodjr.ancientlanguage.api.script.IWord;
import com.firegodjr.ancientlanguage.api.script.events.ScriptEvent;
import com.firegodjr.ancientlanguage.magic.words.WordAction.ShieldWord;

public class SpecificTypeActiveEventTest {

	private static final boolean ENABLE = false;

	public static void init() {
		if (ENABLE)
			MinecraftForge.EVENT_BUS.register(new SpecificTypeActiveEventTest());
	}

	@SubscribeEvent
	public void onScriptTypeActive(ScriptEvent.TypeActive<IWord> event) {
		if (!(event.getWordObject() instanceof ShieldWord))
			return;
		System.out.println(event.getString());
		event.setCanceled(true);
	}
}