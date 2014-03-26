package com.varun.planner.Activities;

import java.util.ArrayList;

import com.varun.planner.R;
import com.varun.planner.Fragments.CalendarFragment;
import com.varun.planner.Fragments.ItemsFragment;
import com.varun.planner.Objects.Assignment;
import com.varun.planner.Objects.TabsPagerAdapter;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

public class MainActivity extends Activity implements OnNavigationListener
{
	private ActionBar actionBar;
	private ArrayAdapter<String> mSpinnerAdapter;
	 

	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		String urg = sp.getString("urgency", "2");
		int days = Integer.parseInt(urg);
		
		Assignment.changeUrgency(days);
		Log.i("PREAD",urg );
		
		// Set up the action bar to show a dropdown list.
		actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        actionBar = getActionBar();
        
        ArrayList<String> itemList = new ArrayList<String>();
        itemList.add("Assignments");
        itemList.add("Calendar");
        
        mSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, itemList);
        
        actionBar.setListNavigationCallbacks(mSpinnerAdapter, this);

	}

	/**
	 * Backward-compatible version of {@link ActionBar#getThemedContext()} that
	 * simply returns the {@link android.app.Activity} if
	 * <code>getThemedContext</code> is unavailable.
	 */
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private Context getActionBarThemedContextCompat()
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
		{
			return getActionBar().getThemedContext();
		}
		else
		{
			return this;
		}
	}
	@Override
	public boolean onCreateOptionsMenu(final Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == (R.id.itemAdd))
		{
			Intent intent = new Intent(this, AddNewActivity.class);
			startActivity(intent);
			return true;
		}
		else if (item.getItemId() == (R.id.action_settings))
		{
            Intent intent = new Intent(this, PrefsActivity.class);
            startActivity(intent);
            return true;
		}
		return false;
	}
	 @Override
	  public boolean onNavigationItemSelected(int position, long id) {
	    // When the given dropdown item is selected, show its contents in the
	    // container view.
		Fragment fragment;
		if(position == 0)
			 fragment = new ItemsFragment();
		else 
			fragment = new CalendarFragment();
	    
	    
	    getFragmentManager().beginTransaction()
	        .replace(R.id.fragment_container, fragment).commit();
	    return true;
	  }

}
