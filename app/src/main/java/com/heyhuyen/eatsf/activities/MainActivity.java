package com.heyhuyen.eatsf.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.heyhuyen.eatsf.R;
import com.heyhuyen.eatsf.adapters.PlaceAdapter;
import com.heyhuyen.eatsf.model.PlaceInfo;
import com.heyhuyen.eatsf.net.GoogleApiHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rvPlaces) RecyclerView rvPlaces;

    private PlaceAdapter placeAdapter;
    private List<PlaceInfo> places;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        this.places = new ArrayList<>();
        setupListView();
    }

    private void setupListView() {
        placeAdapter = new PlaceAdapter(places);
        rvPlaces.setAdapter(this.placeAdapter);
        rvPlaces.setLayoutManager(new LinearLayoutManager(this));
        populatePlaces();
        // TODO: swipe to refresh
        // TODO: endless scrolling
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
                Toast.makeText(MainActivity.this, "error: failed populating places",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
