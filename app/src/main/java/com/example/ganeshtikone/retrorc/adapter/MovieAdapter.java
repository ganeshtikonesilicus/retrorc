package com.example.ganeshtikone.retrorc.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ganeshtikone.retrorc.R;
import com.example.ganeshtikone.retrorc.extra.GTImageView;
import com.example.ganeshtikone.retrorc.model.Datum;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;

/**
 *
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    /**
     * Context object
     */
    private Context context;

    /**
     * List of Datum - Movie Object
     */
    private List<Datum> datumList;

    public MovieAdapter(Context context) {
        this.context = context;
        this.datumList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.row_movie, parent, false);
        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {

        Datum datum = datumList.get(position);
        holder.textViewMovieTitle.setText(datum.getMovieName());
        holder.imageViewPoster.loadImageFromUrl(datum.getMoviePoster());
    }

    @Override
    public int getItemCount() {
        return datumList.size();
    }

    /**
     * Update data to recycler view
     *
     * @param data update data from server
     */
    public void updateData(List<Datum> data) {
        this.datumList.clear();
        this.datumList.addAll(data);
        notifyDataSetChanged();
    }


    /**
     *
     * @param data RealmResult object
     */
    public void updateData(RealmResults<Datum> data) {

        this.datumList.clear();
        this.datumList.addAll(data.subList(0, data.size()));
        notifyDataSetChanged();
    }

    /**
     * ViewHolder class for RecyclerView
     */
    class MovieViewHolder extends RecyclerView.ViewHolder {

        GTImageView imageViewPoster;
        AppCompatTextView textViewMovieTitle;

        MovieViewHolder(View itemView) {
            super(itemView);

            imageViewPoster = itemView.findViewById(R.id.imageViewPoster);
            textViewMovieTitle = itemView.findViewById(R.id.textViewMovieTitle);
        }
    }
}
