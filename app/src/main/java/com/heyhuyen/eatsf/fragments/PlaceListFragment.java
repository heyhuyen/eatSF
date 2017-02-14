package com.heyhuyen.eatsf.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.heyhuyen.eatsf.R;
import com.heyhuyen.eatsf.adapters.PlaceAdapter;
import com.heyhuyen.eatsf.model.PlaceInfo;
import com.heyhuyen.eatsf.net.GoogleApiHttpClient;
import com.heyhuyen.eatsf.utils.ItemClickSupport;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cz.msebera.android.httpclient.Header;

/**
 * List Fragment.
 */

public class PlaceListFragment extends Fragment {

    @BindView(R.id.rvPlaces) RecyclerView rvPlaces;

    private PlacesFragmentListener listener;
    private PlaceAdapter placeAdapter;
    private List<PlaceInfo> places;
    private Unbinder unbinder;

    public static PlaceListFragment newInstance() {
        PlaceListFragment listFragment = new PlaceListFragment();
        return listFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            listener = (PlacesFragmentListener) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        places = new ArrayList<>();
        placeAdapter = new PlaceAdapter(places);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, parent, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        rvPlaces.setAdapter(placeAdapter);
        rvPlaces.setLayoutManager(new LinearLayoutManager(getActivity()));

        // attach click handling
        ItemClickSupport.addTo(rvPlaces).setOnItemClickListener(
                (recyclerView, position, v) -> {
                    PlaceInfo place = places.get(position);
                    listener.onPlaceSelected(place);
                }
        );

        populatePlaces();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void populatePlaces() {
        GoogleApiHttpClient.getNearbyPlaces(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    places.addAll(PlaceInfo.getPlacesFromJSONArray(
                            response.getJSONArray("results")));
                    placeAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                // TODO: Show error
                Log.e("ERROR", t.toString());
            }
        });
    }

}
