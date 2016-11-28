package drsdrs.retrofitlab;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import drsdrs.retrofitlab.models.WeatherResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by ds on 11/18/16.
 */

public interface WeatherService {

    @GET("weather")
    Call<WeatherResponse> getWeather(@Query("q") String cityName, @Query("APPID") String apiKey);


}

