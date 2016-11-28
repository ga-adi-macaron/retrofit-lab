package com.scottlindley.retrofitlab;

import com.scottlindley.retrofitlab.Model.City;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Scott Lindley on 11/18/2016.
 */

public interface OpenWeatherMapService {

    @GET("/data/2.5/weather")
    Call<City> getCity(@Query("q") String cityName, @Query("appid") String apiKey, @Query("units") String units);

}
