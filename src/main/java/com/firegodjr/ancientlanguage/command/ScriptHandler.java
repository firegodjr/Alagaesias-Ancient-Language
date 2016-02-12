package com.firegodjr.ancientlanguage.command;

import java.util.ArrayList;
import java.util.List;

import com.firegodjr.ancientlanguage.BlockPosHit;
import com.firegodjr.ancientlanguage.EntListIterated;
import com.firegodjr.ancientlanguage.Main;
import com.firegodjr.ancientlanguage.api.magic.IEnergyProducer;
import com.firegodjr.ancientlanguage.magic.ScriptInstance;

import joptsimple.internal.Strings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class ScriptHandler 
{
	/**
	 * The index of the "TYPE" tag
	 */
	private final byte TYPE = 0;
	
	/**
	 * The index of the "MODIFIER" tag
	 */
	private final byte MODIFIER = 1;
	
	/**
	 * The index of the "SUBWORD" tag
	 */
	private final byte SUBWORD = 3;

	public List<String> wardScript = new ArrayList();

	/*
	 * TYPE: 
	 * Action tag: "action" 
	 * Target tag: "target"
	 * 
	 * MODIFIER: 
	 * Comma tag: "comma" 
	 * Separator tag: "separator" 
	 * Invalid tag: "invalid"
	 */

	public ScriptHandler() {

	}
	
	public ScriptInstance createScriptInstance(Object producer, String[] args) {
		String script = Strings.join(args, " ");
		
		if(producer instanceof IEnergyProducer) {
			Main.getLogger().info("Producer was IEnergyProducer");
			return new ScriptInstance((IEnergyProducer)producer, script);
		} else if(producer instanceof EntityPlayer) {
			Main.getLogger().info("Producer was Player");
			return new ScriptInstance((EntityPlayer)producer, script);
		} else if(producer instanceof Entity) {
			Main.getLogger().info("Producer was Entity");
			return new ScriptInstance((Entity)producer, script);
		} else {
			return null;
		}
	}

	/**
	 * Takes raw spell input and converts each word to a WordTagged, all of
	 * which are outputted in an ArrayList()
	 * 
	 * @return ArrayList of WordTagged objects
	 */
	public List<WordTagged> getScriptFromSpell(String[] args) {
		WordTagged wordTaggedSaved = new WordTagged();
		List<WordTagged> script = new ArrayList();
		for (int i = 0; i < args.length; i++) {
			WordTagged wordTagged = new WordTagged();
			for (byte j = 0; j < wordTaggedSaved.getTags().length - 1; j++) {
				wordTagged.setTag(j, wordTaggedSaved.getTag(j));
				if (wordTaggedSaved.getTag(j) == "separator" || wordTaggedSaved.getTag(j) == "comma") {
					wordTaggedSaved.clearTags();
					Main.getLogger().info("Applied modifier to retried word. Clearing saved modifier.");
				}
			}
			Main.getLogger().info("Adding new WordTagged from args[" + i + "]");
			WordTagged addition = new WordTagged();
			String word = args[i];
			boolean isAction = WordHandler.isWord(word, "action");
			boolean isLivingTarget = WordHandler.isWord(word, "livingtarget");
			boolean isBlockTarget = WordHandler.isWord(word, "blocktarget");
			boolean isFromVariant = WordHandler.isWord(word, "from");

			wordTagged.setWord(word);

			/*
			 * Begin setting tags
			 */
			if (isAction) {
				wordTagged.setTag(TYPE, "action");
				script.add(wordTagged);
				Main.getLogger().info("Found action word at args[" + i + "]");
			} else if (isLivingTarget) {
				wordTagged.setTag(TYPE, "livingtarget");
				script.add(wordTagged);

				Main.getLogger().info("Found target word at args[" + i + "]");
			} else if (isBlockTarget) {
				wordTagged.setTag(TYPE, "blocktarget");
				script.add(wordTagged);

				Main.getLogger().info("Found block target word at args[" + i + "]");
			} else if (isFromVariant) {
				wordTagged.setTag(TYPE, "fromvariant");
				Main.getLogger().info("Found 'from' variant at args[" + i + "]");

				if (args[i + 1] != null && WordHandler.isWord(clearSeparators(args[i + 1]), "wardtarget")) {
					String nextWord = args[i + 1];
					Main.getLogger().info("Found 'from' target at args[" + (i + 1) + "]");
					wordTagged.setTag(SUBWORD, clearSeparators(nextWord));
					if (nextWord.contains(":")) {
						wordTagged.setTag(MODIFIER, "colon");
						Main.getLogger().info("Found colon in 'from' target");
					}
					Main.getLogger().info("Found 'from' variant and target. Skipping 'from' target processing.");
					i++;
				}
				script.add(wordTagged);
			} else if (word.contains(",")) {
				wordTaggedSaved.setTag(MODIFIER, "comma");
				args[i] = clearSeparators(args[i]);
				i--;
				Main.getLogger().info("Found comma at args[" + i + "], modifying input and retrying");
			}
			else if(word.contains(":"))
			{
				wordTaggedSaved.setTag(MODIFIER, "colon");
				args[i] = clearSeparators(args[i]);
				i--;
				Main.getLogger().info("Found colon at args[" + i + "], modifying input and retrying");
			}
			else if(word.contains(".") || word.contains(";"))
			{
				wordTaggedSaved.setTag(MODIFIER, "separator");
				args[i] = clearSeparators(args[i]);
				i--;
				Main.getLogger().info("Found separator at args[" + i + "], modifying input and retrying");
			}
			else
			{
				Main.getLogger().info("Found invalid string at args[" + i + "]");
			}
		}
		return script;
	}

	/**
	 * Cleans the script of null entries.
	 * 
	 * @param script
	 * @return The cleaned script
	 */
	public List<WordTagged> cleanScript(List<WordTagged> script) {
		for (int i = 0; i < script.size(); i++) {
			WordTagged word = script.get(i);

			if (word == null) // Clean any null entries
			{
				script.remove(i);
			} else if (!WordHandler.isWord(word.getWord(), "any")) // Clean any
																	// invalid
																	// words
			{
				script.remove(i);
			}
		}
		return script;
	}
	
	public void executeScript(List<WordTagged> script, EntityPlayer player) {
		EntListIterated livingTargets = new EntListIterated();
		List<Entity> entTargets = new ArrayList();
		List<BlockPosHit> blockTargets = new ArrayList();

		List<String> actions = new ArrayList();
		if(!player.worldObj.isRemote)
		for(int i = 0; i < script.size(); i++)
		{
			WordTagged component = script.get(i);
			
			/*
			 * Figure out what type of word this component is,
			 * then act accordingly.
			 */
			if(component.getTag(TYPE).equals("livingtarget"))
			{
				//TODO: Add handling code for standard entities
				//TODO: Don't add targets if they already exist in the list
				Main.getLogger().info("Found living target tag at index[" + i + "]");
				
				EntListIterated iteratedTargets = WordHandler.getLivingTargetsFromWord(component.getWord(), player);
				livingTargets.addAll(iteratedTargets.getList());
				
				if(livingTargets.getIteration() > 3)
				{
					Main.getLogger().info("Averaging distances...");
					livingTargets.setIteration((iteratedTargets.getIteration() + livingTargets.getIteration())/2);
				}
				
				if(!actions.isEmpty())
				{
					Main.getLogger().info("Executable actions found. Executing with targets:");
					Main.getLogger().info("          " + livingTargets);
					for(String s : actions)
						WordHandler.performEntityAction(player, s, livingTargets.getList(), iteratedTargets.getIteration());
					/*
					 * TODO
					 * Note that this doesn't clear the "livingTargets" or "actions" lists.
					 * Clearing the lists requires the use of the "separator" tag
					 * Changes may be needed to keep the syntax natural and easy to use
					 */
				}
			}
			else if(component.getTag(TYPE).equals("blocktarget"))
			{
				Main.getLogger().info("Found block target tag at index[" + i + "]");
				List<BlockPosHit> blockList = WordHandler.getBlockTargetsFromWord(component.getWord(), player);
				for(BlockPosHit bp : blockList)
				{
					blockTargets.add(bp);
				}
				
				if(!actions.isEmpty())
				{
					Main.getLogger().info("Executable actions found. Executing with targets:");
					Main.getLogger().info("          " + blockTargets);
					for(String s : actions)
						WordHandler.performBlockAction(player, s, blockTargets);
				}
			}
			else if(component.getTag(TYPE).equals("action"))
			{
				if(livingTargets.isEmpty())
				{
					actions.add(component.getWord());
				}
				else
				{
					actions.add(component.getWord());
					for(String s : actions)
						WordHandler.performEntityAction(player, s, livingTargets.getList(), livingTargets.getIteration());
				}
				
				if(blockTargets.isEmpty())
				{
					actions.add(component.getWord());
				}
				else
				{
					actions.add(component.getWord());
					for(String s : actions)
					{
						WordHandler.performBlockAction(player, s, blockTargets);
					}
				}
				/*
				 * Check for ward TODO
				 */
				
				/*
				 * Apply Modifiers
				 */
				if (component.getTag(MODIFIER) != null)
					if (component.getTag(MODIFIER).equals("separator")) {
						Main.getLogger().info("Separator found, clearing targets and actions.");
						livingTargets.clear();
						entTargets.clear();
						blockTargets.clear();
						actions.clear();
					} else if (component.getTag(MODIFIER).equals("comma")) {
						Main.getLogger().info("Found comma");
					}
		}
	}
	
	/**
	 * Formats a nice output for the player to see what spell was casted
	 * @param script
	 * @return
	 */
	public String getChantFromScript(List<WordTagged> script)
	{
		String out = "";
		for(int i = 0; i < script.size(); i++)
		{
			String word = script.get(i).getWord();
			out += word + (script.get(i).getTag(SUBWORD) != null ? " " + script.get(i).getTag(SUBWORD) : "")
					+ (script.get(i).getTag(MODIFIER) == "colon" ? ":" : "")
					+ (script.get(i).getTag(MODIFIER) == "comma" ? "," : "")
					+ (script.get(i).getTag(MODIFIER) == "separator" ? "." : "") + (i == script.size() - 1 ? "!" : " ");
		}

		return out.trim();
	}
	
	public String getScriptInstanceChant(ScriptInstance instance) {
		return new StringBuilder(Strings.join(instance.getChant(), " ")).append("!").toString();
	}

	/**
	 * Removes separator and other miscellaneous characters from the input
	 * String, then calls String.trim()
	 * 
	 * @param wordIn
	 * @return
	 */
	private String clearSeparators(String wordIn) {
		String out = wordIn.replace('.', ' ').replace(';', ' ').replace(',', ' ').replace(':', ' ').trim();
		Main.getLogger().info("Clearing any punctuation: \"" + wordIn + "\" -> \"" + out + "\"");
		return out;
	}
}
