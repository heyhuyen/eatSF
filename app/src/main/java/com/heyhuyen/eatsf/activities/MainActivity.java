package com.heyhuyen.eatsf.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.heyhuyen.eatsf.R;
import com.heyhuyen.eatsf.adapters.PlaceAdapter;
import com.heyhuyen.eatsf.model.PlaceInfo;
import com.heyhuyen.eatsf.net.GoogleApiHttpClient;
import com.heyhuyen.eatsf.utils.ItemClickSupport;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.rvPlaces) RecyclerView rvPlaces;

    private PlaceAdapter placeAdapter;
    private List<PlaceInfo> places;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        this.places = new ArrayList<>();
        setupListView();
    }

    private void setupListView() {
        placeAdapter = new PlaceAdapter(places);
        rvPlaces.setAdapter(this.placeAdapter);
        rvPlaces.setLayoutManager(new LinearLayoutManager(this));

        // attach click handling
        ItemClickSupport.addTo(rvPlaces).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        PlaceInfo place = places.get(position);
                        launchPlaceDetailActivity(place);
                    }
                }
        );

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

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.miMap:
                Toast.makeText(MainActivity.this, "show map!", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.miList:
                Toast.makeText(MainActivity.this, "show list!", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* Activity Navigation */
    private void launchPlaceDetailActivity(PlaceInfo place) {
        Intent intent = new Intent(MainActivity.this, PlaceDetailActivity.class);
        intent.putExtra("placeInfo", Parcels.wrap(place));
        startActivity(intent);
    }
}
