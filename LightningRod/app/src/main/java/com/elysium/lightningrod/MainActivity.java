package com.elysium.lightningrod;

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

import com.elysium.lightningrod.Model.City;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    Button button;
    TextView cityView, descriptionView, humidityView, pressureView, temperatureView;
    String cityName;

    public static final String API_KEY = "a3805d0a057d5e356d6aea4d6a30e4d1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.search_field);
        button = (Button) findViewById(R.id.button);

        cityView = (TextView) findViewById(R.id.city_name);
        descriptionView = (TextView) findViewById(R.id.weather_descript);
        humidityView = (TextView) findViewById(R.id.humidity);
        pressureView = (TextView) findViewById(R.id.pressure);
        temperatureView = (TextView) findViewById(R.id.temperature);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                cityName = editText.getText().toString();
                getCityName(cityName);
            }
        });
    }

    protected void getCityName(String query) {

        Log.d("MainActivity: ", "getting your weather");

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            String baseUrl = "http://api.openweathermap.org";

                    Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            OpenWeatherMapService service = retrofit.create(OpenWeatherMapService.class);

            Call<City> call = service.getCity(query, API_KEY, "imperial"); // TODO - do i also need to add descript, humid, press, and temp to these calls??

            call.enqueue(new Callback<City>() {

                @Override
                public void onResponse(Call<City> call, Response<City> response) {

                   try {

                        /**String city = response.body().getCity();
                        String description = response.body().getWeatherDescription();
                        String humidity = response.body().getHumidity();
                        String pressure = response.body().getPressure();
                        String temperature = response.body().getTemperature();*/

                        cityView.setText(response.body().getName());
                        descriptionView.setText(response.body().getWeather().get(0).getDescription());
                        humidityView.setText("Hunidity: "+ response.body().getMain().getHumidity()+"%");
                        double psi = Math.round(response.body().getMain().getPressure() * 001.45038);
                        psi /= 100;
                        pressureView.setText("Pressure: "+ psi + " PSI");
                        temperatureView.setText("Temperature: "+ response.body().getMain().getTemp().toString() + "\u2109");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<City> call, Throwable t) {
                }
            });

        } else {
            Toast.makeText(MainActivity.this, "Not connected...", Toast.LENGTH_SHORT).show();
        }
    }
}
