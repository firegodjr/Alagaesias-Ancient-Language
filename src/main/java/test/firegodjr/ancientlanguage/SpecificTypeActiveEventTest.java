package test.firegodjr.ancientlanguage;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.firegodjr.ancientlanguage.Main;
import com.firegodjr.ancientlanguage.api.script.IWord;
import com.firegodjr.ancientlanguage.event.ScriptEvent;
import com.firegodjr.ancientlanguage.magic.words.WordAction.ShieldWord;

@Mod(modid = Main.MODID + "specifictypeactiveeventtest", name = Main.MODNAME
		+ " SpecificTypeActiveEventTest", version = "0.0.0")
public class SpecificTypeActiveEventTest {

	public static final boolean ENABLE = false;

	@EventHandler
	public void init(FMLInitializationEvent event) {
		if (ENABLE)
			MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onScriptPreParse(ScriptEvent.TypeActive<IWord> event) {
		if (!(event.getWordObject() instanceof ShieldWord))
			return;
		System.out.println(event.getString());
		event.setCanceled(true);
	}
}