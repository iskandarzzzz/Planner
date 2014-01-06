package com.varun.planner.Activities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.varun.planner.Objects.Assignment;
import com.varun.planner.Objects.User;
import com.varun.planner.R;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.CalendarContract.Events;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class AddNewActivity extends Activity
{
	Assignment ass;

	EditText titleEdit, descEdit;
	AutoCompleteTextView classEdit;

	User user;
	DatePicker dp;
	TimePicker tp;
	Button record;
	String title, desc, cla;
	boolean startRec;
	int day, month, year, hour, minute;
	private MediaRecorder mRecorder = null;
	String mFileName;
	boolean hasRecorded = false;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_addnew_layout);
		generate();
	}

	public void generate()
	{

		final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		user = (User) ReadFromFile();
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, user.getClassNames());
		titleEdit = (EditText) findViewById(R.id.TitleEntry);
		descEdit = (EditText) findViewById(R.id.descEntry);
		classEdit = (AutoCompleteTextView) findViewById(R.id.classEntry);
		classEdit.setAdapter(adapter);
		dp = (DatePicker) findViewById(R.id.datePicker);
		tp = (TimePicker) findViewById(R.id.timePicker);
		tp.setIs24HourView(true);

		record = (Button) findViewById(R.id.record);
		startRec = true;
		record.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (startRec)
				{
					record.setText("Recording. Tap to stop.");
					record.setTextColor(Color.RED);
				}
				else if (!startRec && hasRecorded)
				{
					record.setTextColor(Color.BLACK);
					record.setText("Not happy? Tap to retry.");
				}
				else if (!startRec)
				{
					record.setTextColor(Color.BLACK);
					record.setText("Tap to record a voice memo");
				}

				if (hasRecorded && startRec)
				{
					deletePrev();
				}
				onRecord(startRec);
				startRec = !startRec;
				hasRecorded = true;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.addnew, menu);
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == (R.id.accept))
			addAssignment();
		
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
        
		return true;
	}

	private void addAssignment()
	{
		String calKey = "calendaradd";
		boolean success = false;
		title = titleEdit.getText().toString();
		desc = descEdit.getText().toString();
		cla = classEdit.getText().toString();

		day = dp.getDayOfMonth();
		month = dp.getMonth();
		year = dp.getYear();

		hour = tp.getCurrentHour();
		minute = tp.getCurrentMinute();

		if (hasRecorded && mFileName != null)
			ass = new Assignment(title, desc, cla, year, month, day, hour,
					minute, mFileName);
		else
			ass = new Assignment(title, desc, cla, year, month, day, hour,
					minute);

		
		user.addAssignment(ass);
		
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		if (sp.getBoolean(calKey, false))
			addToUserCal(ass);
		try
		{
			WriteToFile(user);
			success = true;
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		if (success)
			Toast.makeText(this, "Added!", Toast.LENGTH_LONG).show();
		
		else
			Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();

	}

	private void addToUserCal(Assignment Ass)
	{
		long calID = 1;
		String title = Ass.getTitle();
		String cl = Ass.getAssClass();
		String desc = Ass.getDescription();
		GregorianCalendar sTime = Ass.getDueDate();
		GregorianCalendar eTime = addHour(sTime);
		TimeZone tz = TimeZone.getDefault();
		String timezone = tz.getID();

		ContentValues vals = new ContentValues();

		ContentResolver cr = getContentResolver();

		vals.put(Events.TITLE, title);
		vals.put(Events.EVENT_LOCATION, cl);
		vals.put(Events.DESCRIPTION, desc);
		vals.put(Events.EVENT_TIMEZONE, timezone);
		vals.put(Events.DTSTART, sTime.getTimeInMillis());
		vals.put(Events.DTEND, eTime.getTimeInMillis());
		vals.put(Events.CALENDAR_ID, calID);

		Uri uri = cr.insert(Events.CONTENT_URI, vals);

		Log.i("Calendar", "event apparently added to calendar");

	}

	private GregorianCalendar addHour(GregorianCalendar c)
	{
		GregorianCalendar later = (GregorianCalendar) c.clone();
		int hour = later.get(Calendar.HOUR);
		later.set(Calendar.HOUR, hour + 1);

		return later;
	}

	@SuppressWarnings("rawtypes")
	private User ReadFromFile()
	{
		User UserRead = new User();
		FileInputStream fis;
		try {
		    fis = openFileInput("UserData");
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
		FileOutputStream fos = openFileOutput("UserData", Context.MODE_PRIVATE);
	    ObjectOutputStream oos = new ObjectOutputStream(fos);
	    oos.writeObject(user);
	    Log.i("Written", "user data written to file");
	    oos.close();
	    fos.close();
	}

	private void onRecord(boolean start)
	{
		if (start)
		{
			startRecording();
		}
		else
		{
			stopRecording();
		}
	}

	private void startRecording()
	{
		mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
		mFileName += "/" + (Calendar.getInstance().getTimeInMillis()) + ".3gp";
		mRecorder = new MediaRecorder();
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mRecorder.setOutputFile(mFileName);
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

		try
		{
			mRecorder.prepare();
		} catch (IOException e)
		{
			Log.e("AudioRecord", "prepare() failed");
		}

		mRecorder.start();
	}

	private void stopRecording()
	{
		mRecorder.stop();
		mRecorder.release();
		mRecorder = null;
	}

	private boolean deletePrev()
	{
		File audio = new File(mFileName);
		return audio.delete();
	}

}
