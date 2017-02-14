package com.heyhuyen.eatsf.activities;

import static com.heyhuyen.eatsf.adapters.ViewModePagerAdapter.LIST_MODE;
import static com.heyhuyen.eatsf.adapters.ViewModePagerAdapter.MAP_MODE;

import android.content.Intent;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.heyhuyen.eatsf.R;
import com.heyhuyen.eatsf.adapters.ViewModePagerAdapter;
import com.heyhuyen.eatsf.fragments.PlacesFragmentListener;
import com.heyhuyen.eatsf.model.PlaceInfo;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements PlacesFragmentListener {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.vpPager) ViewPager viewPager;
    private MenuItem miList;
    private MenuItem miMap;

    private FragmentPagerAdapter adapterViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        adapterViewPager = new ViewModePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapterViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        miMap = menu.findItem(R.id.miMap);
        miList = menu.findItem(R.id.miList);
        toggleViewModeIcons(MAP_MODE);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.miMap:
                viewPager.setCurrentItem(MAP_MODE);
                toggleViewModeIcons(MAP_MODE);
                return true;
            case R.id.miList:
                viewPager.setCurrentItem(LIST_MODE);
                toggleViewModeIcons(LIST_MODE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void toggleViewModeIcons(int mode) {
        miMap.setVisible(mode != MAP_MODE);
        miList.setVisible(mode != LIST_MODE);
    }

    public void onPlaceSelected(PlaceInfo place) {
        Intent intent = new Intent(MainActivity.this, PlaceDetailActivity.class);
        intent.putExtra("placeInfo", Parcels.wrap(place));
        startActivity(intent);
    }
}
