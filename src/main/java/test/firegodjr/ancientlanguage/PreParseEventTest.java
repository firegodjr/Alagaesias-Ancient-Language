package test.firegodjr.ancientlanguage;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.firegodjr.ancientlanguage.Main;
import com.firegodjr.ancientlanguage.event.ScriptEvent;

@Mod(modid = Main.MODID + "preparseeventtest", name = Main.MODNAME
		+ " PreParseEventTest", version = "0.0.0")
public class PreParseEventTest {

	public static final boolean ENABLE = false;

	@EventHandler
	public void init(FMLInitializationEvent event) {
		if (ENABLE)
			MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onScriptPreParse(ScriptEvent.PreParse event) {
		System.out.println(event.getScriptInstance().toString());
		event.setCanceled(true);
	}
}