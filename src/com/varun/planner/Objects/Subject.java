package com.varun.planner.Objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class Subject implements Comparable, Serializable
{
	private ArrayList<Assignment> mAssignments;
	private String mName;
	
	public Subject(String name)
	{
		mName = name;
		mAssignments = new ArrayList<Assignment>();
	}
	
	public String getName()
	{
		return mName;
	}
	@SuppressWarnings("unchecked")
	public void addAssignment(Assignment a)
	{
		mAssignments.add(a);
		Collections.sort(mAssignments);
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Assignment>getAssignments()
	{
		Collections.sort(mAssignments);
		return mAssignments;
	}
	public int getNum()
	{
		if(mAssignments != null)
			return mAssignments.size();
		else
			return 0;
	}
	
	public int getUrgents()
	{
		int sum = 0;
		if(mAssignments != null)
		{
			for(Assignment a : mAssignments)
				if(a.isUrgent())
					sum++;
		}
		return sum;
	}
	@Override
	public int compareTo(Object arg0)
	{
		if(!(arg0 instanceof Subject))
			return 0;
		else
			return this.getName().compareToIgnoreCase(((Subject)arg0).getName());
	}
	
	public void delete(Assignment atodel)
	{
		if(mAssignments.contains(atodel))
		{
			atodel.onDelete();
			mAssignments.remove(atodel);
		}
	}
}
