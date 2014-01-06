package com.varun.planner.Activities;

import com.varun.planner.R;
import com.varun.planner.Objects.Assignment;
import com.varun.planner.Objects.TabsPagerAdapter;

import android.annotation.TargetApi;
import android.app.ActionBar;
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

public class MainActivity extends Activity implements TabListener
{
	private ViewPager viewPager;
	private Fragment passed;
	private ActionBar actionBar;
	private TabsPagerAdapter mAdapter;

	
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
		
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        mAdapter = new TabsPagerAdapter(getFragmentManager());
 
        viewPager.setAdapter(mAdapter);
        
		Tab tab1 = actionBar.newTab();
		tab1.setText("Assignments");
		tab1.setTabListener(this);
		actionBar.addTab(tab1);
		
		Tab tab2 = actionBar.newTab();
		tab2.setText("Calendar");
		tab2.setTabListener(this);
		actionBar.addTab(tab2);



		 viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
		 {
			 
	            @Override
	            public void onPageSelected(int position)
	            {
	                // on changing the page
	                // make respected tab selected
	                actionBar.setSelectedNavigationItem(position);
	            }
	 
	            @Override
	            public void onPageScrolled(int arg0, float arg1, int arg2) {
	            }
	 
	            @Override
	            public void onPageScrollStateChanged(int arg0) {
	            }
	        });
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
	public void onTabReselected(Tab arg0, FragmentTransaction arg1)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft)
	{
		Log.i("INFO", "Page " + tab.getPosition()+" Selected");
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft)
	{
		// TODO Auto-generated method stub
	}

}
