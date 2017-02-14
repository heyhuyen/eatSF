package com.heyhuyen.eatsf.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.heyhuyen.eatsf.R;

/**
 * Map fragment.
 */

public class PlaceMapFragment extends Fragment {

    PlacesFragmentListener listener;

    public static PlaceMapFragment newInstance() {
        PlaceMapFragment mapFragment = new PlaceMapFragment();
        return mapFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            listener = (PlacesFragmentListener) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
    }
}
