package com.joelimyx.myapplication;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Joe on 11/18/16.
 */

public interface OpenWeatherService {
    @GET("data/2.5/weather")
    Call<City> getCityWeather(@Query("q") String city, @Query("appid") String id, @Query("units") String unit);
}
