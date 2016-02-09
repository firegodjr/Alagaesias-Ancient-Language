package com.firegodjr.ancientlanguage.command;

import java.util.ArrayList;
import java.util.List;

public class WordTagged {
	
	private String[] tags;
	private String word;
	
	public WordTagged()
	{
		this("");
	}
	
	public WordTagged(String wordIn)
	{
		this(wordIn, new String[4]);
	}
	
	public WordTagged(String wordIn, String[] tagsIn)
	{
		tags = tagsIn;
		word = wordIn;
	}
	
	public String getWord()
	{
		return word;
	}
	
	public String[] getTags()
	{
		return tags;
	}
	
	public String getTag(int index)
	{
		return tags[index];
	}
	
	public void setTag(int index, String tag)
	{
		tags[index] = tag;
	}
	
	public void clearTags()
	{
		for(int i = 0; i < tags.length; i++)
		{
			tags[i] = "";
		}
	}
	
	public void setWord(String wordIn)
	{
		word = wordIn;
	}
	
	public void clear()
	{
		for(int i = 0; i < tags.length - 1; i++)
			tags[i] = "";
		
		word = "";
	}

}
