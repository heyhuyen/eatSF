package com.heyhuyen.eatsf.model;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Model for basic place info.
 */

@Parcel
public class PlaceInfo {
    private static final String NAME_KEY = "name";
    private static final String PLACE_ID_KEY = "place_id";
    private static final String RATING_KEY = "rating";
    private static final String PHOTOS_KEY = "photos";
    private static final String PHOTO_REF_KEY = "photo_reference";
    private static final String GEOMETRY_KEY = "geometry";
    private static final String LOCATION_KEY = "location";
    private static final String LATITUDE_KEY = "lat";
    private static final String LONGITUDE_KEY = "lng";

    // fields must be public for Parceler library
    String name;
    double rating;
    String placeId;
    String photoRef;
    LatLng latlng;

    public PlaceInfo() {
        // empty constructor needed by the Parceler library
    }

    public PlaceInfo(JSONObject place) {
        super();
        try {
            this.name = place.getString(NAME_KEY);
            this.placeId = place.getString(PLACE_ID_KEY);
            if (place.has(RATING_KEY)) {
                this.rating = place.getDouble(RATING_KEY);
            }
            if (place.has(PHOTOS_KEY)) {
                JSONArray photos = place.getJSONArray(PHOTOS_KEY);
                if (photos.length() > 0) {
                    this.photoRef = photos.getJSONObject(0).getString(PHOTO_REF_KEY);
                }
            }
            JSONObject geometry = place.getJSONObject(GEOMETRY_KEY);
            JSONObject location = geometry.getJSONObject(LOCATION_KEY);
            this.latlng = new LatLng(location.getDouble(LATITUDE_KEY),
                    location.getDouble(LONGITUDE_KEY));
        } catch (JSONException e) {
            Log.d("Parse place failed", e.toString());
        }
    }

    /**
     * Parses a jsonArray response from a Google Place API request.
     */
    public static ArrayList<PlaceInfo> getPlacesFromJSONArray(JSONArray jsonArray) {
        ArrayList<PlaceInfo> suggestionPlaces = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++){
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                PlaceInfo place = new PlaceInfo(jsonObject);
                suggestionPlaces.add(place);
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        return suggestionPlaces;
    }

    public String getName() {
        return name;
    }

    public double getRating() {
        return rating;
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getPhotoRef() {
        return photoRef;
    }

    public LatLng getLatlng() {
        return latlng;
    }
}

