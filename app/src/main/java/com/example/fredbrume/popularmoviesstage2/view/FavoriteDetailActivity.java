package com.example.fredbrume.popularmoviesstage2.view;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.fredbrume.popularmoviesstage2.R;
import com.example.fredbrume.popularmoviesstage2.model.Poster;
import com.example.fredbrume.popularmoviesstage2.util.FavoriteContentProviderHelper;
import com.example.fredbrume.popularmoviesstage2.util.FavoriteContract;

import java.io.ByteArrayOutputStream;

public class FavoriteDetailActivity extends AppCompatActivity {

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView bannerPoster;
    private Poster posterDetails;
    private FloatingActionButton fab;
    private ImageView poster;
    private static String movieId;
    private TextView overview;
    private TextView title;
    private TextView year;
    private RatingBar rating;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_detail);


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
        posterDetails = intent.getParcelableExtra(Intent.EXTRA_TEXT);


        fab = (FloatingActionButton) findViewById(R.id.fab);

        if (intent != null) {

            if (intent.hasExtra(Intent.EXTRA_TEXT)) {

                title.setText(String.valueOf(posterDetails.getPosterTitle()));
                overview.setText(String.valueOf(posterDetails.getPosterSypnosis()));
                rating.setRating(Float.parseFloat(posterDetails.getPosterRating()) / 2);
                year.setText(String.valueOf(posterDetails.getPosterYear() + " " + "(Released)"));

                final byte[] bytesPoster = posterDetails.getPosterInBytes();
                final byte[] bytesBackDrop = posterDetails.getBackdropInBytes();
                bitmap = BitmapFactory.decodeByteArray(bytesPoster, 0, bytesPoster.length);
                Bitmap bitmapBackdrop = BitmapFactory.decodeByteArray(bytesBackDrop, 0, bytesBackDrop.length);

                poster.setImageBitmap(bitmap);
                bannerPoster.setImageBitmap(bitmapBackdrop);

                movieId = posterDetails.getPosterId();

            }
        }

        collapsingToolbarLayout.setTitle((posterDetails.getPosterTitle()));
        checkMovieFavoriteInDB();
        toolbarAppernce();
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

    private void toolbarAppernce() {
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.colorPrimaryDark));
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.transparent));
    }


    public Intent shareIntent(String data) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.movie_extra_subject));
        sharingIntent.putExtra(Intent.EXTRA_TEXT, data);
        return sharingIntent;
    }
}
