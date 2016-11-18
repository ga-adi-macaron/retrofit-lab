package net.serkanbal.retrofitlab;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Serkan on 18/11/16.
 */

public interface OpenAirWeatherService {

@GET("weather?appid=1dc7f58282bec421f230ea3985ee589d")
    Call<OpenAirWeatherObject> getCity(@Query("q") String cityName);

}
