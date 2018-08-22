package com.example.ganeshtikone.retrorc.feature.get;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.ganeshtikone.retrorc.MainActivity;
import com.example.ganeshtikone.retrorc.R;
import com.example.ganeshtikone.retrorc.adapter.MovieAdapter;
import com.example.ganeshtikone.retrorc.database.RealmController;
import com.example.ganeshtikone.retrorc.model.Datum;
import com.example.ganeshtikone.retrorc.model.MovieResponse;
import com.example.ganeshtikone.retrorc.network.RequestService;

import io.reactivex.disposables.CompositeDisposable;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * A simple {@link Fragment} subclass.
 */
public class GetRequestFragment extends Fragment implements RealmController.OnMovieDataChangeListener {


    private static final String TAG = MainActivity.class.getSimpleName();

    /**
     * RxAndroid init object
     */
    private CompositeDisposable compositeDisposable;

    /**
     * Adapter object
     */
    private MovieAdapter movieAdapter;

    /**
     * Context context
     */
    private Context context;

    /**
     * RealmController
     */
    private RealmController realmController;

    /**
     * RealResult object of Dataum
     */
    private RealmResults<Datum> realmResults;


    public GetRequestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        compositeDisposable = new CompositeDisposable();
        context = getActivity();
        realmController = RealmController.getInstance();
        realmController.setOnMovieDataChangeListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_get_request, container, false);
        initUI(rootView);
        return rootView;
    }

    /**
     * Initialise UI with rootView
     *
     * @param rootView view object
     */
    private void initUI(View rootView) {

        RecyclerView recyclerViewMovie = rootView.findViewById(R.id.recyclerViewMovie);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        movieAdapter = new MovieAdapter(context);

        recyclerViewMovie.setHasFixedSize(true);
        recyclerViewMovie.setLayoutManager(linearLayoutManager);
        recyclerViewMovie.setAdapter(movieAdapter);

        AppCompatEditText editTextSearchQuery = rootView.findViewById(R.id.editTextSearchBox);
        editTextSearchQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Code
                if (count >= 3) {
                    // start searching
                    realmController.searchBy(s.toString());
                } else if (count == 0) {
                    // load original data
                    realmController.getAllMovies();
                } else {
                    // do nothing
                    Log.d(TAG, "Do nothing");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        makeGetAPICallWithService();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_get_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_sort_asc:
                realmController.sortBy(Sort.ASCENDING);
                break;

            case R.id.action_sort_desc:
                realmController.sortBy(Sort.DESCENDING);
                break;

            case R.id.action_write_file:
                realmController.exportDatabase();
                break;
            default:
                // do nothing
        }

        return super.onOptionsItemSelected(item);
    }

    /*
     * Make API Call with Service class Object
     */
    @SuppressLint("CheckResult")
    private void makeGetAPICallWithService() {
        RequestService requestService = new RequestService();
        requestService.getRecentMovies()
                .subscribe(this::handleResponse, this::handleError);
    }

    /**
     * Handler error from library/ server
     *
     * @param error throwable
     */
    private void handleError(Throwable error) {
        Log.e(TAG, error.getMessage());
        realmController.getAllMovies();
    }

    /**
     * Handler server response
     *
     * @param movieResponse movie response object
     */
    private void handleResponse(MovieResponse movieResponse) {
        Log.e(TAG, movieResponse.toString());
        movieAdapter.updateData(movieResponse.getData());
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        realmController.close();
        super.onDestroy();
    }

    @Override
    public void onMovieDataChange(RealmResults<Datum> data) {
        movieAdapter.updateData(data);
    }


}
