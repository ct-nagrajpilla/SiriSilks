package com.aniapps.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.aniapps.siri.Fragment_Account;

public class PagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 3;


    public PagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return Fragment_Account.newInstance("","");
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return Fragment_Account.newInstance("","");
            case 2: // Fragment # 1 - This will show SecondFragment
                return Fragment_Account.newInstance("","");
            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }

}
