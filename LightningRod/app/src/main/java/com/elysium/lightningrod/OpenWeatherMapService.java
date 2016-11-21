package com.elysium.lightningrod;

import com.elysium.lightningrod.Model.City;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by jay on 11/18/16.
 */

public interface OpenWeatherMapService {

        @GET("/data/2.5/weather")
        Call<City> getCity(@Query("q") String cityName, @Query("appid") String apiKey, @Query("units") String units);
    }
