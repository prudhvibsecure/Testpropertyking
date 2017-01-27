package com.tk.property.adapters;

import com.tk.property.fragments.FccApps;
import com.tk.property.fragments.FccHome;
import com.tk.property.fragments.FccMore;
import com.tk.property.fragments.FccShop;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by prudhvi on 07/12/2015.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

	CharSequence Titles[]; // This will Store the Titles of the Tabs which are
							// Going to be passed when ViewPagerAdapter is
							// created
	int NumbOfTabs; // Store the number of tabs, this will also be passed when
					// the ViewPagerAdapter is created
	// Build a Constructor and assign the passed Values to appropriate values in
	// the class

	public ViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
		super(fm);

		this.Titles = mTitles;
		this.NumbOfTabs = mNumbOfTabsumb;

	}

	// This method return the fragment for the every position in the View Pager
	@Override
	public Fragment getItem(int position) {

		if (position == 0) {
			FccHome tab1 = new FccHome();
			return tab1;
		}
		if (position == 1) {
			FccShop tab2 = new FccShop();
			return tab2;
		}
		if (position == 2) {
			FccApps tab3 = new FccApps();
			return tab3;
		} else {
			FccMore tab4 = new FccMore();
			return tab4;
		}

	}

	// This method return the titles for the Tabs in the Tab Strip

	@Override
	public CharSequence getPageTitle(int position) {
		return Titles[position];
	}

	// This method return the Number of tabs for the tabs Strip

	@Override
	public int getCount() {
		return NumbOfTabs;
	}
}