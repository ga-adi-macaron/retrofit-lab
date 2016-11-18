package com.korbkenny.kennyretrofitlabweather;

import android.content.Context;
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

import com.korbkenny.kennyretrofitlabweather.models.GoodWeather;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    public static final String API_KEY = "9aef2a156ab77f87deb121a0bf6de799";
    EditText mEditText;
    Button mButton;
    TextView mCity, mWeather, mPressure, mHumidity, mTemperature;
    String mTheCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditText = (EditText) findViewById(R.id.edit_text);
        mButton = (Button) findViewById(R.id.fetch_button);

        mCity = (TextView) findViewById(R.id.viewCity);
        mWeather = (TextView) findViewById(R.id.viewWeather);
        mPressure = (TextView) findViewById(R.id.viewPressure);
        mHumidity = (TextView) findViewById(R.id.viewHumidity);
        mTemperature = (TextView) findViewById(R.id.viewTemperature);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWeatherStuff(mEditText.getText().toString());
            }
        });
    }

    protected void getWeatherStuff(String city){
        ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMan.getActiveNetworkInfo();
        if(netInfo!=null && netInfo.isConnected()){
//            mTheCity = city;
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            OpenWeatherService service = retrofit.create(OpenWeatherService.class);

            Call<GoodWeather> call = service.getWeather(city);
            call.enqueue(new Callback<GoodWeather>() {
                @Override
                public void onResponse(Call<GoodWeather> call, Response<GoodWeather> response) {
                    Log.d("ok", "onResponse: made it this far...");
                    try {
                        String weather = response.body().getWeather().get(0).getMain();
                        String pressure = response.body().getMain().getPressure().toString();
                        String humidity = response.body().getMain().getHumidity().toString();
                        String temperature = response.body().getMain().getTemp().toString();

                        mCity.setText(mTheCity);
                        mWeather.setText("It is " + weather + " outside!");
                        mTemperature.setText("Temperature: " + temperature);
                        mPressure.setText("Pressure: " + pressure);
                        mHumidity.setText("Humidity: " + humidity);
                        Log.d("nice", "onResponse: well why isn't it changing textviews?");
                    }catch(Exception e){
                        Toast.makeText(MainActivity.this, "CAUGHT!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<GoodWeather> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "This happened", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(MainActivity.this, "No network connection", Toast.LENGTH_LONG).show();
        }
    }
}
