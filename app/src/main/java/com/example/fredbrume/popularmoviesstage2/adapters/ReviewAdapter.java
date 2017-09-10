package com.example.fredbrume.popularmoviesstage2.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fredbrume.popularmoviesstage2.R;
import com.example.fredbrume.popularmoviesstage2.model.Review;

import java.util.ArrayList;


public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private ArrayList mReviewData;
    private Context context;

    public ReviewAdapter(Context context) {

        this.context=context;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(final ViewGroup viewGroup, int viewType) {

        context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.review_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);

        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ReviewViewHolder viewHolder, int position) {

        Review review = (Review) mReviewData.get(position);

        viewHolder.reviewer.setText(review.getReviewer());
        viewHolder.review.setText(review.getReview());
    }

    @Override
    public int getItemCount() {
        return mReviewData == null ? 0 : mReviewData.size();

    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener

    {
        public TextView reviewer;
        public TextView review;


        public ReviewViewHolder(View itemView) {
            super(itemView);

            reviewer = (TextView) itemView.findViewById(R.id.reviewer);
            review = (TextView) itemView.findViewById(R.id.review);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }

    }

    public void setReviewData(ArrayList<Review> mReviewDataList) {

        this.mReviewData = mReviewDataList;
        notifyDataSetChanged();
    }
}


