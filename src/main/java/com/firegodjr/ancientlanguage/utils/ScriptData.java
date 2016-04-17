package com.firegodjr.ancientlanguage.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.firegodjr.ancientlanguage.api.script.IScriptObject;
import com.firegodjr.ancientlanguage.magic.ScriptRegistry;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * A class to allow for both mutable and immutable data depending on the
 * circumstance
 */
public class ScriptData {

	private Map<Integer, Map<String, String>> idToDataList;
	private List<Collection<String>> wordLocations;

	public ScriptData() {
		this.idToDataList = Maps.newHashMap();
		this.wordLocations = Lists.newArrayList();
	}

	public ScriptData(Map<Integer, Map<String, String>> dataList, List<Collection<String>> wordLocations) {
		this.idToDataList = dataList;
		this.wordLocations = wordLocations;
	}

	/**
	 * Retrieves a data map for a word
	 * 
	 * @param word
	 *            The word to get data for
	 */
	public Map<String, String> getDataForWord(String word) {
		for (int i = 0; i < wordLocations.size(); i++) {
			if (wordLocations.get(i).contains(word)) {
				Map<String, String> result = idToDataList.get(i);
				if (result == null) {
					result = Maps.newHashMap();
					idToDataList.put(i, result);
				}
				return result;
			}
		}
		return Collections.emptyMap();
	}

	/**
	 * Retrieves a immutable data map for a word
	 * 
	 * @param word
	 *            The word to get data for
	 */
	public Map<String, String> getImmutableData(String word) {
		return Collections.unmodifiableMap(this.getDataForWord(word));
	}

	/**
	 * Retrieves a immutable data map for a script object
	 * 
	 * @param object
	 *            The script object to get data for
	 */
	public Map<String, String> getImmutableData(IScriptObject object) {
		return this.getImmutableData(ScriptRegistry
				.getStringForInterface(object));
	}

	/**
	 * Registers words together for data association
	 * 
	 * @param words
	 *            The words to register
	 */
	public ScriptData add(Collection<String> words) {
		if (words == null || words.size() < 1)
			return this;
		wordLocations.add(words);
		return this;
	}

	/**
	 * Registers words together for data association
	 * 
	 * @param words
	 *            The words to register
	 */
	public ScriptData add(String... words) {
		if (words == null)
			return this;
		return this.add(Lists.newArrayList(Arrays.asList(words)));
		// Prefer to stay consistent and allow size mutability
	}
}