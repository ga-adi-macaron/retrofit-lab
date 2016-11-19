package com.ezequielc.retrofitlab;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ezequielc.retrofitlab.models.RootObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    EditText mEditText;
    Button mButton;
    TextView mCityView, mWeatherView, mPressureView, mHumidityView, mTemperatureView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditText = (EditText) findViewById(R.id.edit_text);
        mButton = (Button) findViewById(R.id.button);
        mCityView = (TextView) findViewById(R.id.city);
        mWeatherView = (TextView) findViewById(R.id.weather);
        mPressureView = (TextView) findViewById(R.id.pressure);
        mHumidityView = (TextView) findViewById(R.id.humidity);
        mTemperatureView = (TextView) findViewById(R.id.temperature);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cityname = mEditText.getText().toString();
                getCityInfo(cityname);
            }
        });
    }

    protected void getCityInfo(String cityname){
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            String baseURL = "http://api.openweathermap.org/data/2.5/";

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            OpenWeatherMapService service = retrofit.create(OpenWeatherMapService.class);

            Call<RootObject> call = service.getInfo(cityname);

            call.enqueue(new Callback<RootObject>() {
                @Override
                public void onResponse(Call<RootObject> call, Response<RootObject> response) {
                    try {
                        String city = response.body().getName();
                        String weather = response.body().getWeather().get(0).getDescription();
                        String pressure = response.body().getMain().getPressure().toString();
                        String humidity = response.body().getMain().getHumidity().toString();
                        String temperature = response.body().getMain().getTemp().toString();

                        mCityView.setText("City: " + city);
                        mWeatherView.setText("Weather: " + weather);
                        mPressureView.setText("Pressure: " + pressure + " hPa");
                        mHumidityView.setText("Humidity: " + humidity + " %");
                        mTemperatureView.setText("Temperature: " + temperature + " °F");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<RootObject> call, Throwable t) {

                }
            });
        } else {
            Toast.makeText(this, "No Network Connection", Toast.LENGTH_SHORT).show();
        }
    }
}
