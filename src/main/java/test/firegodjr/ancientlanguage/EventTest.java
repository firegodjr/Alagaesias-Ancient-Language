package test.firegodjr.ancientlanguage;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import com.firegodjr.ancientlanguage.Main;

@Mod(modid = Main.MODID + "eventtest", name = Main.MODNAME + " EventTest")
public class EventTest {

	private static boolean ENABLE = false;
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		if(ENABLE) {
			PreParseEventTest.init();
			PostParseEventTest.init();
			ScriptEndEventTest.init();
			TypeActiveEventTest.init();
			SpecificTypeActiveEventTest.init();
		}
	}
	
}
