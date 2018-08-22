package com.example.ganeshtikone.retrorc.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;


public class Datum extends RealmObject implements Parcelable
{

    @Expose
    @SerializedName("movie_id")
    @PrimaryKey
    @Required
    private String movieId;

    @Expose
    @SerializedName("movie_name")
    @Required
    private String movieName;

    @Expose
    @SerializedName("movie_poster")
    @Required
    private String moviePoster;

    @Expose
    @SerializedName("movie_dialog_count")
    private String movieDialogCount;

    public final static Parcelable.Creator<Datum> CREATOR = new Creator<Datum>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Datum createFromParcel(Parcel in) {
            return new Datum(in);
        }

        public Datum[] newArray(int size) {
            return (new Datum[size]);
        }

    }
            ;

    protected Datum(Parcel in) {
        this.movieId = ((String) in.readValue((String.class.getClassLoader())));
        this.movieName = ((String) in.readValue((String.class.getClassLoader())));
        this.moviePoster = ((String) in.readValue((String.class.getClassLoader())));
        this.movieDialogCount = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public Datum() {
    }

    /**
     *
     * @param movieName
     * @param movieId
     * @param moviePoster
     * @param movieDialogCount
     */
    public Datum(String movieId, String movieName, String moviePoster, String movieDialogCount) {
        super();
        this.movieId = movieId;
        this.movieName = movieName;
        this.moviePoster = moviePoster;
        this.movieDialogCount = movieDialogCount;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public void setMoviePoster(String moviePoster) {
        this.moviePoster = moviePoster;
    }

    public String getMovieDialogCount() {
        return movieDialogCount;
    }

    public void setMovieDialogCount(String movieDialogCount) {
        this.movieDialogCount = movieDialogCount;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("movieId:"+ movieId).append("movieName:"+ movieName).append("moviePoster:"+ moviePoster).append("movieDialogCount:"+ movieDialogCount).toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(movieId);
        dest.writeValue(movieName);
        dest.writeValue(moviePoster);
        dest.writeValue(movieDialogCount);
    }

    public int describeContents() {
        return 0;
    }

}