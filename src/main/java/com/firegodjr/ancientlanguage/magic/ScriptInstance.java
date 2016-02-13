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
import com.google.common.collect.Lists;

import joptsimple.internal.Strings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public final class ScriptInstance {

	private MagicEnergy energyStore;
	private Object actualUser;
	private List<IScriptObject> words;
	private List<String> chantedWords = new ArrayList<String>();
	private List<String> wardWords = new ArrayList<String>();

	private int currentParsePos;
	private int seperatorPos;

	public ScriptInstance(IEnergyProducer producer, String script) {
		Main.getLogger().info("Script: "+script);
		this.words = parseScript(script);
		this.energyStore = new MagicEnergy(producer);
		this.actualUser = producer;
	}

	public ScriptInstance(Entity producer, String script) {
		this(Utilities.createProducerFor(producer), script);
		this.actualUser = producer;
	}
	
	public ScriptInstance(EntityPlayer producer, String script) {
		this(Utilities.createProducerFor(producer), script);
		this.actualUser = producer;
	}
	
	public static String getStringFromArray(String[] args) {
		return Strings.join(args, " ");
	}
	
	public static String getStringFromArray(Collection<String> args) {
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
			Main.getLogger().info("\nCurrent Position: "+position+"\nCurrent Word: "+word+"\nCurrent Word's End: "+wordEnd);
			IScriptObject scriptObj = Utilities.getWord(word);
			if(scriptObj != null && parsedScript.add(scriptObj)) { 
				this.chantedWords.add(word);
				if(scriptObj instanceof IWardPlacer) {
					this.wardWords.add(word);
				}
			}
			position = wordEnd;
		}
		Main.getLogger().info("Finished Parsing Script");
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
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void onExecute(World world, Vec3 position) {
		Main.getLogger().info("Attempting to Execute Parsed Script");
		//if(!world.isRemote) return;
		Main.getLogger().info("Executing Parsed Script");
		List selected = new ArrayList();
		List<IWord> activeWords = new ArrayList<IWord>();
		IScriptObject currentWord;
		for (currentParsePos = 0; currentParsePos < words.size(); currentParsePos++) {
			currentWord = words.get(currentParsePos);
			if (currentWord instanceof ISelector) {
				Main.getLogger().info("Selector found");
				List s = ((ISelector) currentWord).getSelected(this, world, position);
				s.removeAll(Collections.singleton(null)); // Prevents NPEs
				selected.addAll(s);
			}

			if (currentWord instanceof IWord) {
				Main.getLogger().info("Action Word found");
				activeWords.add((IWord) currentWord);
			}

			if (currentWord instanceof IModifier) {
				Main.getLogger().info("Modifier found");
				if(currentParsePos-1 > -1)
					((IModifier) currentWord).modifyWord(this, words.get(currentParsePos - 1));
				else Main.getLogger().info("Modifier did nothing");
			}

			if (this.seperatorPos == this.currentParsePos || this.currentParsePos == words.size()-1) {
				Main.getLogger().info("Reached seperator, running seperated section");
				selected.removeAll(Collections.singleton(null));
				activeWords.removeAll(Collections.singleton(null));
				Main.getLogger().info("\nSelected is null: "+selected == null+
						"\nSelected is empty"+selected.isEmpty()+"\nSelected: "+selected);
				Main.getLogger().info("\nActive Words are null: "+activeWords == null+
						"\nActive Words are empty: "+activeWords.isEmpty()+"\nActive Words: "+activeWords);
				for (IWord word : activeWords)
					word.onUse(this, selected);
				activeWords.clear();
				Main.getLogger().info("Active words cleared");
			}
		}
		Main.getLogger().info("Script Execution finished");
	}

	/**
	 * Retrieves Energy producing object running this script
	 */
	public Object getActualUser() {
		return this.actualUser;
	}
	
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

	public List<String> getChant() {
		return this.chantedWords;
	}
	
	public List<String> getWardlessChant() {
		List<String> result = Lists.newCopyOnWriteArrayList(this.chantedWords);
		result.removeAll(this.wardWords);
		return result;
	}
}
