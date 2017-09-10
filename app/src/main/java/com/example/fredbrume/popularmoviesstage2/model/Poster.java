package com.example.fredbrume.popularmoviesstage2.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fredbrume on 8/26/17.
 */

public class Poster implements Parcelable {

    private String posterPath;
    private String posterRating;
    private String posterId;
    private String posterTitle;
    private String posterBackdropPath;
    private String posterSypnosis;
    private String posterOverview;
    private String posterDuration;
    private String posterYear;
    private byte[] posterInBytes;
    private byte[] backdropInBytes;

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getPosterRating() {
        return posterRating;
    }

    public void setPosterRating(String posterRating) {
        this.posterRating = posterRating;
    }

    public String getPosterId() {
        return posterId;
    }

    public void setPosterId(String posterId) {
        this.posterId = posterId;
    }

    public String getPosterTitle() {
        return posterTitle;
    }

    public void setPosterTitle(String posterTitle) {
        this.posterTitle = posterTitle;
    }

    public String getPosterBackdropPath() {
        return posterBackdropPath;
    }

    public String getPosterYear() {
        return posterYear;
    }

    public void setPosterYear(String posterYear) {
        this.posterYear = posterYear;
    }

    public void setPosterBackdropPath(String posterBackdropPath) {
        this.posterBackdropPath = posterBackdropPath;
    }

    public String getPosterSypnosis() {
        return posterSypnosis;
    }

    public void setPosterSypnosis(String posterSypnosis) {
        this.posterSypnosis = posterSypnosis;
    }

    public String getPosterOverview() {
        return posterOverview;
    }

    public void setPosterOverview(String posterOverview) {
        this.posterOverview = posterOverview;
    }

    public String getPosterDuration() {
        return posterDuration;
    }

    public void setPosterDuration(String posterDuration) {
        this.posterDuration = posterDuration;
    }

    public byte[] getPosterInBytes() {
        return posterInBytes;
    }

    public void setPosterInBytes(byte[] posterInBytes) {
        this.posterInBytes = posterInBytes;
    }

    public byte[] getBackdropInBytes() {
        return backdropInBytes;
    }

    public void setBackdropInBytes(byte[] backdropInBytes) {
        this.backdropInBytes = backdropInBytes;
    }

    public static Creator<Poster> getCREATOR() {
        return CREATOR;
    }

    public Poster() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.posterPath);
        dest.writeString(this.posterRating);
        dest.writeString(this.posterId);
        dest.writeString(this.posterTitle);
        dest.writeString(this.posterBackdropPath);
        dest.writeString(this.posterSypnosis);
        dest.writeString(this.posterOverview);
        dest.writeString(this.posterDuration);
        dest.writeString(this.posterYear);
        dest.writeByteArray(this.posterInBytes);
        dest.writeByteArray(this.backdropInBytes);
    }

    protected Poster(Parcel in) {
        this.posterPath = in.readString();
        this.posterRating = in.readString();
        this.posterId = in.readString();
        this.posterTitle = in.readString();
        this.posterBackdropPath = in.readString();
        this.posterSypnosis = in.readString();
        this.posterOverview = in.readString();
        this.posterDuration = in.readString();
        this.posterYear = in.readString();
        this.posterInBytes = in.createByteArray();
        this.backdropInBytes = in.createByteArray();
    }

    public static final Creator<Poster> CREATOR = new Creator<Poster>() {
        @Override
        public Poster createFromParcel(Parcel source) {
            return new Poster(source);
        }

        @Override
        public Poster[] newArray(int size) {
            return new Poster[size];
        }
    };
}
