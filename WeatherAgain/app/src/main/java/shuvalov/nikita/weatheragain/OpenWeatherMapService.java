package shuvalov.nikita.weatheragain;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import shuvalov.nikita.weatheragain.JSONPOJOS.WeatherData;

/**
 * Created by NikitaShuvalov on 11/18/16.
 */

public interface OpenWeatherMapService {


    @GET("weather/?appid=f9a44a5599ad558ca9ac2a02eb37eba5")
    Call<WeatherData>getWeather(@Query("q")String city_name, @Query("units") String units);
}
