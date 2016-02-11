package com.firegodjr.ancientlanguage.magic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.firegodjr.ancientlanguage.api.magic.IEnergyProducer;
import com.firegodjr.ancientlanguage.api.magic.IEnergyWrapper.IProducerWrapper;
import com.firegodjr.ancientlanguage.api.script.ISelector;
import com.firegodjr.ancientlanguage.api.script.IWord;

import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public final class ScriptInstance {
	
	private IEnergyProducer producer;
	private List<IWord> words;
	private Set<Integer> seperatorPositions = new HashSet<Integer>();
	
	private int currentParsePos;
	
	public ScriptInstance(IEnergyProducer producer, String script) {
		this.words = parseScript(script);
		this.producer = producer;
	}
	
	@SuppressWarnings("unchecked")
	public ScriptInstance(Object producer, String script) {
		if(!Utilities.hasWrapper(producer.getClass())) return;
		this.producer = Utilities.getWrapperFor(producer.getClass()).createProducerFor(producer);
		this.words = parseScript(script);
	}
	
	/**
	 * Parses script into actionable interfaces
	 * @param script The script to parse
	 * @return A list of all actionable interfaces
	 */
	protected static List<IWord> parseScript(String script) {
		List<IWord> parsedScript = new ArrayList<IWord>();
		int position = 0;
		int wordEnd = -1;
		String word;
		while(position != -1) {
			if(position != 0) position++;
			if((wordEnd = script.indexOf(' ', position)) == -1) {
				word = script.substring(position);
			} else {
				word = script.substring(position, wordEnd);
			}
			parsedScript.add(Utilities.getWord(word));
			position = wordEnd;
		}
		return parsedScript;
	}
	
	/**
	 * Handles execution of the script
	 * @param world The world executed in
	 * @param position The position executed in
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void onExecute(World world, Vec3 position) {
		List selected = new ArrayList();
		List<IWord> activeWords = new ArrayList<IWord>();
		Iterator<IWord> it = words.iterator();
		IWord currentWord;
		for(currentParsePos = 0; it.hasNext(); currentParsePos++) {
			currentWord = it.next();
			if(currentWord instanceof ISelector) {
				selected.addAll(((ISelector)currentWord).getSelected(this, world, position));
			}
			
			if(this.seperatorPositions.contains(this.currentParsePos)) {
				for(int i = 0; i < activeWords.size(); i++) activeWords.get(i).onStart(this, selected);
			}
		}
	}
	
	public IEnergyProducer getProducer() {
		return this.producer;
	}
	
	@SuppressWarnings("rawtypes")
	public Object getActualUser() {
		if(this.producer instanceof IProducerWrapper) {
			return ((IProducerWrapper)this.producer).getRepresent();
		} else {
			return this.producer;
		}
	}
	
	public void setSeperatorIndex(int index) {
		this.seperatorPositions.add(index);
	}
	
	public int getCurrentPosition() {
		return this.currentParsePos;
	}
	
}
