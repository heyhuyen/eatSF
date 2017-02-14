package com.heyhuyen.eatsf.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.heyhuyen.eatsf.fragments.PlaceCardFragment;
import com.heyhuyen.eatsf.model.PlaceInfo;

import java.util.List;

/**
 * ViewPager adapter for place cards.
 */

public class PlaceCardPagerAdapter extends FragmentPagerAdapter {

    private List<PlaceInfo> places;

    public PlaceCardPagerAdapter(FragmentManager fm, List<PlaceInfo> places) {
        super(fm);
        this.places = places;
    }


    @Override
    public Fragment getItem(int position) {
        return PlaceCardFragment.newInstance(places.get(position), position);
    }

    @Override
    public int getCount() {
        return places.size();
    }
}
