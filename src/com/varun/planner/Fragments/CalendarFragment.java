package com.varun.planner.Fragments;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;

import com.varun.planner.Objects.Assignment;
import com.varun.planner.Objects.CalendarAdapter;
import com.varun.planner.Objects.User;
import com.varun.planner.R;
import com.varun.planner.Objects.TabsPagerAdapter;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

public class CalendarFragment extends Fragment
{
	private GridView cal;
	private final String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
	private final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	private String[] Years;
	private Spinner yearSpin;
	int monthsel;
	int yearsel;
	private Spinner monthSpin;
	private GregorianCalendar calSelected;
	private CalendarAdapter adptr;

	public CalendarFragment()
	{
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{    
		monthsel = Calendar.getInstance().get(Calendar.MONTH);
		yearsel = Calendar.getInstance().get(Calendar.YEAR);

		View rootView = inflater.inflate(R.layout.fragment_calendar_layout,container, false);
		final ActionBar actionBar = getActivity().getActionBar();

		Years = getYears();

		calSelected = new GregorianCalendar(yearsel, monthsel, 1);

		adptr = new CalendarAdapter(getActivity(), getDays(), calSelected, AssignmentsinMonth(calSelected));

		cal = (GridView)rootView.findViewById(R.id.calendar_grid);

		adptr.notifyDataSetChanged();
		cal.setAdapter(adptr);

		monthSpin = (Spinner)rootView.findViewById(R.id.Month_Spinner);
		yearSpin = (Spinner)rootView.findViewById(R.id.Year_Spinner);

		ArrayAdapter<String> Mnthadapter = new ArrayAdapter<String>(getActivity(),  android.R.layout.simple_spinner_dropdown_item, months);
		monthSpin.setAdapter(Mnthadapter);

		ArrayAdapter<String> Yradapter = new ArrayAdapter<String>(getActivity(),  android.R.layout.simple_spinner_dropdown_item, Years);
		yearSpin.setAdapter(Yradapter);

		monthSpin.setSelection(monthsel);
		yearSpin.setSelection(yearsel-2013);

		monthSpin.setOnItemSelectedListener(new OnItemSelectedListener()
		{

			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{

				monthsel = monthSpin.getSelectedItemPosition();
				calSelected = new GregorianCalendar(yearsel, monthsel, 1);
				adptr = new CalendarAdapter(getActivity(), getDays(), calSelected, AssignmentsinMonth(calSelected));
				adptr.notifyDataSetChanged();
				cal.setAdapter(adptr);
			}

			public void onNothingSelected(AdapterView<?> arg0) 
			{

			}

		});

		cal.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View v, int pos, long id)
			{
				TextView day = (TextView)v.findViewById(R.id.day_of_month);
				if(day.getText()!=null && !day.getText().equals(""))
				{
					ArrayList<Assignment> ThatDay = new ArrayList<Assignment>();
					int d = Integer.parseInt(day.getText().toString());
					GregorianCalendar today = new GregorianCalendar(yearsel, monthsel, d);
					ThatDay = AssignmentsOnDay(today);

					if(ThatDay.size()>0)
					{
						TextView title = new TextView(getActivity());
						TextView content = new TextView(getActivity());

						title.setAllCaps(true);
						title.setTextSize(32);
						title.setPadding(8, 0, 0, 0);

						content.setPadding(8, 8, 0, 0);
						content.setTextSize(16);
						setTypeface(title, "Roboto-Thin.ttf");
						setTypeface(content, "Roboto-Black.ttf");
						String mssg = "";

						for(Assignment a : ThatDay)
						{
							mssg+= a.getAssClass().toUpperCase()+": "+a.getTitle()+"\n\n";
						}
						String date = ""+d+" "+months[monthsel]+", "+yearsel;
						title.setText(date);
						content.setText(mssg);

						AlertDialog.Builder bldr = new AlertDialog.Builder(getActivity());
						bldr.setCustomTitle(title);
						bldr
						.setView(content)
						.setCancelable(true)
						.setPositiveButton("View All Assignments", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								getFragmentManager().beginTransaction()
						        .replace(R.id.fragment_container, new ItemsFragment()).commit();
							}
						})
						.setNegativeButton("Cancel",new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog,int id)
							{

								dialog.cancel();
							}
						});
						AlertDialog UponSelected = bldr.create();
						UponSelected.show();
					}
				}


			}

		});

		yearSpin.setOnItemSelectedListener(new OnItemSelectedListener()
		{

			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{

				yearsel = yearSpin.getSelectedItemPosition()+2013;
				calSelected = new GregorianCalendar(yearsel, monthsel, 1);
				adptr = new CalendarAdapter(getActivity(), getDays(), calSelected, AssignmentsinMonth(calSelected));
				adptr.notifyDataSetChanged();
				cal.setAdapter(adptr);
			}

			public void onNothingSelected(AdapterView<?> arg0) 
			{

			}
		});



		return rootView;
	}

	public void setTypeface(TextView t, String path)
	{
		Typeface TF = Typeface.createFromAsset(getActivity().getAssets(), "fonts/"+path);
		t.setTypeface(TF);
	}
	private User ReadFromFile()
	{
		User UserRead = new User();
		FileInputStream fis;
		try {
		    fis = getActivity().openFileInput("UserData");
		    ObjectInputStream ois = new ObjectInputStream(fis);
		    UserRead = (User) ois.readObject();
		    Log.i("READ", "ArrayList read from file");
		    ois.close();
		    fis.close();
		} catch (Exception e) {
		    e.printStackTrace();
		}
		return (User)UserRead;
	}

	private String[]getYears()
	{
		String[]years = new String[99];
		for(int i = 0; i < 99; i++)
		{
			int y = 2013+i;
			years[i]=""+y;
		}
		return years;
	}

	private int[] getDays()
	{
		int[] daysinmonth = new int[daysOfMonth[calSelected.get(Calendar.MONTH)]];
		for(int i = 1; i<=daysinmonth.length; i++)
		{
			daysinmonth[i-1]=i;
		}
		return daysinmonth;
	}

	private ArrayList<Assignment> AssignmentsinMonth(GregorianCalendar cal)
	{
		User user = ReadFromFile();
		if(user == null)
			return null;
		ArrayList<Assignment> AllAssigns = user.getAssignments();
		Collections.sort(AllAssigns);
		ArrayList<Assignment> toBeReturned = new ArrayList<Assignment>();
		for(Assignment a : AllAssigns)
		{
			if(a.getDueDate().get(Calendar.MONTH)==cal.get(Calendar.MONTH) && a.getDueDate().get(Calendar.YEAR)==cal.get(Calendar.YEAR))
			{
				toBeReturned.add(a);
			}

		}
		return toBeReturned;
	}

	private ArrayList<Assignment> AssignmentsOnDay(GregorianCalendar cal)
	{
		User user = ReadFromFile();
		if(user == null)
			return null;
		ArrayList<Assignment> AllAssigns = user.getAssignments();
		Collections.sort(AllAssigns);
		ArrayList<Assignment> toBeReturned = new ArrayList<Assignment>();
		for(Assignment a : AllAssigns)
		{
			GregorianCalendar ass = a.getDueDate();
			if(a.isDueDate(cal))
			{
				toBeReturned.add(a);
			}

		}
		return toBeReturned;
	}
}
