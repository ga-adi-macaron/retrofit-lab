package com.korbkenny.kennyretrofitlabweather;

import com.korbkenny.kennyretrofitlabweather.models.GoodWeather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by KorbBookProReturns on 11/18/16.
 */

public interface OpenWeatherService {
    @GET("weather?appid=9aef2a156ab77f87deb121a0bf6de799")
    Call<GoodWeather> getWeather(@Query("q")String cityName);
}
