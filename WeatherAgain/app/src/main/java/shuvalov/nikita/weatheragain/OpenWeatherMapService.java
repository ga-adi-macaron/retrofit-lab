package shuvalov.nikita.weatheragain;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import shuvalov.nikita.weatheragain.JSONPOJOS.WeatherData;

/**
 * Created by NikitaShuvalov on 11/18/16.
 */

public interface OpenWeatherMapService {


    @GET("weather/?q={city-name}")
    Call<WeatherData>getWeather(@Path("city-name")String cityName);
}
