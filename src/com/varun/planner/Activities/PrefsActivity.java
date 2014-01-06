package com.varun.planner.Activities;

import com.varun.planner.Fragments.PrefsFragment;
import com.varun.planner.R;
import com.varun.planner.Objects.TabsPagerAdapter;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;



public class PrefsActivity extends Activity
{

	public void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(false);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new PrefsFragment())
                .commit();
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu)
        {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return super.onCreateOptionsMenu(menu);
    }

	public boolean onOptionsItemSelected(MenuItem item)
	{
		
		if (item.getItemId() == (R.id.done))
		{
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
		}
		
		return false;
	}

}
