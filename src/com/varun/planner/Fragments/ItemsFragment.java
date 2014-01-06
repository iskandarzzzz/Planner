package com.varun.planner.Fragments;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import com.varun.planner.Objects.Assignment;
import com.varun.planner.Objects.AssignmentsAdapter;
import com.varun.planner.Objects.User;
import com.varun.planner.R;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class ItemsFragment extends Fragment
{
	View rootView;
	int p;
	TextView UA;
	TextView TA;
	ListView lstTest;
	AssignmentsAdapter arrayAdapter;
	User user; 
	int totalassignments;
	int urgentassignments;
	
	public ItemsFragment()
	{
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		Typeface Black = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Black.ttf");
		Typeface boldCon = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-BoldCondensed.ttf");
		
		
		rootView = inflater.inflate(R.layout.fragment_items_layout, container, false);
		
		
		lstTest = (ListView)rootView.findViewById(R.id.lstText);
		
		
		totalassignments = 0;
		urgentassignments = 0;
		user = new User();
		user = ReadFromFile();
		
		if(user!=null)
		{
			totalassignments = user.getNumAssignments();
			urgentassignments = user.getUrgents();
		}
		
		UA = (TextView)rootView.findViewById(R.id.UrgentAssignments);
		TA = (TextView)rootView.findViewById(R.id.TotalAssignments);
		
		((TextView) rootView.findViewById(R.id.TotalHeader)).setTypeface(boldCon);
		((TextView) rootView.findViewById(R.id.UrgentHeader)).setTypeface(boldCon);

		
		UA.setTypeface(Black);
		TA.setTypeface(Black);
		
		UA.setText(String.valueOf(urgentassignments));
		TA.setText(String.valueOf(totalassignments));
		
		if(urgentassignments>0)
			UA.setTextColor(Color.parseColor("#FF4444"));
		else
			UA.setTextColor(Color.BLACK);
		
					
		arrayAdapter = new AssignmentsAdapter(getActivity(), R.layout.listitems, user.getAssignments());
		lstTest.setAdapter(arrayAdapter);
		arrayAdapter.notifyDataSetChanged();
		
		lstTest.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
		    public void onItemClick(@SuppressWarnings("rawtypes") AdapterView parent, View v, int position, long id)
		    {
		    	p=position;
		    	final Assignment selected = (Assignment) lstTest.getItemAtPosition(position);
		    	
		    	
		    	
		    	String message = selected.getDescription()+"\n\n"+selected.formatCal();
		    	
		    	AlertDialog.Builder bldr = new AlertDialog.Builder(getActivity());
		    	bldr.setTitle(selected.getAssClass()+": "+selected.getTitle());
		    	bldr
		    		.setMessage(message)
		    		.setCancelable(false)
		    		.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							try {
								deleteFromList(selected, rootView);
							} catch (IOException e) {
								e.printStackTrace();
							}
							dialog.cancel();
							arrayAdapter.notifyDataSetChanged();
						}
					})
					.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							// if this button is clicked, just close
							// the dialog box and do nothing
							dialog.cancel();
						}
					});
		    	
		    	AlertDialog UponSelected = bldr.create();
		    	UponSelected.show();
		    }
		   
		});
		return rootView;
	}
	private void deleteFromList(Assignment atodel, View v) throws IOException
	{
		
		user.deleteAssignment(atodel);
		WriteToFile(user);
		
		UA = (TextView)v.findViewById(R.id.UrgentAssignments);
		TA = (TextView)v.findViewById(R.id.TotalAssignments);
		
		totalassignments = user.getNumAssignments();
		urgentassignments = user.getUrgents();
		
		UA.setText(String.valueOf(urgentassignments));
		TA.setText(String.valueOf(totalassignments));
		
		if(urgentassignments>0)
			UA.setTextColor(Color.RED);
		else
			UA.setTextColor(Color.BLACK);
		arrayAdapter.remove(atodel);
		arrayAdapter.notifyDataSetChanged();
	}
	private int getUrgent(ArrayList<Assignment> a)
	{
		int urgent = 0;
		if(a.size()>0)
		{
			for(Assignment ass : a)
			{
				if(ass.isUrgent())
					urgent++;
			}
		}
		return urgent;
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
	
	public void setTypeface(TextView t, String path)
	{
		Typeface TF = Typeface.createFromAsset(getActivity().getAssets(), "fonts/"+path);
 	    t.setTypeface(TF);
	}
}
