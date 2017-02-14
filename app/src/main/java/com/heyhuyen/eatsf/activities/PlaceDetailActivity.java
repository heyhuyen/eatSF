package com.heyhuyen.eatsf.activities;

import static com.heyhuyen.eatsf.activities.MainActivity.PLACE_INFO_ARG;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.heyhuyen.eatsf.R;
import com.heyhuyen.eatsf.adapters.ReviewsAdapter;
import com.heyhuyen.eatsf.model.PlaceInfo;
import com.heyhuyen.eatsf.model.Review;
import com.heyhuyen.eatsf.net.GoogleApiHttpClient;
import com.heyhuyen.eatsf.utils.ImageUtils;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class PlaceDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String RESULT_KEY = "result";
    public static final String FORMATTED_ADDR_KEY = "formatted_address";
    public static final String INTL_PHONE_KEY = "international_phone_number";
    public static final String FORMATTED_PHONE_KEY = "formatted_phone_number";
    public static final String OPENING_HOURS_KEY = "opening_hours";
    public static final String OPEN_KEY = "open_now";
    public static final String HOURS_KEY = "weekday_text";
    public static final String WEBSITE_KEY = "website";
    public static final String GOOGLE_URL_KEY = "url";
    public static final String PRICE_KEY = "price_level";
    public static final String RATING_KEY = "rating";
    public static final String REVIEWS_KEY = "reviews";

    // Strings
    @BindString(R.string.open) String open;
    @BindString(R.string.closed) String closed;
    @BindString(R.string.free) String free;

    // Colors
    @BindColor(R.color.green) int green;
    @BindColor(R.color.red) int red;

    // Views
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.ivPlacePhoto) ImageView ivPlacePhoto;
    @BindView(R.id.tvPlaceName) TextView tvPlacename;
    @BindView(R.id.tvRating) TextView tvRating;
    @BindView(R.id.rbPlaceRating) RatingBar rbPlaceRating;
    @BindView(R.id.tvPriceLevel) TextView tvPriceLevel;

    @BindView(R.id.tvDirections) TextView tvDirections;
    @BindView(R.id.tvCall) TextView tvCall;
    @BindView(R.id.tvWebsite) TextView tvWebsite;

    @BindView(R.id.tvAddress) TextView tvAddress;
    @BindView(R.id.tvPhoneNumber) TextView tvPhoneNumber;
    @BindView(R.id.llHours) LinearLayout llHours;
    @BindView(R.id.tvOpenNow) TextView tvOpenNow;
    @BindView(R.id.tvHours) TextView tvHours;
    @BindView(R.id.tvWebAddress) TextView tvWebAddress;
    @BindView(R.id.tvGoogleUrl) TextView tvGoogleUrl;

    @BindView(R.id.tvReviewsRating) TextView tvReviewsRating;
    @BindView(R.id.rbPlaceReviewsRating) RatingBar rbPlaceReviewsRating;
    @BindView(R.id.rvReviews) RecyclerView rvReviews;

    private PlaceInfo place;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        place = Parcels.unwrap(getIntent().getParcelableExtra(PLACE_INFO_ARG));
        setupViewsWithInitialInfo();

        // setup map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.frPlaceMapView);
        mapFragment.getMapAsync(this);

        // fetch place data
        GoogleApiHttpClient.getPlaceDetails(place.getPlaceId(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.has(RESULT_KEY)) {
                        setupViewsWithDetailInfo(response.getJSONObject(RESULT_KEY));
                    } else {
                        Log.e("ERROR", String.format("Null place id : %s", place.getPlaceId()));
                    }
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

    /**
     * Called when the map is ready.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.addMarker(new MarkerOptions()
                .position(place.getLatlng())
                .title(place.getName())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatlng(), 16f));
    }

    private void setupViewsWithInitialInfo() {
        getSupportActionBar().setTitle(place.getName());
        tvPlacename.setText(place.getName());
        String ratingString = String.format("%.1f", place.getRating());
        tvRating.setText(ratingString);
        tvReviewsRating.setText(ratingString);
        rbPlaceRating.setRating((float) place.getRating());
        rbPlaceReviewsRating.setRating((float) place.getRating());

        ImageUtils.loadImage(
                ivPlacePhoto,
                GoogleApiHttpClient.getPlacePhotoUrl(place.getPhotoRef()),
                R.drawable.ic_fork_knife);
    }

    private void setupViewsWithDetailInfo(JSONObject data) throws JSONException {
        // price level
        if (data.has(PRICE_KEY)) {
            int priceLevel = data.getInt(PRICE_KEY);
            if (priceLevel == 0) {
                tvPriceLevel.setText(free);
            } else {
                tvPriceLevel.setText(new String(new char[priceLevel]).replace("\0", "$"));
            }
        } else {
            tvPriceLevel.setVisibility(View.GONE);
        }

        // address
        if (data.has(FORMATTED_ADDR_KEY)) {
            String address = data.getString(FORMATTED_ADDR_KEY);
            tvAddress.setText(address);
            // open up maps with directions
            tvAddress.setOnClickListener(v -> onAddressClick(address));
            tvDirections.setOnClickListener(v -> onDirectionsClick(address));
        }

        // phone number
        if (data.has(INTL_PHONE_KEY)) {
            setupPhoneNumber(data.getString(INTL_PHONE_KEY));
        } else if (data.has(FORMATTED_PHONE_KEY)) {
            setupPhoneNumber(data.getString(FORMATTED_PHONE_KEY));
        }

        // website
        if (data.has(WEBSITE_KEY)) {
            // use website if it is available, otherwise default to google url below
            String website = data.getString(WEBSITE_KEY);
            tvWebAddress.setText(website);
            // open up web browser
            tvWebAddress.setOnClickListener(v -> onWebAddressClick(website));
            tvWebsite.setOnClickListener(v -> onWebAddressClick(website));
        } else {
            tvWebsite.setVisibility(View.GONE);
            tvWebAddress.setVisibility(View.GONE);
            // google url
            if (data.has(GOOGLE_URL_KEY)) {
                // google url is basically just map view with the location coordinates.
                tvGoogleUrl.setVisibility(View.VISIBLE);
                String googleUrl = data.getString(GOOGLE_URL_KEY);
                tvGoogleUrl.setText(data.getString(GOOGLE_URL_KEY));
                // open up maps
                tvGoogleUrl.setOnClickListener(v -> onGoogleUrlClick(googleUrl));
                tvWebsite.setOnClickListener(v -> onGoogleUrlClick(googleUrl));
            }
        }

        // hours
        if (data.has(OPENING_HOURS_KEY)) {
            llHours.setVisibility(View.VISIBLE);
            JSONObject openingHours = data.getJSONObject(OPENING_HOURS_KEY);
            boolean openNow = openingHours.getBoolean(OPEN_KEY);
            if (openNow) {
                tvOpenNow.setText(open);
                tvOpenNow.setTextColor(green);
            } else {
                tvOpenNow.setText(closed);
                tvOpenNow.setTextColor(red);
            }

            // hours
            JSONArray rawHours = openingHours.getJSONArray(HOURS_KEY);
            ArrayList<String> hoursList = new ArrayList<>();
            for (int i = 0; i < rawHours.length(); i++) {
                hoursList.add(rawHours.getString(i));
            }
            String hours = TextUtils.join("\n", hoursList);
            tvHours.setText(hours);
            tvOpenNow.setOnClickListener(v -> {
                tvOpenNow.setVisibility(View.GONE);
                tvHours.setVisibility(View.VISIBLE);
            });
            tvHours.setOnClickListener(v -> {
                tvHours.setVisibility(View.GONE);
                tvOpenNow.setVisibility(View.VISIBLE);
            });
        }

        // reviews
        if (data.has(REVIEWS_KEY)) {
            ArrayList<Review> reviews =
                    Review.getReviewsFromJSONArray(data.getJSONArray(REVIEWS_KEY));
            ReviewsAdapter adapter = new ReviewsAdapter(PlaceDetailActivity.this, reviews);
            rvReviews.setAdapter(adapter);
            rvReviews.setLayoutManager(new LinearLayoutManager(PlaceDetailActivity.this));
        } else {
            rvReviews.setVisibility(View.GONE);
        }
    }

    private void setupPhoneNumber(String phone) {
        tvPhoneNumber.setVisibility(View.VISIBLE);
        tvCall.setVisibility(View.VISIBLE);
        tvPhoneNumber.setText(phone);

        // open phone intent
        tvPhoneNumber.setOnClickListener(v -> onPhoneClick(phone));
        tvCall.setOnClickListener(v -> onPhoneClick(phone));
    }

    /* Navigation */
    private void onDirectionsClick(String address) {
        String data = String.format("google.navigation:q=%s", address);
        launchMapsIntent(data);
    }

    private void onAddressClick(String address) {
        String data = String.format("geo:0,0?q=%s(%s)", address, place.getName());
        launchMapsIntent(data);
    }

    private void onPhoneClick(String phoneNumber) {
        String uri = String.format("tel:%s", phoneNumber);
        Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
        phoneIntent.setData(Uri.parse(uri));
        if (phoneIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(phoneIntent);
        }
    }

    private void onWebAddressClick(String webAddress) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webAddress));
        if (browserIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(browserIntent);
        }
    }

    private void onGoogleUrlClick(String googleUrl) {
        launchMapsIntent(googleUrl);
    }

    private void launchMapsIntent(String data) {
        Intent mapsIntent = new Intent();
        mapsIntent.setAction(Intent.ACTION_VIEW);
        mapsIntent.setPackage("com.google.android.apps.maps");
        mapsIntent.setData(Uri.parse(data));
        if (mapsIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapsIntent);
        }
    }
}
