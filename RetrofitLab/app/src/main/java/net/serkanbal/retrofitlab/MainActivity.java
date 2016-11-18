package net.serkanbal.retrofitlab;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    TextView mCity, mWeather, mPressure, mHumidity, mTemperature;
    EditText mEnterCity;
    Button mGetWeatherInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCity = (TextView) findViewById(R.id.textview_city);
        mWeather = (TextView) findViewById(R.id.textview_weather);
        mPressure = (TextView) findViewById(R.id.textview_pressure);
        mHumidity = (TextView) findViewById(R.id.textview_humidity);
        mTemperature = (TextView) findViewById(R.id.textview_temperature);

        mEnterCity = (EditText) findViewById(R.id.edittext_city);

        mGetWeatherInfo = (Button) findViewById(R.id.button_search);

        mGetWeatherInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCityInfo(mEnterCity.getText().toString());
            }
        });


    }

    public void getCityInfo(String cityName) {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            String baseUrl = "http://api.openweathermap.org/data/2.5/";

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            OpenAirWeatherService openAirWeatherService =
                    retrofit.create(OpenAirWeatherService.class);

            Call<OpenAirWeatherObject> call = openAirWeatherService.getCity(cityName);

            call.enqueue(new Callback<OpenAirWeatherObject>() {
                @Override
                public void onResponse(Call<OpenAirWeatherObject> call, Response<OpenAirWeatherObject> response) {
                    try {
                        String humidity = response.body().getMain().getHumidity().toString();
                        String temperature = response.body().getMain().getTemp().toString();
                        String pressure = response.body().getMain().getPressure().toString();
                        String weather = response.body().getWeather().get(0).getDescription();
                        String loc = response.body().getName();

                        mCity.setText("City: " + loc);
                        mHumidity.setText("Humidity: " + humidity + " %");
                        mTemperature.setText("Temperature: " + temperature + " degrees Celcius");
                        mPressure.setText("Pressure: " + pressure + " hPa");
                        mWeather.setText("Weather: " + weather);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<OpenAirWeatherObject> call, Throwable t) {

                }
            });
        }

    }
}
