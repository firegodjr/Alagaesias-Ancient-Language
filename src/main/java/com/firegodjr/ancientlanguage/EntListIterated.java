package com.firegodjr.ancientlanguage;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;

/**
 * A basic List type that contains List and int values
 * @author firegodjr
 *
 */

@SuppressWarnings({ "unchecked", "rawtypes" })
public class EntListIterated{
	
	private List<EntityLivingBase> entityList;
	private int iterations;
	
	public EntListIterated()
	{
		entityList = new ArrayList<EntityLivingBase>();
		iterations = 0;
	}
	
	public void add(EntityLivingBase e)
	{
		entityList.add(e);
	}
	
	public void addAll(List<EntityLivingBase> list)
	{
		for(EntityLivingBase ent : list)
		{
			entityList.add(ent);
		}
	}
	
	public void clear()
	{
		entityList.clear();
	}
	
	public void setIteration(int iteration)
	{
		iterations = iteration;
	}
	
	public void clearIteration()
	{
		iterations = 0;
	}
	
	public List getList()
	{
		return entityList;
	}
	
	public int getIteration()
	{
		return iterations;
	}

	public boolean isEmpty()
	{
		return entityList.isEmpty();
	}
	
	public void setList(List list)
	{
		entityList = list;
	}
}
