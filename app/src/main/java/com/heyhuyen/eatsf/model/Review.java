package com.heyhuyen.eatsf.model;

import static android.R.attr.author;

import static com.heyhuyen.eatsf.activities.PlaceDetailActivity.RATING_KEY;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Date;

/**
 * Model for a google place review.
 */
@Parcel
public class Review {
    private static final String AUTHOR_NAME_KEY = "author_name";
    private static final String TEXT_KEY = "text";
    private static final String RELATIVE_TIME_KEY = "relative_time_description";
    private static final String TIME_KEY = "time";

    public static final int INVALID_TIMESTAMP = -1;

    // fields must be public
    String author;
    double rating;
    String text;
    long timestamp = INVALID_TIMESTAMP;
    String relativeTimestamp;

    public Review() {
        // empty constructor needed by the Parceler library
    }

    public Review(JSONObject jsonObject) {
        super();
        try {
            setAuthor(jsonObject.getString(AUTHOR_NAME_KEY));
            setRating(jsonObject.getDouble(RATING_KEY));
            setText(jsonObject.getString(TEXT_KEY));
            if (jsonObject.has(RELATIVE_TIME_KEY)) {
                setRelativeTimestamp(jsonObject.getString(RELATIVE_TIME_KEY));
            } else {
                setTimestamp(new Date(jsonObject.getLong(TIME_KEY)).getTime());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Review> getReviewsFromJSONArray(JSONArray jsonArray) {
        ArrayList<Review> reviews = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++){
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Review review = new Review(jsonObject);
                reviews.add(review);
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        return reviews;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getRelativeTimestamp() {
        return relativeTimestamp;
    }

    public void setRelativeTimestamp(String relativeTimestamp) {
        this.relativeTimestamp = relativeTimestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
