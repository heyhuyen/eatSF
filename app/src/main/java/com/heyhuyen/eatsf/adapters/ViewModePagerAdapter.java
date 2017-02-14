package com.heyhuyen.eatsf.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.heyhuyen.eatsf.fragments.PlaceMapFragment;
import com.heyhuyen.eatsf.fragments.PlaceListFragment;

/**
 * ViewPager adapter for places view modes.
 */

public class ViewModePagerAdapter extends FragmentPagerAdapter {
    public static final int MAP_MODE = 0;
    public static final int LIST_MODE = 1;
    private static int NUM_ITEMS = 2;

    private PlaceMapFragment mapFragment;
    private PlaceListFragment listFragment;

    public ViewModePagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case MAP_MODE:
                if (mapFragment == null) {
                    mapFragment = PlaceMapFragment.newInstance();
                }
                return mapFragment;
            case LIST_MODE:
                if (listFragment == null) {
                    listFragment = PlaceListFragment.newInstance();
                }
                return listFragment;
            default:
                return null;
        }
    }

}
