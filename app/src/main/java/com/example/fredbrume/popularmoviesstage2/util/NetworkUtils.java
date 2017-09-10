package com.example.fredbrume.popularmoviesstage2.util;

import android.net.Uri;
import android.util.Log;

import com.example.fredbrume.popularmoviesstage2.BuildConfig;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by fredbrume on 8/26/17.
 */

public class NetworkUtils {

    private static final String MOVIEBASEPATH = "http://api.themoviedb.org/3/movie";
    private static final String POSTERSIZE = "w500";
    private static final String POSTERPATH = "http://image.tmdb.org/t/p";
    private static final String API_KEY = "api_key";
    private static final String YOUTUBE_TRAILER_VIDEO_BASE_URL= "http://www.youtube.com/watch";
    private static final String YOUTUBE_TRAILER_FILE_FORMAT = "hqdefault.jpg";
    private static final String YOUTUBE_TRAILER_IMG_BASE_URL = "https://img.youtube.com/vi";
    private static final String DIRECTORY_VIDEOS = "videos";
    private static final String DIRECTORY_REVIEWS = "reviews";
    private static String TAG=NetworkUtils.class.getSimpleName();


    public static URL buildMovieUrl(String sortQuery) {

        Uri uri = Uri.parse(MOVIEBASEPATH).buildUpon()
                .appendPath(sortQuery)
                .appendQueryParameter(API_KEY, BuildConfig.API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildPosterURL() {

        Uri uri = Uri.parse(POSTERPATH).buildUpon().appendPath(POSTERSIZE).build();

        URL url = null;

        try {
            url = new URL(uri.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws Exception {

        HttpURLConnection httpURLConnection = null;

        try {

            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(5000);

            InputStream inputStream = httpURLConnection.getInputStream();

            Scanner input = new Scanner(inputStream);
            input.useDelimiter("\\A");

            if (input.hasNext()) {
                return input.next();
            } else {
                return null;
            }
        } finally {

            httpURLConnection.disconnect();
        }
    }

    public static String buildYoutubeVideolURL(String trailerId) {

        Uri builtUri = Uri.parse(YOUTUBE_TRAILER_VIDEO_BASE_URL).buildUpon()
                .appendPath(trailerId)
                .build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return String.valueOf(url);
    }

    public static String buildYoutubeThumbnailURL(String trailer_Id) {

        Uri builtUri = Uri.parse(YOUTUBE_TRAILER_IMG_BASE_URL).buildUpon()
                .appendPath(trailer_Id)
                .appendPath(YOUTUBE_TRAILER_FILE_FORMAT)
                .build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return String.valueOf(url);
    }

    public static URL buildTrailerIDURL(String poster_id) {
        Uri builtUri = Uri.parse(MOVIEBASEPATH).buildUpon()
                .appendPath(poster_id)
                .appendPath(DIRECTORY_VIDEOS)
                .appendQueryParameter(API_KEY, BuildConfig.API_KEY)
                .build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static URL buildReviewIDURL(String posterId) {
        Uri builtUri = Uri.parse(MOVIEBASEPATH).buildUpon()
                .appendPath(posterId)
                .appendPath(DIRECTORY_REVIEWS)
                .appendQueryParameter(API_KEY, BuildConfig.API_KEY)
                .build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }
}
