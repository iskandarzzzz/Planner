package com.varun.planner.Objects;

import java.io.File;
import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

@SuppressWarnings({ "rawtypes", "serial" })
public class Assignment implements Comparable, Serializable
{

	/**
	 * 
	 */
	private String title;
	private String description;
	private String assClass;
	private GregorianCalendar dueDate;
	private boolean urgent;
	private String AudioFile;
	private boolean hasAudio;
	private static long urgency = 172800000;
	private final String[] months = { "January", "February", "March", "April",
			"May", "June", "July", "August", "September", "October",
			"November", "December" };

	public Assignment(String t, String d, String c, int year, int month,
			int day, int hour, int minute)
	{
		setTitle(t);
		setDescription(d);
		setAssClass(c);
		setDueDate(new GregorianCalendar(year, month, day, hour, minute));
		calcIfUrgent();
		hasAudio = false;
		AudioFile = "";
	}

	public Assignment(String t, String d, String c, int year, int month,
			int day, int hour, int minute, String file)
	{
		setTitle(t);
		setDescription(d);
		setAssClass(c);
		setDueDate(new GregorianCalendar(year, month, day, hour, minute));
		calcIfUrgent();
		AudioFile = file;
		hasAudio = true;
	}

	public boolean isUrgent()
	{
		calcIfUrgent();
		return urgent;
	}

	private void calcIfUrgent()
	{
		Calendar rightnow = Calendar.getInstance();
		long millis = rightnow.getTimeInMillis();
		long thismillis = dueDate.getTimeInMillis();
		if (thismillis - millis <= urgency)
			urgent = true;
		else
			urgent = false;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getAssClass()
	{
		return assClass;
	}

	public void setAssClass(String assClass)
	{
		this.assClass = assClass;
	}

	public String getAudioPath()
	{
		return AudioFile;
	}

	public File getAudioFile()
	{
		if (AudioFile != "")
		{
			File audio = new File(AudioFile);
			return audio;
		}
		else
			return null;
	}

	public boolean hasAudio()
	{
		return hasAudio;
	}

	public GregorianCalendar getDueDate()
	{
		return dueDate;
	}

	public void setDueDate(GregorianCalendar dueDate)
	{
		this.dueDate = dueDate;
	}

	public String formatCal()
	{
		int minute = dueDate.get(Calendar.MINUTE);
		int hour = dueDate.get(Calendar.HOUR_OF_DAY);
		int day = dueDate.get(Calendar.DAY_OF_MONTH);
		int month = dueDate.get(Calendar.MONTH);
		int year = dueDate.get(Calendar.YEAR);
		int DoW = dueDate.get(Calendar.DAY_OF_WEEK);
		String mth = getMonth(month);
		String dayOfWk = "";
		String date = "";
		switch (DoW)
		{
		case 1:
			dayOfWk = "Sunday";
			break;
		case 2:
			dayOfWk = "Monday";
			break;
		case 3:
			dayOfWk = "Tuesday";
			break;
		case 4:
			dayOfWk = "Wednesday";
			break;
		case 5:
			dayOfWk = "Thursday";
			break;
		case 6:
			dayOfWk = "Friday";
			break;
		case 7:
			dayOfWk = "Saturday";
			break;
		}
		String min = "";
		if (minute < 10)
			min = "0" + minute;
		else
			min = String.valueOf(minute);
		if(isTomorrow(dueDate))
			date = date = "Due\tTomorrow at " + hour + ":" + min;
		else
			date = "Due\t" + dayOfWk + ", " + day + " " + mth + " " + year+ " at " + hour + ":" + min;
		
		
		
		return date;
	}

	public void onDelete()
	{
		if (hasAudio())
		{
			File del = new File(getAudioPath());
			del.delete();
		}
	}

	public int compareTo(Object a1)
	{
		return dueDate.compareTo(((Assignment) a1).getDueDate());
	}
	public int compareClasses(Object a1)
	{
		return assClass.compareTo(((Assignment) a1).getAssClass());
	}
	public boolean isDueDate(GregorianCalendar today)
	{
		return (dueDate.get(Calendar.MONTH)==today.get(Calendar.MONTH) && dueDate.get(Calendar.YEAR)==today.get(Calendar.YEAR) && dueDate.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH));
	}
	private boolean isTomorrow(GregorianCalendar day)
	{
		GregorianCalendar today = (GregorianCalendar) GregorianCalendar.getInstance();
		
		int dueDay = day.get(Calendar.DATE);
		int t = today.get(Calendar.DATE);
		
		return dueDay-t==1;
	}

	private String getMonth(int index)
	{
		return months[index];
	}
	
	public static void changeUrgency(int days)
	{
		urgency = 86400000*days;
	}
}
