/*
* Copyright (C) 2016 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.example.fredbrume.popularmoviesstage2.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fredbrume.popularmoviesstage2.R;
import com.example.fredbrume.popularmoviesstage2.model.Poster;
import com.example.fredbrume.popularmoviesstage2.util.FavoriteContentProviderHelper;
import com.example.fredbrume.popularmoviesstage2.util.FavoriteContract;


public class FavoriteCursorAdapter extends RecyclerView.Adapter<FavoriteCursorAdapter.FavoriteViewHolder> {

    private Cursor mCursor;
    private Context mContext;
    private FavoritePosterOnClickHandler favoritePosterOnClickHandler;


    public FavoriteCursorAdapter(FavoritePosterOnClickHandler favoritePosterOnClickHandler) {

        this.favoritePosterOnClickHandler = favoritePosterOnClickHandler;
    }

    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);

        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteViewHolder holder, int position) {

        int moviePosterIndex = mCursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_POSTER);
        int movieRatingIndex = mCursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_RATING);

        mCursor.moveToPosition(position);

        final byte[] bytesPoster = mCursor.getBlob(moviePosterIndex);

        Bitmap bitmap = BitmapFactory.decodeByteArray(bytesPoster, 0, bytesPoster.length);

        holder.rating.setText(mCursor.getString(movieRatingIndex));
        holder.sImage.setImageBitmap(bitmap);

    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    public Cursor swapCursor(Cursor c) {

        if (mCursor == c) {
            return null;
        }
        Cursor temp = mCursor;
        this.mCursor = c;

        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

    class FavoriteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public ImageView sImage;
        public TextView rating;


        public FavoriteViewHolder(View itemView) {
            super(itemView);

            sImage = (ImageView) itemView.findViewById(R.id.movie_poster_id);
            rating = (TextView) itemView.findViewById(R.id.rating_value_id);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();

            Poster poster = FavoriteContentProviderHelper.MoviePosterFromDB(mContext, position);

            favoritePosterOnClickHandler.onClickFavoritePoster(poster);

        }
    }

    public interface FavoritePosterOnClickHandler {

        void onClickFavoritePoster(Poster poster);

    }
}