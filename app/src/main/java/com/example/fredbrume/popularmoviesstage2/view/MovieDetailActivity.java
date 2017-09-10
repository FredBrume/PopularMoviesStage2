package com.example.fredbrume.popularmoviesstage2.view;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.fredbrume.popularmoviesstage2.R;
import com.example.fredbrume.popularmoviesstage2.adapters.ReviewAdapter;
import com.example.fredbrume.popularmoviesstage2.adapters.TrailerAdapter;
import com.example.fredbrume.popularmoviesstage2.model.Poster;
import com.example.fredbrume.popularmoviesstage2.model.Review;
import com.example.fredbrume.popularmoviesstage2.model.Trailer;
import com.example.fredbrume.popularmoviesstage2.util.FavoriteContentProviderHelper;
import com.example.fredbrume.popularmoviesstage2.util.FavoriteContract;
import com.example.fredbrume.popularmoviesstage2.util.MovieDBjsonUtils;
import com.example.fredbrume.popularmoviesstage2.util.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.ArrayList;


public class MovieDetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterOnClickHandler {

    private static final String TAG = MovieDetailActivity.class.getSimpleName();

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView bannerPoster;
    private Poster posterDetails;
    private FloatingActionButton fab;
    private ImageView poster;
    private TextView overview;
    private TextView title;
    private TextView year;
    private RatingBar rating;
    private static String movieId;
    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;
    private LoaderManager loaderManager;
    private RecyclerView recyclerViewReview;
    private static NestedScrollView nestedScrollView;
    private static RecyclerView trailerViewList;
    private final int TRAILER_LOADER_ID = 2;
    private final int REVIEW_LOADER_ID=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Drawable navIcon = toolbar.getNavigationIcon();
        navIcon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);


        Intent intent = getIntent();
        poster = (ImageView) findViewById(R.id.review_item_poster);
        bannerPoster = (ImageView) findViewById(R.id.posterView);
        title = (TextView) findViewById(R.id.movie_title);
        overview = (TextView) findViewById(R.id.overview);
        year = (TextView) findViewById(R.id.year);
        rating = (RatingBar) findViewById(R.id.rating);
        trailerViewList = (RecyclerView) findViewById(R.id.rv_trailer);
        recyclerViewReview = (RecyclerView) findViewById(R.id.rv_reviewlist);
        posterDetails = intent.getParcelableExtra(Intent.EXTRA_TEXT);

        LinearLayoutManager trailerLinearLayoutManager = new LinearLayoutManager(this);
        LinearLayoutManager reviewLinearLayoutManager = new LinearLayoutManager(this);
        trailerViewList.setLayoutManager(trailerLinearLayoutManager);
        trailerViewList.setHasFixedSize(true);
        trailerViewList.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));

        recyclerViewReview.setLayoutManager(reviewLinearLayoutManager);
        recyclerViewReview.setHasFixedSize(true);
        recyclerViewReview.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));


        fab = (FloatingActionButton) findViewById(R.id.fab);

        if (intent != null) {

            if (intent.hasExtra(Intent.EXTRA_TEXT)) {

                title.setText(String.valueOf(posterDetails.getPosterTitle()));
                overview.setText(String.valueOf(posterDetails.getPosterOverview()));
                rating.setRating(Float.parseFloat(posterDetails.getPosterRating()) / 2);
                year.setText(String.valueOf(posterDetails.getPosterYear() + " " + "(Released)"));
                movieId = posterDetails.getPosterId();

                Picasso.with(this).load(NetworkUtils.buildPosterURL() + posterDetails.getPosterBackdropPath())
                        .into(bannerPoster);

                Picasso.with(this).load(NetworkUtils.buildPosterURL() + posterDetails.getPosterPath())
                        .into(poster);

                collapsingToolbarLayout.setTitle((posterDetails.getPosterTitle()));
            }

        }

        toolbarAppernce();
        checkMovieFavoriteInDB();

        loaderManager = getSupportLoaderManager();

        loaderManager.initLoader(TRAILER_LOADER_ID, null, new FetchTrailerTask());
        loaderManager.initLoader(REVIEW_LOADER_ID, null, new FetchReviewTask());

        initializeTrailerAdapter();
        initializeReviewAdapter();
    }

    private void checkMovieFavoriteInDB() {
        if (FavoriteContentProviderHelper.checkForMovieFavorite(getBaseContext(), movieId)) {

            fab.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.ic_favorite));
        }
    }

    public void onClickAddFavorite(View view) {


        if (FavoriteContentProviderHelper.checkForMovieFavorite(getBaseContext(), movieId)) {
            getContentResolver().delete(FavoriteContract.FavoriteEntry.CONTENT_URI, "id=?", new String[]{movieId.toString()});

            fab.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.ic_favorite_border));

            Snackbar.make(view, getResources().getString(R.string.removed_from_favorite), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();


        } else {

            Bitmap bitmapPoster = ((BitmapDrawable) poster.getDrawable()).getBitmap();
            ByteArrayOutputStream baosPoster = new ByteArrayOutputStream();
            bitmapPoster.compress(Bitmap.CompressFormat.JPEG, 100, baosPoster);
            byte[] posterInByte = baosPoster.toByteArray();

            Bitmap bitmapBackdrop = ((BitmapDrawable) bannerPoster.getDrawable()).getBitmap();
            ByteArrayOutputStream baosBackdrop = new ByteArrayOutputStream();
            bitmapBackdrop.compress(Bitmap.CompressFormat.JPEG, 100, baosBackdrop);
            byte[] bitmapBackdropInByte = baosBackdrop.toByteArray();

            ContentValues contentValues = new ContentValues();

            contentValues.put(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_BACKDROP, bitmapBackdropInByte);
            contentValues.put(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_POSTER, posterInByte);
            contentValues.put(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_TITLE, title.getText().toString());
            contentValues.put(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_SYPNOSIS, overview.getText().toString());
            contentValues.put(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_RATING, String.valueOf(rating.getRating()));
            contentValues.put(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_DATE, year.getText().toString());
            contentValues.put(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_ID, movieId);


            Uri uri = getContentResolver().insert(FavoriteContract.FavoriteEntry.CONTENT_URI, contentValues);

            if (uri != null) {
                fab.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.ic_favorite));
                Snackbar.make(view, getResources().getString(R.string.added_to_favorite), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }

    }

    private void toolbarAppernce() {
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.colorPrimaryDark));
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.transparent));

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.share:

                shareIntent(posterDetails.getPosterTitle());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.share, menu);
        return true;
    }

    public Intent shareIntent(String data) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.movie_extra_subject));
        sharingIntent.putExtra(Intent.EXTRA_TEXT, data);
        return sharingIntent;
    }

    @Override
    public void onTrailerClick(Trailer mTrailerDetails) {

        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse((NetworkUtils.buildYoutubeVideolURL(mTrailerDetails.getTrailerId()))));

        startActivity(intent);
    }

    public class FetchReviewTask implements LoaderManager.LoaderCallbacks<ArrayList<Review>> {

        @Override
        public Loader<ArrayList<Review>> onCreateLoader(int id, Bundle args) {

            return new AsyncTaskLoader<ArrayList<Review>>(getBaseContext()) {

                ArrayList<Review> reviewList;

                @Override
                protected void onStartLoading() {
                    super.onStartLoading();

                    if (reviewList != null) {
                        deliverResult(reviewList);
                    } else {
                        forceLoad();
                    }
                }

                @Override
                public void deliverResult(ArrayList<Review> data) {
                    super.deliverResult(data);

                    reviewList = data;
                }

                @Override
                public ArrayList<Review> loadInBackground() {

                    URL url = NetworkUtils.buildReviewIDURL(movieId);

                    try {
                        String reviewJsonResponse = NetworkUtils.getResponseFromHttpUrl(url);
                        ArrayList<Review> reviews = MovieDBjsonUtils.getMovieReviewArrayListFromJson(reviewJsonResponse);

                        return reviews;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<Review>> loader, ArrayList<Review> data) {

            if (data != null) {
                reviewAdapter.setReviewData(data);
            }
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Review>> loader) {

        }
    }

    public class FetchTrailerTask implements LoaderManager.LoaderCallbacks<ArrayList<Trailer>> {

        @Override
        public Loader<ArrayList<Trailer>> onCreateLoader(int id, Bundle args) {
            return new AsyncTaskLoader<ArrayList<Trailer>>(getBaseContext()) {

                ArrayList<Trailer> trailerList;

                @Override
                protected void onStartLoading() {
                    super.onStartLoading();

                    if (trailerList != null) {

                        deliverResult(trailerList);
                    } else {
                        forceLoad();
                    }
                }

                @Override
                public void deliverResult(ArrayList<Trailer> data) {
                    super.deliverResult(data);

                    trailerList = data;
                }

                @Override
                public ArrayList<Trailer> loadInBackground() {

                    try {
                        String jsonTrailerResponse = NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildTrailerIDURL(movieId));

                        ArrayList<Trailer> trailerList = MovieDBjsonUtils.getMovieTrailerArrayListFromJson(jsonTrailerResponse);

                        return trailerList;

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<Trailer>> loader, ArrayList<Trailer> data) {

            if (data != null) {
                trailerAdapter.setTrailerData(data);
            }
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Trailer>> loader) {

        }
    }

    private void initializeTrailerAdapter() {
        trailerAdapter = new TrailerAdapter(this);
        trailerViewList.setAdapter(trailerAdapter);
    }

    private void initializeReviewAdapter() {
        reviewAdapter = new ReviewAdapter(this);
        recyclerViewReview.setAdapter(reviewAdapter);
    }

}
