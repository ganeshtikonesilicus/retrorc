package com.example.ganeshtikone.retrorc.network;

import com.example.ganeshtikone.retrorc.database.RealmController;
import com.example.ganeshtikone.retrorc.model.MovieResponse;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RequestService {

    /**
     * TAG
     */
    private static final String TAG = RequestService.class.getSimpleName();

    /**
     * BASE URL
     */
    public static final String BASEURL = "http://sysandnet.com/filmystanapi/public/api/v1/";


    /**
     * RequestInterface object
     */
    private RequestInterface requestInterface;


    public RequestService() {
        initRequestInterface();
    }

    /**
     *
     */
    private void initRequestInterface() {

        requestInterface = new Retrofit.Builder()
                .baseUrl(BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(RequestInterface.class);
    }

    /**
     * GET request call
     *
     * @return Observable<MoviResponse> object
     */
    public Observable<MovieResponse> getRecentMovies() {

        return requestInterface.getRecentMovies()
                .retry(5)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .filter(movieResponse -> movieResponse.getStatus() == 200)
                .map(this::writeToDatabase);

    }

    /**
     * Insert movie data into database
     * @param movieResponse movieResponse object from api
     * @param <R> Unknow
     * @return movieResponse
     */
    private <R> MovieResponse writeToDatabase(MovieResponse movieResponse) {

        RealmController realmController = RealmController.getInstance();
        realmController.insertMovies(movieResponse);
        return movieResponse;
    }

    /**
     * Create user
     *
     * @param postRequestModel POST request parameters
     * @return Observable<JSONObject>
     */
    public Observable<String> createUser(String postRequestModel) {

        return requestInterface.createUserWith(postRequestModel)
                .retry(2)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }


    public Observable<String> uploadImage(RequestBody file) {

        return requestInterface.uploadImage(file)
                .retry(2)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map(ResponseBody::string);
    }
}
