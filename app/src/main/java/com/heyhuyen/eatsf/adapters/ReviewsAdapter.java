package com.heyhuyen.eatsf.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.heyhuyen.eatsf.R;
import com.heyhuyen.eatsf.model.Review;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Custom recycler view adapter for review items.
 */
public class ReviewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Review> reviews;
    Context mContext;

    public ReviewsAdapter(Context context, List<Review> reviews) {
        mContext = context;
        this.reviews = reviews;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ReviewViewHolder(inflater.inflate(R.layout.item_review,
                parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Review review = reviews.get(position);
        configureReviewViewHolder((ReviewViewHolder) holder, review);
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    private void configureReviewViewHolder(ReviewViewHolder holder, Review review) {
        holder.tvAuthor.setText(review.getAuthor());
        holder.rbRating.setRating((float) review.getRating());
        if (!TextUtils.isEmpty(review.getRelativeTimestamp())) {
            holder.tvTime.setText(review.getRelativeTimestamp());
        } else if (review.getTimestamp() != Review.INVALID_TIMESTAMP) {
            holder.tvTime.setText(DateUtils.formatDateTime(mContext, review.getTimestamp(),
                    DateUtils.FORMAT_ABBREV_MONTH));
        }

        String text = review.getText();
        if (!TextUtils.isEmpty(text)) {
            holder.tvText.setVisibility(View.VISIBLE);
            holder.tvText.setText(text);
        } else {
            holder.tvText.setVisibility(View.GONE);
        }
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvAuthor) TextView tvAuthor;
        @BindView(R.id.rbRating) RatingBar rbRating;
        @BindView(R.id.tvText) TextView tvText;
        @BindView(R.id.tvTime) TextView tvTime;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
