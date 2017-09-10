package com.example.fredbrume.popularmoviesstage2.util;

import android.content.Context;
import android.database.Cursor;
import com.example.fredbrume.popularmoviesstage2.model.Poster;

import java.util.ArrayList;

/**
 * Created by fredbrume on 8/20/17.
 */

public class FavoriteContentProviderHelper {

    public static ArrayList<Poster> MoviePosterListFromDB(Context context) {

        Poster moviePoster = new Poster();
        ArrayList<Poster> arrayListMoviePoster = new ArrayList<>();

        Cursor cursor = context.getContentResolver().query(FavoriteContract.FavoriteEntry.CONTENT_URI,
                null, null, null, null);

        if (cursor.moveToFirst()) {
            do {

                moviePoster.setPosterTitle(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_TITLE)));
                moviePoster.setPosterId(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_ID)));
                moviePoster.setPosterSypnosis(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_SYPNOSIS)));
                moviePoster.setBackdropInBytes(cursor.getBlob(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_BACKDROP)));
                moviePoster.setPosterInBytes(cursor.getBlob(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_POSTER)));
                moviePoster.setPosterYear(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_DATE)));
                moviePoster.setPosterRating(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_RATING)));

                arrayListMoviePoster.add(moviePoster);
            }
            while (cursor.moveToNext());
        }

        cursor.close();

        return arrayListMoviePoster;
    }

    public static boolean checkForMovieFavorite(Context context, String movie_id) {

        ArrayList<Poster> list = MoviePosterListFromDB(context);

        for (int i = 0; i < list.size(); ++i) {

            if (list.get(i).getPosterId().equals(movie_id)) {

                return true;
            }
        }

        return false;
    }

    public static Poster MoviePosterFromDB(Context context, int position) {

        Poster moviePoster = new Poster();

        Cursor cursor = context.getContentResolver().query(FavoriteContract.FavoriteEntry.CONTENT_URI,
                null, null, null, null);

        if (cursor.moveToPosition(position)) {
            moviePoster.setPosterTitle(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_TITLE)));
            moviePoster.setPosterId(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_ID)));
            moviePoster.setPosterSypnosis(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_SYPNOSIS)));
            moviePoster.setBackdropInBytes(cursor.getBlob(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_BACKDROP)));
            moviePoster.setPosterInBytes(cursor.getBlob(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_POSTER)));
            moviePoster.setPosterYear(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_DATE)));
            moviePoster.setPosterRating(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_RATING)));
        }


        cursor.close();

        return moviePoster;
    }
}
