package com.example.ganeshtikone.retrorc;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.ganeshtikone.retrorc.adapter.MovieAdapter;
import com.example.ganeshtikone.retrorc.model.MovieResponse;
import com.example.ganeshtikone.retrorc.network.RequestInterface;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    /**
     * RxAndroid intilizer object
     */
    private CompositeDisposable compositeDisposable;

    /**
     * Adapter object
     */
    private MovieAdapter movieAdapter;

    private CoordinatorLayout coordinatorLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        compositeDisposable = new CompositeDisposable();
        initRecyclerView();
    }

    /**
     *
     */
    private void initRecyclerView() {


        coordinatorLayout = findViewById(R.id.coordinateLayout);

        RecyclerView recyclerViewMovie = findViewById(R.id.recyclerViewMovie);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        movieAdapter = new MovieAdapter(this);

        recyclerViewMovie.setHasFixedSize(true);
        recyclerViewMovie.setLayoutManager(linearLayoutManager);
        recyclerViewMovie.setAdapter(movieAdapter);
    }


    @Override
    protected void onStart() {
        super.onStart();
        //makeAPICall();
        //makeGetAPICallWithService();
        requestPermissionWriteExternalStorage();

    }

    /**
     *
     */
    private void requestPermissionWriteExternalStorage() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        2001
                );
            }else {
                Snackbar.make(coordinatorLayout, "Write to storage Permission Granted", Snackbar.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (2001 == requestCode
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Snackbar.make(coordinatorLayout, "Write to storage  Permission Granted", Snackbar.LENGTH_SHORT).show();
        }else {
            Snackbar.make(coordinatorLayout, "Write to storage Permission Denied", Snackbar.LENGTH_SHORT).show();

        }
    }

    /**
     * Make API Call with Service class Object
     */
    private void makeGetAPICallWithService() {
        //RequestService requestService = new RequestService();
        //requestService.getRecentMovies();
    }


    private void makeAPICall() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(this::intercept);


        RequestInterface requestInterface = new Retrofit.Builder()
                .baseUrl("http://sysandnet.com/filmystanapi/public/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(builder.build())
                .build()
                .create(RequestInterface.class);

        requestInterface.getRecentMovies()
                .retry(5)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .filter(movieResponse -> movieResponse.getStatus() == 200)
                .subscribe(this::handleResponse, this::handleError);
    }

    /**
     * Add header to request
     *
     * @param chain chain object
     * @return response object
     */
    private Response intercept(Interceptor.Chain chain) {
        try {
            Request request = chain.request().newBuilder().addHeader("Content-Type", "application/json").build();
            return chain.proceed(request);
        } catch (IOException ex) {
            Log.e(TAG, ex.getLocalizedMessage());
        }

        return null;
    }


    /**
     * Handler error from library/ server
     *
     * @param error throwable
     */
    private void handleError(Throwable error) {
        Log.e(TAG, error.getMessage());

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
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
