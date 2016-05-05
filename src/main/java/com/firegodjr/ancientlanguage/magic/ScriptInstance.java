package com.firegodjr.ancientlanguage.magic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import joptsimple.internal.Strings;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import com.firegodjr.ancientlanguage.Main;
import com.firegodjr.ancientlanguage.api.magic.IEnergyProducer;
import com.firegodjr.ancientlanguage.api.script.IModifier;
import com.firegodjr.ancientlanguage.api.script.IScriptObject;
import com.firegodjr.ancientlanguage.api.script.ISelector;
import com.firegodjr.ancientlanguage.api.script.IWardPlacer;
import com.firegodjr.ancientlanguage.api.script.IWord;
import com.firegodjr.ancientlanguage.utils.MagicUtils;
import com.firegodjr.ancientlanguage.utils.ModHooks;
import com.firegodjr.ancientlanguage.utils.ScriptData;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

/**
 * Class for managing and executing script instances
 */
public final class ScriptInstance {

	private MagicData energyStore;
	private String originalScript;
	private List<IScriptObject> words;
	private List<String> chantedWords = new ArrayList<String>();
	private List<String> wardWords = new ArrayList<String>();
	private ScriptData data;

	private int currentParsePos;
	private int seperatorPos;

	public ScriptInstance(IEnergyProducer producer, String script) {
		Main.getLogger().info("Script: " + script);
		this.originalScript = script;
		this.energyStore = new MagicData(producer);
		this.data = new ScriptData();
		parseScript();
	}

	public ScriptInstance(ICommandSender sender, String[] args) {
		this((Entity)(sender instanceof Entity ? sender : sender.getCommandSenderEntity()), Strings.join(args, " "));
	}

	public ScriptInstance(Entity producer, String script) {
		this(MagicUtils.createProducerFor(producer), script);
		this.energyStore.setActualUser(producer);
	}

	public ScriptInstance(IEnergyProducer producer, String script, ScriptData data) {
		this(producer, script);
		this.data = data;
	}

	/**
	 * Creates String from an array
	 *
	 * @param args
	 *            The array to create from
	 */
	public static String getStringFrom(String[] args) {
		return Strings.join(args, " ");
	}

	/**
	 * Creates String from a collection
	 *
	 * @param args
	 *            The array to create from
	 */
	public static String getStringFrom(Collection<String> args) {
		return Strings.join(new ArrayList<String>(args), " ");
	}

	/**
	 * Parses {@link #originalScript} into {@link #words}
	 */
	protected void parseScript() {
		if (ModHooks.onPreParse(this, this.originalScript)) {
			this.words = Lists.newArrayList();
			return;
		}
		Main.getLogger().info("Parsing Script");
		List<IScriptObject> parsedScript = new ArrayList<IScriptObject>();
		List<String> names = Lists.newArrayList(MinecraftServer.getServer().getAllUsernames());
		int position = 0;
		int wordEnd = -1;
		String word;
		while (position != -1) {
			if (position != 0)
				position++;
			if ((wordEnd = this.originalScript.indexOf(' ', position)) == -1) {
				word = this.originalScript.substring(position);
			} else {
				word = this.originalScript.substring(position, wordEnd);
			}
			Main.getLogger().info("\nCurrent Position: " + position + "\nCurrent Word: " + word + "\nCurrent Word's End: " + wordEnd);
			String name = Main.getConfig().getNameVersion(word);
			IScriptObject scriptObj = ScriptRegistry.getInterfaceForString(word);
			if (!name.isEmpty() && names.contains(name)) this.chantedWords.add(name);
			else if (scriptObj != null && parsedScript.add(scriptObj) && this.chantedWords.add(word) && scriptObj instanceof IWardPlacer)
				this.wardWords.add(word);
			else if (names.contains(word)) this.chantedWords.add(word);
			position = wordEnd;
		}
		Main.getLogger().info("Finished Parsing Script");
		if (ModHooks.onPostParse(this, this.originalScript, parsedScript)) {
			this.words = Lists.newArrayList();
			return;
		}
		this.words = parsedScript;
	}

	/**
	 * Handles execution of the script
	 *
	 * @param world
	 *            The world executed in
	 * @param position
	 *            The position executed in
	 */
	public void onExecute(World world, final Vec3 position) {
		Main.getLogger().info("Executing Parsed Script");
		@SuppressWarnings("unchecked")
		List<Object> selected = world.getPlayers(EntityPlayer.class, new Predicate<EntityPlayer>() {
			@Override
			public boolean apply(EntityPlayer input) {
				return position.squareDistanceTo(input.getPositionVector()) <= 100 && originalScript.indexOf(input.getName()) != -1;
			}});
		if (selected == null) selected = Lists.newArrayList();
		List<IWord> activeWords = Lists.newArrayList();
		IScriptObject currentWord;
		for (currentParsePos = 0; currentParsePos < words.size(); currentParsePos++) {
			currentWord = words.get(currentParsePos);
			if (currentWord instanceof ISelector) {
				ISelector selector = (ISelector) currentWord;
				if (ModHooks.onTypeActivation(this, this.originalScript, this.words, selector, this.chantedWords.get(currentParsePos))) continue;
				Main.getLogger().info("Selector found");
				List<?> s = selector.getSelected(this.getEnergy(), this.data.getImmutableData(selector), world, position);
				if (s != null) {
					s.removeAll(Collections.singleton(null)); // Prevents NPEs
					selected.addAll(s);
				}
			}

			if (currentWord instanceof IWord) {
				IWord action = (IWord) currentWord;
				if (ModHooks.onTypeActivation(this, this.originalScript, this.words, action, this.chantedWords.get(currentParsePos))) continue;
				Main.getLogger().info("Action Word found");
				activeWords.add(action);
			}

			if (currentWord instanceof IModifier) {
				IModifier mod = (IModifier) currentWord;
				if (ModHooks.onTypeActivation(this, this.originalScript, this.words, mod, this.chantedWords.get(currentParsePos))) continue;
				Main.getLogger().info("Modifier found");
				if (mod.modifyWord(this.getEnergy(), this.data, this.words)) this.setSeperatorAtCurrent();
			}

			if (this.seperatorPos == this.currentParsePos || this.currentParsePos == words.size() - 1) {
				Main.getLogger().info("Reached seperator, running seperated section");
				activeWords.removeAll(Collections.singleton(null));
				Main.getLogger().info("Selected is null: " + selected == null + ", Selected is empty: "
						+ selected.isEmpty() + ", Selected: " + selected);
				Main.getLogger().info("Active Words are null: " + activeWords == null + ", Active Words are empty: "
						+ activeWords.isEmpty() + ", Active Words: " + activeWords);
				for (IWord word : activeWords)
					word.onUse(this.getEnergy(), this.data.getImmutableData(word), selected);
				activeWords.clear();
				Main.getLogger().info("Active words cleared");
			}
		}
		Main.getLogger().info("Script Execution finished");
		ModHooks.onScriptEnd(this, this.originalScript, this.words);
	}

	/**
	 * Retrieves Energy producing object running this script
	 */
	public Object getActualUser() {
		return this.getEnergy().getActualUser();
	}

	/**
	 * Retrieves the energy storage object
	 */
	public MagicData getEnergy() {
		return this.energyStore;
	}

	/**
	 * Sets separator at the current parsing position
	 */
	public void setSeperatorAtCurrent() {
		this.seperatorPos = this.currentParsePos;
	}

	/**
	 * Retrieves the current parsing position
	 */
	public int getCurrentPosition() {
		return this.currentParsePos;
	}

	/**
	 * Retrieves the full chant
	 */
	public List<String> getChant() {
		return this.chantedWords;
	}

	/**
	 * Retrieves the chant without the ward section
	 */
	public List<String> getWardlessChant() {
		List<String> result = Lists.newCopyOnWriteArrayList(this.chantedWords);
		result.removeAll(this.wardWords);
		return result;
	}

	/**
	 * Retrieves a printable chant
	 */
	public String getPrintableChant() {
		if (this.chantedWords.isEmpty()) return "";
		return new StringBuilder(Strings.join(this.getChant(), " ")).append("!").toString();
	}

	@Override
	public String toString() {
		return this.getPrintableChant().replace('!', (char) 0);
	}
}
