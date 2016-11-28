package com.jonathanlieblich.myapplication;


import com.jonathanlieblich.myapplication.Assets.RootWeatherObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static com.jonathanlieblich.myapplication.MainActivity.API_KEY;

/**
 * Created by jonlieblich on 11/18/16.
 */

public interface OpenWeatherMapService {
    @GET("weather?appid="+API_KEY)
    Call<RootWeatherObject> getWeather(@Query("q") String cityname);
}
