package com.scottlindley.retrofitlab;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.scottlindley.retrofitlab.Model.City;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    public static final String API_KEY = "8125261db99aefc2183578b967646acc";
    private EditText mCityEdit;
    private Button mFetchButton;
    private TextView mCityName, mWeather, mHumidity, mPressure, mTemperature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCityEdit = (EditText)findViewById(R.id.city_edit);
        mFetchButton = (Button)findViewById(R.id.search_button);
        mCityName = (TextView)findViewById(R.id.city_name);
        mWeather = (TextView)findViewById(R.id.weather_text);
        mHumidity = (TextView)findViewById(R.id.humidity_text);
        mPressure = (TextView)findViewById(R.id.pressure_text);
        mTemperature = (TextView)findViewById(R.id.temp_text);

        mFetchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCityWeather(mCityEdit.getText().toString());
            }
        });
    }

    public void getCityWeather(String query){
        ConnectivityManager connectivityManager =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if(info!=null && info.isConnected()){
            String baseUrl = "http://api.openweathermap.org";
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            OpenWeatherMapService service = retrofit.create(OpenWeatherMapService.class);
            Call<City> call = service.getCity(query, API_KEY, "imperial");
            call.enqueue(new Callback<City>() {
                @Override
                public void onResponse(Call<City> call, Response<City> response) {
                    try{
                        mCityName.setText(response.body().getName());
                        mWeather.setText(response.body().getWeather().get(0).getDescription());
                        mHumidity.setText("Humidity: "+response.body().getMain().getHumidity()+"%");
                        double psi = Math.round(response.body().getMain().getPressure() * 001.45038);
                        psi /= 100;
                        mPressure.setText("Pressure: "+psi+" PSI");
                        mTemperature.setText("Temperature: "+response.body().getMain().getTemp().toString()+"\u2109");
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<City> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }else{
            Toast.makeText(this, "No network connection found", Toast.LENGTH_SHORT).show();
        }
    }
}
