package com.example.jeanweatherwax.openweatherapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jeanweatherwax.openweatherapp.models.Model;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private static final String BASE_URL = "http://api.openweathermap.org";
    private static final String APP_ID = "c9460bbff7b36af0686c2dd73bfae04a";
    private static final String UNITS = "imperial";

    private EditText mEditText;
    private TextView mCityView, mDescriptionView, mHumidityView, mPressureView, mTemperatureView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditText = (EditText) findViewById(R.id.edittext);
        mCityView = (TextView) findViewById(R.id.city);
        mDescriptionView = (TextView) findViewById(R.id.description);
        mHumidityView = (TextView) findViewById(R.id.humidity);
        mPressureView = (TextView) findViewById(R.id.pressure);
        mTemperatureView = (TextView) findViewById(R.id.temperature);

        Button button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String location = mEditText.getText().toString();
                getWeatherReport(location);
            }
        });
    }

    protected void getWeatherReport(String location) {

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            OpenWeatherInterface weatherService = retrofit.create(OpenWeatherInterface.class);

            Call<Model> call = weatherService.getWeather(location, UNITS, APP_ID);

            call.enqueue(new Callback<Model>() {
                @Override
                public void onResponse(Call<Model> call, Response<Model> response) {
                    try {
                        Model weatherData = response.body();
                        Locale locale = Locale.getDefault();

                        mCityView.setText(String.format(locale,
                                getString(R.string.city_text),
                                weatherData.getName()));

                        mDescriptionView.setText(String.format(locale,
                                getString(R.string.weather_description_text),
                                weatherData.getWeather().get(0).getDescription()));

                        mHumidityView.setText(String.format(locale,
                                getString(R.string.humidity_text),
                                weatherData.getMain().getHumidity()));

                        mPressureView.setText(String.format(locale,
                                getString(R.string.pressure_text),
                                weatherData.getMain().getPressure()));

                        mTemperatureView.setText(String.format(locale,
                                getString(R.string.temperature_text),
                                weatherData.getMain().getTemp()));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Model> call, Throwable t) {
                    Log.d(TAG, "onFailure: call to OpenWeather failed");
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(MainActivity.this, "No network connection", Toast.LENGTH_LONG).show();
        }
    }
}
