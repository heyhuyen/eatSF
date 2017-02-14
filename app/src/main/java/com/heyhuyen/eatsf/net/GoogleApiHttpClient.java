package com.heyhuyen.eatsf.net;

import android.text.TextUtils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * HTTP client for Google Places API
 */

public class GoogleApiHttpClient {

    private static AsyncHttpClient client = new AsyncHttpClient();

    private static final String GOOGLE_PLACES_SEARCH_API_KEY =
            "AIzaSyB-bpw0ollWA5AKpT11Y2CL2qPFs4kC_dk";

    // API endpoints
    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/";
    private static final String NEARBY_SEARCH_URL =
            String.format("%s%s", BASE_URL, "nearbysearch/json?");
    private static final String PLACE_PHOTO_URL_FORMAT =
            String.format("%s%s%s", BASE_URL, "photo?maxwidth=400&photoreference=%s&key=",
                    GOOGLE_PLACES_SEARCH_API_KEY);
    public static final String PLACE_DETAILS_URL = String.format("%s%s", BASE_URL, "details/json?");

    // Hard coded search params for "Restaurants in SF"
    private static final int SF_RADIUS = 5633; // ~ 3.5 miles in meters
    private static final String SF_LAT_LONG = "37.7749,-122.4194";
    private static final String TYPE_RESTAURANT = "restaurant";

    public static AsyncHttpClient getInstance() {
        return client;
    }

    public static void get(String url, RequestParams params,
            AsyncHttpResponseHandler responseHandler) {

        client.get(url, params, responseHandler);
    }

    public static void getNearbyPlaces(JsonHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("key", GOOGLE_PLACES_SEARCH_API_KEY);
        params.put("location", SF_LAT_LONG);
        params.put("radius", SF_RADIUS);
        params.put("type", TYPE_RESTAURANT);
        get(NEARBY_SEARCH_URL, params, handler);
    }

    public static String getPlacePhotoUrl(String reference) {
        if (reference == null || TextUtils.isEmpty(reference)) {
            return reference;
        } else {
            return String.format(PLACE_PHOTO_URL_FORMAT, reference);
        }
    }

    public static void getPlaceDetails(String placeId, JsonHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("placeid", placeId);
        params.put("key", GOOGLE_PLACES_SEARCH_API_KEY);
        get(PLACE_DETAILS_URL, params, handler);
    }
}

