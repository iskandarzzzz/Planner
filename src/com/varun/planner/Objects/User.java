package com.varun.planner.Objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class User implements Serializable
{
	private ArrayList<Subject> mSubs;
	
	public User()
	{
		mSubs = new ArrayList<Subject>();
	}
	public User(ArrayList<Subject> subs)
	{
		mSubs = subs;
	}
	
	public ArrayList<Subject> getSubs()
	{
		Collections.sort(mSubs);
		return mSubs;
	}
	
	//returns arraylist of all assignments SORTED BY DUE DATE.
	public ArrayList<Assignment> getAssignments()
	{
		ArrayList<Assignment> asses = new ArrayList<Assignment>();
		for(Subject s : mSubs)
		{
			for(Assignment a : s.getAssignments())
				asses.add(a);
		}
		
		Collections.sort(asses);
		return asses;
	}
	public void setSubs(ArrayList<Subject>subs)
	{
		mSubs = subs;
	}
	
	public int getNumAssignments()
	{
		int sum = 0;
		for(Subject s : mSubs)
		{
			sum += s.getNum();
		}
		return sum;
	}
	
	public int getUrgents()
	{
		int sum = 0;
		for(Subject s : mSubs)
		{
			sum += s.getUrgents();
		}
		return sum;
		
	}
	public void addAssignment(Assignment a)
	{
		Subject s = getSubject(a.getAssClass().trim());
		if (s != null)
		{
			s.addAssignment(a);
		}
		else
		{
			addSubject(a.getAssClass());
			addAssignment(a);
		}
	}
	public void deleteAssignment (Assignment atodel)
	{
		Subject sub = getSubject(atodel.getAssClass().trim());
		if(sub != null)
		{
			sub.delete(atodel);
		}
	}
	
	public ArrayList<String> getClassNames()
	{
		ArrayList<String> names = new ArrayList<String>();
		for(Subject s : mSubs)
		{
			names.add(s.getName());
		}
		return names;
	}
	private Subject getSubject(String name)
	{
		for(Subject s : mSubs)
		{
			if(s.getName().equalsIgnoreCase(name))
				return s;
		}
		return null;
	}
	private void addSubject(String name)
	{
		Subject s = new Subject (name);
		mSubs.add(s);
	}
}
