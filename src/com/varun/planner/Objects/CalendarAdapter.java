package com.varun.planner.Objects;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.varun.planner.R;

public class CalendarAdapter extends BaseAdapter
{
	private Context context;
	private int[] days;
	private GregorianCalendar current;
	private ArrayList<Assignment> Assignments;
	private int firstDoM;

	public CalendarAdapter(Context context, int[] days,
			GregorianCalendar selected, ArrayList<Assignment> a)
	{
		this.context = context;
		this.days = days;
		current = selected;
		firstDoM = getFirstDayOfMonth(current);
		Assignments = a;
	}

	private int getFirstDayOfMonth(GregorianCalendar c)
	{
		// SUN = 1, MON = 2... SAT = 7 by default
		c.set(Calendar.DAY_OF_MONTH, 1);
		return c.get(Calendar.DAY_OF_WEEK) - 1;
		// SUN = 0, MON = 1... SAT = 6 changed
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		Integer day = new Integer(0);
		int countAss = 0;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View gridView;

		if (convertView == null)
		{

			gridView = new View(context);

			// get layout from mobile.xml
			gridView = inflater.inflate(R.layout.calendar_cell, null);

			// set value into textview
			TextView dayofmonth = (TextView) gridView
					.findViewById(R.id.day_of_month);

			Log.i("VALUE", "" + position + firstDoM);

			if (position >= firstDoM)
				day = (Integer) getItem(position - firstDoM);

			if (position >= firstDoM)
			{
				if (isToday((Integer) getItem(position - firstDoM), current))
				{
					SpannableString spanString = new SpannableString(String.valueOf(day));
					spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
					spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
					dayofmonth.setText(spanString);
                    dayofmonth.setTextColor(Color.parseColor("#000000"));
				}
				else
					dayofmonth.setText(String.valueOf(day));

				for (Assignment a : Assignments)
				{
                    Integer d = a.getDueDate().get(Calendar.DAY_OF_MONTH);
					if (d.equals((Integer) getItem(position - firstDoM)))
					{
						countAss++;
					}

				}

				if (countAss > 0)
				{
					TextView numAssignments = ((TextView) gridView.findViewById(R.id.number_of_assignments));
							numAssignments.setText(String.valueOf(countAss));
							numAssignments.setTextColor(Color.parseColor("#AA66CC"));
					((TextView) gridView.findViewById(R.id.day_of_month)).setTextColor(Color.parseColor("#AA66CC"));
					
				}
			}

		}
		else
		{
			gridView = (View) convertView;
		}

		return gridView;
	}

	@Override
	public int getCount()
	{
		return days.length + firstDoM;
	}

	@Override
	public Object getItem(int position)
	{
		return (Integer) days[position];
	}

	@Override
	public long getItemId(int position)
	{
		return 0;
	}

	@SuppressWarnings("unchecked")
	private ArrayList<Assignment> ReadFromFile()
	{
		ArrayList<Assignment> assesRead = new ArrayList<Assignment>();
		FileInputStream fis;
		try
		{
			fis = context.openFileInput("Storedasses");
			ObjectInputStream ois = new ObjectInputStream(fis);
			assesRead = (ArrayList<Assignment>) ois.readObject();
			Log.i("READ", "ArrayList read from file");
			ois.close();
			fis.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return (ArrayList<Assignment>) assesRead;
	}

	private boolean isToday(int D, GregorianCalendar MY)
	{
		GregorianCalendar rightNow = (GregorianCalendar) GregorianCalendar
				.getInstance();
		int month = MY.get(Calendar.MONTH);
		int year = MY.get(Calendar.YEAR);

		int nowDay = rightNow.get(Calendar.DAY_OF_MONTH);
		int nowMonth = rightNow.get(Calendar.MONTH);
		int nowYear = rightNow.get(Calendar.YEAR);

		return (D == nowDay && month == nowMonth && year == nowYear);

	}
}