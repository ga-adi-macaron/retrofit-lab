package com.jonathanlieblich.myapplication;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jonathanlieblich.myapplication.Assets.RootWeatherObject;
import com.jonathanlieblich.myapplication.Assets.Weather;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    public static final String API_KEY = "42b3f7c4847d3fa7e4469dafe74ec553";
    public static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";

    TextView mName, mWeather, mHumidity, mPressure, mTemperature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText nameEdit = (EditText)findViewById(R.id.city);

        Button weatherButton = (Button)findViewById(R.id.weather_btn);

        mName = (TextView)findViewById(R.id.name);
        mWeather = (TextView)findViewById(R.id.weather);
        mHumidity = (TextView)findViewById(R.id.humidity);
        mPressure = (TextView)findViewById(R.id.pressure);
        mTemperature = (TextView)findViewById(R.id.temperature);

        weatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWeatherByCity(nameEdit.getText().toString());
            }
        });
    }

    private void getWeatherByCity(final String city) {
        Log.d(MainActivity.class.getName(), "Getting weather");

        ConnectivityManager mgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = mgr.getActiveNetworkInfo();
        if(info != null && info.isConnected()) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            OpenWeatherMapService service = retrofit.create(OpenWeatherMapService.class);

            Call<RootWeatherObject> call = service.getWeather(city);

            call.enqueue(new Callback<RootWeatherObject>() {
                @Override
                public void onResponse(Call<RootWeatherObject> call, Response<RootWeatherObject> response) {
                    try {
                        String name = "City: "+city;
                        String humidity = response.body().getMain().getHumidity().toString();
                        String temperature = response.body().getMain().getTemp().toString();
                        String pressure = response.body().getMain().getPressure().toString();
                        String description = response.body().getWeather().get(0).getDescription();

                        mName.setText(name);
                        mTemperature.setText(temperature);
                        mPressure.setText(pressure);
                        mWeather.setText(description);
                        mHumidity.setText(humidity);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<RootWeatherObject> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(this, "No network connection", Toast.LENGTH_SHORT).show();
        }
    }
}
