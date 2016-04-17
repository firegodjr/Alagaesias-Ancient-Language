package com.firegodjr.ancientlanguage.magic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.firegodjr.ancientlanguage.Main;
import com.firegodjr.ancientlanguage.api.magic.IEnergyProducer;
import com.firegodjr.ancientlanguage.api.script.IModifier;
import com.firegodjr.ancientlanguage.api.script.IScriptObject;
import com.firegodjr.ancientlanguage.api.script.ISelector;
import com.firegodjr.ancientlanguage.api.script.IWardPlacer;
import com.firegodjr.ancientlanguage.api.script.IWord;
import com.firegodjr.ancientlanguage.utils.ModHooks;
import com.firegodjr.ancientlanguage.utils.ScriptUtils;
import com.google.common.collect.Lists;

import joptsimple.internal.Strings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

/**
 * Class for managing and executing script instances
 */
public final class ScriptInstance {

	private MagicEnergy energyStore;
	private Object actualUser;
	private String originalScript;
	private List<IScriptObject> words;
	private List<String> chantedWords = new ArrayList<String>();
	private List<String> wardWords = new ArrayList<String>();

	private int currentParsePos;
	private int seperatorPos;

	public ScriptInstance(IEnergyProducer producer, String script) {
		Main.getLogger().info("Script: " + script);
		this.originalScript = script;
		this.words = parseScript(script);
		this.energyStore = new MagicEnergy(producer);
		this.actualUser = producer;
	}

	public ScriptInstance(Entity producer, String script) {
		this(ScriptUtils.createProducerFor(producer), script);
		this.actualUser = producer;
	}

	public ScriptInstance(EntityPlayer producer, String script) {
		this(ScriptUtils.createProducerFor(producer), script);
		this.actualUser = producer;
	}

	/**
	 * Creates a script instance for an object and arguments depending on the
	 * object type
	 * 
	 * @param producer
	 *            The object to create an instance for
	 * @param args
	 *            The arguments sent
	 */
	public static ScriptInstance createScriptInstance(Object producer, String[] args) {
		String script = Strings.join(args, " ");

		if (producer instanceof IEnergyProducer) {
			Main.getLogger().info("Producer was IEnergyProducer");
			return new ScriptInstance((IEnergyProducer) producer, script);
		} else if (producer instanceof EntityPlayer) {
			Main.getLogger().info("Producer was Player");
			return new ScriptInstance((EntityPlayer) producer, script);
		} else if (producer instanceof Entity) {
			Main.getLogger().info("Producer was Entity");
			return new ScriptInstance((Entity) producer, script);
		} else {
			return null;
		}
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
	 * Parses script into actionable interfaces
	 * 
	 * @param script
	 *            The script to parse
	 * @return A list of all actionable interfaces
	 */
	protected List<IScriptObject> parseScript(String script) {
		if(!ModHooks.onPreParse(this, script)) return Lists.newArrayList();
		Main.getLogger().info("Parsing Script");
		List<IScriptObject> parsedScript = new ArrayList<IScriptObject>();
		int position = 0;
		int wordEnd = -1;
		String word;
		while (position != -1) {
			if (position != 0)
				position++;
			if ((wordEnd = script.indexOf(' ', position)) == -1) {
				word = script.substring(position);
			} else {
				word = script.substring(position, wordEnd);
			}
			Main.getLogger().info(
					"\nCurrent Position: " + position + "\nCurrent Word: " + word + "\nCurrent Word's End: " + wordEnd);
			IScriptObject scriptObj = ScriptRegistry.getInterfaceForString(word);
			if (scriptObj != null && parsedScript.add(scriptObj)) {
				this.chantedWords.add(word);
				if (scriptObj instanceof IWardPlacer) {
					this.wardWords.add(word);
				}
			}
			position = wordEnd;
		}
		Main.getLogger().info("Finished Parsing Script");
		if(!ModHooks.onPostParse(this, script, parsedScript)) return Lists.newArrayList();
		return parsedScript;
	}

	/**
	 * Handles execution of the script
	 * 
	 * @param world
	 *            The world executed in
	 * @param position
	 *            The position executed in
	 */
	public void onExecute(World world, Vec3 position) {
		Main.getLogger().info("Attempting to Execute Parsed Script");
		// if(!world.isRemote) return;
		Main.getLogger().info("Executing Parsed Script");
		List<Object> selected = new ArrayList<Object>();
		List<IWord> activeWords = new ArrayList<IWord>();
		IScriptObject currentWord;
		for (currentParsePos = 0; currentParsePos < words.size(); currentParsePos++) {
			currentWord = words.get(currentParsePos);
			if (currentWord instanceof ISelector) {
				ISelector selector = (ISelector) currentWord;
				if(!ModHooks.onTypeActivation(this, this.originalScript, this.words, selector, this.chantedWords.get(currentParsePos))) continue;
				Main.getLogger().info("Selector found");
				List<?> s = selector.getSelected(this, world, position);
				s.removeAll(Collections.singleton(null)); // Prevents NPEs
				selected.addAll(s);
			}

			if (currentWord instanceof IWord) {
				IWord action = (IWord) currentWord;
				if(!ModHooks.onTypeActivation(this, this.originalScript, this.words, action, this.chantedWords.get(currentParsePos))) continue;
				Main.getLogger().info("Action Word found");
				activeWords.add(action);
			}

			if (currentWord instanceof IModifier) {
				IModifier mod = (IModifier) currentWord;
				if(!ModHooks.onTypeActivation(this, this.originalScript, this.words, mod, this.chantedWords.get(currentParsePos))) continue;
				Main.getLogger().info("Modifier found");
				if (currentParsePos - 1 > -1)
					mod.modifyWord(this, words.get(currentParsePos - 1));
				else
					Main.getLogger().info("Modifier did nothing");
			}

			if (this.seperatorPos == this.currentParsePos || this.currentParsePos == words.size() - 1) {
				Main.getLogger().info("Reached seperator, running seperated section");
				selected.removeAll(Collections.singleton(null));
				activeWords.removeAll(Collections.singleton(null));
				Main.getLogger().info("Selected is null: " + selected == null + ", Selected is empty"
						+ selected.isEmpty() + ", Selected: " + selected);
				Main.getLogger().info("Active Words are null: " + activeWords == null + ", Active Words are empty: "
						+ activeWords.isEmpty() + ", Active Words: " + activeWords);
				for (IWord word : activeWords)
					word.onUse(this, selected);
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
		return this.actualUser;
	}

	/**
	 * Retrieves the energy storage object
	 */
	public MagicEnergy getEnergy() {
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
		return new StringBuilder(Strings.join(this.getChant(), " ")).append("!").toString();
	}

	@Override
	public String toString() {
		return this.getPrintableChant().replace('!', (char) 0);
	}
}
