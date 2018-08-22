package com.example.ganeshtikone.retrorc.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class MovieResponse implements Parcelable {

    public final static Parcelable.Creator<MovieResponse> CREATOR = new Creator<MovieResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public MovieResponse createFromParcel(Parcel in) {
            return new MovieResponse(in);
        }

        public MovieResponse[] newArray(int size) {
            return (new MovieResponse[size]);
        }

    };
    private Integer status;
    private List<Datum> data = null;

    protected MovieResponse(Parcel in) {
        this.status = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.data, (Datum.class.getClassLoader()));
    }

    /**
     * No args constructor for use in serialization
     */
    public MovieResponse() {
    }

    /**
     * @param status
     * @param data
     */
    public MovieResponse(Integer status, List<Datum> data) {
        super();
        this.status = status;
        this.data = data;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("status:" + status).append("data:" + data).toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeList(data);
    }

    public int describeContents() {
        return 0;
    }

}