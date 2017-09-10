package com.example.fredbrume.popularmoviesstage2.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fredbrume on 8/8/17.
 */

public class Trailer implements Parcelable {

    private String trailerId;
    private String website;
    private String quality;
    private String trailerType;

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getTrailerType() {
        return trailerType;
    }

    public void setTrailerType(String trailerType) {
        this.trailerType = trailerType;
    }

    public static Creator<Trailer> getCREATOR() {
        return CREATOR;
    }

    public String getTrailerId() {
        return trailerId;
    }

    public void setTrailerId(String trailerId) {
        this.trailerId = trailerId;
    }


    public Trailer() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.trailerId);
        dest.writeString(this.website);
        dest.writeString(this.quality);
        dest.writeString(this.trailerType);
    }

    protected Trailer(Parcel in) {
        this.trailerId = in.readString();
        this.website = in.readString();
        this.quality = in.readString();
        this.trailerType = in.readString();
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel source) {
            return new Trailer(source);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
}
