package com.heyhuyen.eatsf.fragments;

import static com.heyhuyen.eatsf.net.GoogleApiHttpClient.getPlacePhotoUrl;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.heyhuyen.eatsf.R;
import com.heyhuyen.eatsf.model.PlaceInfo;
import com.heyhuyen.eatsf.utils.ImageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Fragment for place, rendered as a card.
 */
public class PlaceCardFragment extends Fragment {
    private static final String POSITION_ARG = "position";
    private static final String PLACE_PHOTO_ARG = "photo";
    private static final String PLACE_NAME_ARG = "name";
    private static final String PLACE_RATING_ARG = "rating";
    private static final String PLACE_ID_ARG = "placeId";
    private static final String PLACE_LAT_ARG = "lat";
    private static final String PLACE_LONG_ARG = "long";

    @BindView(R.id.ivPlacePhoto) ImageView ivPlacePhoto;
    @BindView(R.id.tvPlaceName) TextView tvPlaceName;
    @BindView(R.id.rbPlaceRating) RatingBar rbPlaceRating;
    private Unbinder unbinder;

    // variables
    private PlaceCardFragmentListener listener;

    public interface PlaceCardFragmentListener {
        void onCardSelected(PlaceInfo place);
    }

    public static PlaceCardFragment newInstance(PlaceInfo place, int position) {
        PlaceCardFragment fragment = new PlaceCardFragment();
        Bundle args = new Bundle();
        args.putInt(POSITION_ARG, position);
        args.putString(PLACE_PHOTO_ARG, place.getPhotoRef());
        args.putString(PLACE_NAME_ARG, place.getName());
        args.putDouble(PLACE_RATING_ARG, place.getRating());
        args.putString(PLACE_ID_ARG, place.getPlaceId());
        args.putDouble(PLACE_LAT_ARG, place.getLatlng().latitude);
        args.putDouble(PLACE_LONG_ARG, place.getLatlng().longitude);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View view = inflater.inflate(R.layout.fragment_place, container, false);
        unbinder = ButterKnife.bind(this, view);

        Bundle args = getArguments();
        String name = args.getString(PLACE_NAME_ARG);
        String placeId = args.getString(PLACE_ID_ARG);
        String photoRef = args.getString(PLACE_PHOTO_ARG);
        double rating = args.getDouble(PLACE_RATING_ARG, 0.0);
        tvPlaceName.setText(name);
        ImageUtils.loadImage(ivPlacePhoto, getPlacePhotoUrl(photoRef), R.drawable.ic_fork_knife);
        rbPlaceRating.setRating((float) rating);
        double lat = args.getDouble(PLACE_LAT_ARG, 0.0);
        double lng = args.getDouble(PLACE_LONG_ARG, 0.0);

        view.setOnClickListener(v -> listener.onCardSelected(
                new PlaceInfo(name, rating, placeId, photoRef, new LatLng(lat, lng))));

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof PlacesFragmentListener)) {
            throw new RuntimeException("Activity should implement PlaceCardFragmentListener");
        }
        listener = (PlaceCardFragmentListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
