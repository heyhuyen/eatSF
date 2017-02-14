package com.heyhuyen.eatsf.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.heyhuyen.eatsf.R;
import com.heyhuyen.eatsf.model.PlaceInfo;
import com.heyhuyen.eatsf.net.GoogleApiHttpClient;
import com.heyhuyen.eatsf.utils.ImageUtils;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlaceDetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.ivPlacePhoto) ImageView ivPlacePhoto;
    @BindView(R.id.tvPlaceName) TextView tvPlacename;
    @BindView(R.id.rbPlaceRating) RatingBar rbPlaceRating;
    @BindView(R.id.tvPlaceId) TextView tvPlaceId;

    private PlaceInfo place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        place = Parcels.unwrap(getIntent().getParcelableExtra("placeInfo"));
        getSupportActionBar().setTitle(place.getName());
        tvPlacename.setText(place.getName());
        rbPlaceRating.setRating((float) place.getRating());
        tvPlaceId.setText(place.getPlaceId());
        ImageUtils.loadImage(
                ivPlacePhoto,
                GoogleApiHttpClient.getPlacePhotoUrl(place.getPhotoRef()),
                R.drawable.ic_fork_knife);

        // fetch place detail data
    }
}
