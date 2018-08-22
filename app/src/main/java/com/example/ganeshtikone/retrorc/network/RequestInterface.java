package com.example.ganeshtikone.retrorc.network;

import com.example.ganeshtikone.retrorc.model.MovieResponse;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RequestInterface {

    @GET("recent")
    Observable<MovieResponse> getRecentMovies();

    @Headers("Content-Type: application/json")
    @POST("create")
    Observable<String> createUserWith(@Body String postRequestModel);

    @Multipart
    @POST("upload")
    Observable<ResponseBody> uploadImage(@Part("file\"; filename=\"file.jpg\" ") RequestBody file);

}
