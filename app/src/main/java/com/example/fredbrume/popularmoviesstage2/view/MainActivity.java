package com.example.fredbrume.popularmoviesstage2.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.fredbrume.popularmoviesstage2.R;
import com.example.fredbrume.popularmoviesstage2.adapters.FavoriteCursorAdapter;
import com.example.fredbrume.popularmoviesstage2.adapters.PosterAdapter;
import com.example.fredbrume.popularmoviesstage2.model.Poster;
import com.example.fredbrume.popularmoviesstage2.util.FavoriteContract;
import com.example.fredbrume.popularmoviesstage2.util.MovieDBjsonUtils;
import com.example.fredbrume.popularmoviesstage2.util.NetworkUtils;
import com.example.fredbrume.popularmoviesstage2.view.MovieDetailActivity;
import com.example.fredbrume.popularmoviesstage2.view.SettingsActivity;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener, PosterAdapter.PosterAdapterOnClickHandler, FavoriteCursorAdapter.FavoritePosterOnClickHandler {

    private RecyclerView mainList;
    private PosterAdapter posterAdapter;
    private FavoriteCursorAdapter favoriteCursorAdapter;
    public static final int LOADER_ID = 0;
    private static String SORT_VALUE = "popular";
    private final static String SORT_VALUE_FAVORITE = "favorite";
    private LoaderManager loaderManager;
    private Toolbar toolbar;
    private String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mainList = (RecyclerView) findViewById(R.id.main_list);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        mainList.setLayoutManager(gridLayoutManager);
        mainList.setHasFixedSize(true);

        getSharedPreference();

        loaderManager = getSupportLoaderManager();


        if (SORT_VALUE.equals(SORT_VALUE_FAVORITE)) {

            loaderManager.initLoader(LOADER_ID, null, new FetchFavoriteTask());
            intializeFavoriteAdapter();

        } else {

            loaderManager.initLoader(LOADER_ID, null, new FetchMoviesTask());
            initializePosterAdapter();
        }

    }

    @Override
    public void onClickFavoritePoster(Poster poster) {

        Context context = this;
        Class destinationClass = FavoriteDetailActivity.class;

        Intent intentToStartDetailActivity = new Intent(context, destinationClass);

        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, poster);
        startActivity(intentToStartDetailActivity);
    }

    public class FetchFavoriteTask implements LoaderManager.LoaderCallbacks<Cursor> {

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {

            return new AsyncTaskLoader<Cursor>(getBaseContext()) {


                Cursor cursorPoster;

                @Override
                protected void onStartLoading() {
                    super.onStartLoading();

                    if (cursorPoster != null) {
                        deliverResult(cursorPoster);
                    } else {
                        forceLoad();
                    }
                }

                @Override
                public Cursor loadInBackground() {
                    try {
                        return getBaseContext().getContentResolver().query(FavoriteContract.FavoriteEntry.CONTENT_URI,
                                null,
                                null,
                                null,
                                null
                        );

                    } catch (Exception e) {
                        Log.e(TAG, "Failed to asynchronously load data.");
                        e.printStackTrace();
                        return null;
                    }
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

            if (data == null) {

                favoriteCursorAdapter = new FavoriteCursorAdapter((FavoriteCursorAdapter.FavoritePosterOnClickHandler) getBaseContext());
                mainList.setAdapter(favoriteCursorAdapter);
            }

            favoriteCursorAdapter.swapCursor(data);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }

    public class FetchMoviesTask implements LoaderManager.LoaderCallbacks<ArrayList<Poster>> {

        @Override
        public Loader<ArrayList<Poster>> onCreateLoader(final int id, final Bundle args) {
            return new AsyncTaskLoader<ArrayList<Poster>>(getBaseContext()) {

                ArrayList<Poster> postersList;

                @Override
                protected void onStartLoading() {
                    super.onStartLoading();

                    if (postersList != null) {
                        deliverResult(postersList);
                    } else {
                        forceLoad();
                    }
                }

                @Override
                public void deliverResult(ArrayList<Poster> data) {

                    postersList = data;
                    super.deliverResult(data);
                }

                @Override
                public ArrayList<Poster> loadInBackground() {

                    try {

                        String response = NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildMovieUrl(SORT_VALUE));

                        ArrayList<Poster> posters = MovieDBjsonUtils.getPosterArrayListFromJson(response);

                        return posters;

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<Poster>> loader, ArrayList<Poster> data) {

            if (data != null) {
                posterAdapter.setLayoutAdapter(data);
            }
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Poster>> loader) {

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.sort:

                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getSharedPreference() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SORT_VALUE = sharedPreferences.getString(getString(R.string.pref_sort_key), getString(R.string.popular_value));
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        getSharedPreference();
        inValidateData();
    }

    private void inValidateData() {

        if (SORT_VALUE.equals("favorite")) {

            intializeFavoriteAdapter();
            favoriteCursorAdapter.swapCursor(null);
            getSupportLoaderManager().restartLoader(LOADER_ID, null, new FetchFavoriteTask());


        } else {

            initializePosterAdapter();
            posterAdapter.setLayoutAdapter(null);
            getSupportLoaderManager().restartLoader(LOADER_ID, null, new FetchMoviesTask());

        }
    }

    private void initializePosterAdapter() {
        posterAdapter = new PosterAdapter(this);
        mainList.setAdapter(posterAdapter);
    }

    private void intializeFavoriteAdapter() {
        favoriteCursorAdapter = new FavoriteCursorAdapter(this);
        mainList.setAdapter(favoriteCursorAdapter);
    }

    @Override
    public void onClick(Poster mPosterDetails) {

        Context context = this;
        Class destinationClass = MovieDetailActivity.class;

        Intent intentToStartDetailActivity = new Intent(context, destinationClass);

        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, mPosterDetails);
        startActivity(intentToStartDetailActivity);
    }
}
