package com.heyhuyen.eatsf.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.heyhuyen.eatsf.R;
import com.heyhuyen.eatsf.model.PlaceInfo;
import com.heyhuyen.eatsf.net.GoogleApiHttpClient;
import com.heyhuyen.eatsf.utils.ImageUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Custom recycler view adapter for place items.
 */

public class PlaceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<PlaceInfo> places;

    public PlaceAdapter(List<PlaceInfo> places) {
        this.places = places;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View defaultView = inflater.inflate(R.layout.item_place, parent, false);
        return new PlaceViewHolder(defaultView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PlaceViewHolder vh = (PlaceViewHolder) holder;
        configureViewHolder(vh, position);
    }

    private void configureViewHolder(PlaceViewHolder viewHolder, int position) {
        PlaceInfo place = this.places.get(position);
        viewHolder.tvPlaceName.setText(place.getName());
        viewHolder.rbPlaceRating.setRating((float) place.getRating());

        String photoUrl = GoogleApiHttpClient.getPlacePhotoUrl(place.getPhotoRef());
        ImageUtils.loadImage(viewHolder.ivPlacePhoto, photoUrl, R.drawable.ic_fork_knife);
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public static class PlaceViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivPlacePhoto) ImageView ivPlacePhoto;
        @BindView(R.id.tvPlaceName) TextView tvPlaceName;
        @BindView(R.id.rbPlaceRating) RatingBar rbPlaceRating;

        public PlaceViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

