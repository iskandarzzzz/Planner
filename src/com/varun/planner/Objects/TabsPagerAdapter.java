package com.varun.planner.Objects;


import com.varun.planner.Fragments.CalendarFragment;
import com.varun.planner.Fragments.ItemsFragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;
import android.util.Log;

public class TabsPagerAdapter extends FragmentPagerAdapter {
	 
    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }
 
    @Override
    public Fragment getItem(int index) 
    {
    	Log.i("INFO", "Adapter getItemCalled");
        switch (index)
        {
	        case 0:
	            // Top Rated fragment activity
	            return new ItemsFragment();
	        case 1:
	            // Games fragment activity
	            return new CalendarFragment();
        }
 
        return null;
    }
 
    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 2;
    }
 
}