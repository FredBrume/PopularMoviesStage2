package com.example.fredbrume.popularmoviesstage2.util;

import com.example.fredbrume.popularmoviesstage2.model.Poster;
import com.example.fredbrume.popularmoviesstage2.model.Review;
import com.example.fredbrume.popularmoviesstage2.model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MovieDBjsonUtils {

    public static ArrayList<Poster> getPosterArrayListFromJson(String posterUrlString) throws JSONException {


        ArrayList<Poster> posterStringObj;

        JSONObject posterJsonObject = new JSONObject(posterUrlString);

        JSONArray movieDetailsArray = posterJsonObject.getJSONArray("results");

        if (movieDetailsArray != null) {

            posterStringObj = new ArrayList<>(movieDetailsArray.length());

            for (int i = 0; i < movieDetailsArray.length(); ++i) {

                JSONObject posterObject = movieDetailsArray.getJSONObject(i);

                Poster poster = new Poster();

                poster.setPosterTitle(String.valueOf(posterObject.get("title")));
                poster.setPosterId(String.valueOf(posterObject.get("id")));
                poster.setPosterYear(String.valueOf(posterObject.get("release_date")));
                poster.setPosterOverview(String.valueOf(posterObject.get("overview")));
                poster.setPosterRating(String.valueOf(posterObject.get("vote_average")));
                poster.setPosterPath(String.valueOf(posterObject.get("poster_path")));
                poster.setPosterBackdropPath(String.valueOf(posterObject.get("backdrop_path")));
                poster.setPosterSypnosis(String.valueOf(posterObject.get("backdrop_path")));

                posterStringObj.add(poster);

            }

            return posterStringObj;
        }

        return null;
    }


    public static ArrayList<Trailer> getMovieTrailerArrayListFromJson(String trailerUrlString) throws JSONException {

        ArrayList<Trailer> trailerStringObj = null;

        JSONObject posterJsonObject = new JSONObject(trailerUrlString);

        JSONArray trailerDetailsArray = posterJsonObject.getJSONArray("results");


        if (trailerDetailsArray != null) {

            trailerStringObj = new ArrayList<>(trailerDetailsArray.length());

            for (int i = 0; i < trailerDetailsArray.length(); ++i) {

                JSONObject posterObject = trailerDetailsArray.getJSONObject(i);

                Trailer trailer = new Trailer();

                trailer.setTrailerId(String.valueOf(posterObject.get("key")));
                trailer.setTrailerType(String.valueOf(posterObject.get("name")));
                trailer.setWebsite(String.valueOf(posterObject.get("site")));
                trailer.setQuality(String.valueOf(posterObject.get("size")) + "p");

                trailerStringObj.add(trailer);

            }

            return trailerStringObj;
        }

        return null;
    }

    public static ArrayList<Review> getMovieReviewArrayListFromJson(String reviewUrlString) throws JSONException {

        ArrayList<Review> reviewStringObj = null;

        JSONObject posterJsonObject = new JSONObject(reviewUrlString);

        JSONArray reviewDetailsArray = posterJsonObject.getJSONArray("results");


        if (reviewDetailsArray != null) {

            reviewStringObj = new ArrayList<>(reviewDetailsArray.length());

            for (int i = 0; i < reviewDetailsArray.length(); ++i) {

                JSONObject posterObject = reviewDetailsArray.getJSONObject(i);

                Review review = new Review();

                review.setReviewer(String.valueOf(posterObject.get("author")));
                review.setReview(String.valueOf(posterObject.get("content")));

                reviewStringObj.add(review);

            }

            return reviewStringObj;
        }

        return null;
    }
}
