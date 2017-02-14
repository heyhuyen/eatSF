package com.heyhuyen.eatsf.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.heyhuyen.eatsf.R;
import com.heyhuyen.eatsf.adapters.PlaceCardPagerAdapter;
import com.heyhuyen.eatsf.model.PlaceInfo;
import com.heyhuyen.eatsf.net.GoogleApiHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cz.msebera.android.httpclient.Header;

/**
 * Map fragment.
 */

public class PlaceMapFragment extends Fragment {

    @BindView(R.id.vpPlaces) ViewPager vpPlaces;

    private Unbinder unbinder;
    private List<PlaceInfo> places;
    private PlaceCardPagerAdapter placeCardPagerAdapter;

    private SupportMapFragment googleMapFragment;
    private List<Marker> markers;
    private LatLngBounds mapBounds;
    private BitmapDescriptor defaultMarker;
    private BitmapDescriptor selectedPlaceMarker;
    private GoogleMap googleMap;
    private HashMap<String, Integer> markerMap; // marker id : place index

    public static PlaceMapFragment newInstance() {
        PlaceMapFragment mapFragment = new PlaceMapFragment();
        return mapFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        places = new ArrayList<>();
        markers = new ArrayList<>();
        markerMap = new HashMap<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, parent, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        defaultMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
        selectedPlaceMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
        MapsInitializer.initialize(getActivity().getApplicationContext());
        googleMapFragment = ((SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.frPlacesMapView));
        if (googleMapFragment != null) {
            googleMapFragment.getMapAsync(map -> loadMap(map));
        } else {
            Log.e("ERROR", "frPlacesMapView fragment was null!");
        }
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void loadMap(GoogleMap googleMap) {
        this.googleMap = googleMap;
        if (this.googleMap != null) {
            // Map is ready
            this.googleMap.setOnMarkerClickListener(marker -> {
                // scroll to place card for marker id
                vpPlaces.setCurrentItem(markerMap.get(marker.getId()), true);
                return true;
            });

            populatePlaces();
        } else {
            Log.e("ERROR", "google map was null!");
        }
    }

    private void plotMap() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (PlaceInfo place : places) {
            Marker marker =
                    googleMap.addMarker(
                            new MarkerOptions()
                                    .position(place.getLatlng())
                                    .icon(defaultMarker)
                    );
            markers.add(marker);
            markerMap.put(marker.getId(), markers.size() - 1);
            builder.include(marker.getPosition());
        }
        markers.get(0).setIcon(selectedPlaceMarker);
        mapBounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(mapBounds, 100);
        googleMap.animateCamera(cu);
    }

    private void setupPlaceCards() {
        placeCardPagerAdapter = new PlaceCardPagerAdapter(getChildFragmentManager(), places);
        vpPlaces.setAdapter(placeCardPagerAdapter);
        vpPlaces.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int pos, float posOffset, int posOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                for (Marker marker : markers) {
                    marker.setIcon(defaultMarker);
                }
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(mapBounds, 100);
                googleMap.animateCamera(cu);
                Marker marker = markers.get(position);
                marker.setIcon(selectedPlaceMarker);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void populatePlaces() {
        GoogleApiHttpClient.getNearbyPlaces(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    places.addAll(PlaceInfo.getPlacesFromJSONArray(
                            response.getJSONArray("results")));
                    plotMap();
                    setupPlaceCards();
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
