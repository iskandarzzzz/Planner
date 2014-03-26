package com.varun.planner.Fragments;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.GregorianCalendar;

import com.varun.planner.R;
import com.varun.planner.Activities.MainActivity;
import com.varun.planner.Objects.Assignment;
import com.varun.planner.Objects.User;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DetailViewFragment extends Fragment
{

	private Assignment mAssignment;
	private View rootView;
	TextView title;
	TextView subj;
	TextView desc;
	TextView duedate;
	
	public DetailViewFragment()
	{
	
	}
	
	public void setAssignment(Assignment a)
	{	
		mAssignment = a;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		setHasOptionsMenu(true);
		final ActionBar actionBar = getActivity().getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		
		Typeface Thin = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/Roboto-Thin.ttf");
		Typeface Black = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/Roboto-Black.ttf");
		Typeface LightItal = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/Roboto-LightItalic.ttf");
		Typeface Regular = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/Roboto-Regular.ttf");
		
		rootView = inflater.inflate(R.layout.fragment_detail_view, container, false);
		title = (TextView) rootView.findViewById(R.id.name);
		subj = (TextView) rootView.findViewById(R.id.subject);
		desc = (TextView) rootView.findViewById(R.id.description);
		duedate = (TextView) rootView.findViewById(R.id.duedate);
		
		title.setText(mAssignment.getTitle());
		subj.setText(mAssignment.getAssClass());
		desc.setText(mAssignment.getDescription());
		duedate.setText(mAssignment.formatCal());
		
		title.setTypeface(Black);
		subj.setTypeface(Thin);
		desc.setTypeface(Regular);
		duedate.setTypeface(LightItal);
		return rootView;
	}
	
	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		inflater.inflate(R.menu.detailviewmenu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == (R.id.itemDelete))
		{
			
			Fragment fragment = new ItemsFragment();
		    Bundle bundle = new Bundle();
		    bundle.putSerializable("del", mAssignment);
		    fragment.setArguments(bundle);
		    getFragmentManager().beginTransaction()
	        .replace(R.id.fragment_container, fragment).commit();
		 
		}
		return true;
	}
	
	@SuppressWarnings({ "unchecked" })
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
	
	private void WriteToFile(User user) throws IOException
	{
		FileOutputStream fos = getActivity().openFileOutput("UserData", Context.MODE_PRIVATE);
	    ObjectOutputStream oos = new ObjectOutputStream(fos);
	    oos.writeObject(user);
	    Log.i("Written", "user data written to file");
	    oos.close();
	    fos.close();
	}
	
}
