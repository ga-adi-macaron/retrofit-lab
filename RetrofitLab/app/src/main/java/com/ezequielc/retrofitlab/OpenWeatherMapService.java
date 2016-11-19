package com.ezequielc.retrofitlab;

import com.ezequielc.retrofitlab.models.RootObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by student on 11/18/16.
 */

public interface OpenWeatherMapService {

    @GET("weather?APPID=1d717d686804fb1b252d5391ef153200&units=imperial")
    Call<RootObject> getInfo(@Query("q") String cityname);
}
