package com.example.ganeshtikone.retrorc.database;

import android.os.Environment;

import com.example.ganeshtikone.retrorc.model.Datum;
import com.example.ganeshtikone.retrorc.model.MovieResponse;

import java.io.File;
import java.io.IOException;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Ganesh Tikone on 12/7/18.
 * Company: Silicus Technologies Pvt. Ltd.
 * Email: ganesh.tikone@silicus.com
 * Class: RealmController Database Controller
 * Class
 */
public class RealmController {

    /**
     * Signletone Instance of RealmController class
     */
    private static RealmController instance;

    /**
     * Realm database object
     */
    private final Realm realm;

    /**
     * Listener for movie change listener
     */
    private OnMovieDataChangeListener onMovieDataChangeListener;


    private RealmController() {
        this.realm = Realm.getDefaultInstance();
    }


    public static RealmController getInstance() {
        if (null == instance) {
            instance = new RealmController();
        }
        return instance;
    }

    /**
     *
     */
    public void refresh() {
        realm.refresh();
    }

    public void clearAll() {
        realm.executeTransaction(connection -> realm.delete(Datum.class));
    }

    /**
     * Get all movie list from realm database
     */
    public void getAllMovies() {
        realm.where(Datum.class).findAllAsync().addChangeListener(data -> {
            if (null != onMovieDataChangeListener) {
                onMovieDataChangeListener.onMovieDataChange(data);
            }
        });
    }

    /**
     * Insert movie
     *
     * @param movieResponse movie response object
     */
    public void insertMovies(MovieResponse movieResponse) {
        realm.executeTransaction(realmArgs -> {
//            for (Datum datum : movieResponse.getData()) {
//                Log.d("###", datum.getMovieName());
//                realmArgs.insertOrUpdate(datum);
//            }
            realmArgs.copyToRealmOrUpdate(movieResponse.getData());
        });
    }

    /**
     * Close Connection
     */
    public void close() {
        if (!realm.isClosed()) {
            realm.close();
        }
    }

    /**
     * Sort by result by ascending
     *
     * @param sort sort type object
     * @return RealmResults<Datum> object
     */
    public void sortBy(Sort sort) {
        realm.where(Datum.class).sort("movieId", sort).findAllAsync().addChangeListener(data -> {
            if (null != onMovieDataChangeListener) {
                onMovieDataChangeListener.onMovieDataChange(data);
            }
        });
    }

    /**
     * Search by name
     *
     * @param searchQuery searchQuery
     * @return RealmResults<Datum> object
     */
    public void searchBy(String searchQuery) {
        realm.where(Datum.class).contains("movieName", searchQuery, Case.INSENSITIVE).findAllAsync().addChangeListener(data -> {
            if (null != onMovieDataChangeListener) {
                onMovieDataChangeListener.onMovieDataChange(data);
            }
        });
    }

    /**
     * Setter method for onMovieDataChangeListener
     *
     * @param onMovieDataChangeListener OnMovieDataChangeListener object
     */
    public void setOnMovieDataChangeListener(OnMovieDataChangeListener onMovieDataChangeListener) {
        this.onMovieDataChangeListener = onMovieDataChangeListener;
    }


    public void exportDatabase() {

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm db) {

                final File file = new File(Environment.getExternalStorageDirectory().getPath().concat("/sample.realm"));

                if (file.exists()) {
                    file.delete();
                }

                db.writeCopyTo(file);
            }
        });
    }


    /**
     * Interface to movie data change listener
     */
    public interface OnMovieDataChangeListener {

        /**
         * on movie data change result
         *
         * @param data RealmResults<Datum> data after querying database
         */
        void onMovieDataChange(RealmResults<Datum> data);
    }

}
