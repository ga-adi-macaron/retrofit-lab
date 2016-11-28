package drsdrs.retrofitlab;

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

import drsdrs.retrofitlab.models.WeatherResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    
    

    String apiKey = "5695646f11b05a29e8e560df88dd190b";
    EditText mEditText;
    Button mButton;
    TextView mCityView, mWeatherView, mPressureView, mHumidityView, mTemperatureView;
    String mCityName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditText = (EditText) findViewById(R.id.editText);
        mButton = (Button) findViewById(R.id.button);

        mCityView = (TextView) findViewById(R.id.city);
        mWeatherView = (TextView) findViewById(R.id.weather);
        mPressureView = (TextView) findViewById(R.id.pressure);
        mHumidityView = (TextView) findViewById(R.id.humidity);
        mTemperatureView = (TextView) findViewById(R.id.temperature);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCityName = mEditText.getText().toString();
                getWeatherInfo(mCityName);

            }
        });

    }

    protected void getWeatherInfo(final String mCityName) {

        Log.d("MainActivity: ", "getting city weather");

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {

            String baseURL = "http://api.openweathermap.org/data/2.5/";

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            WeatherService weatherService = retrofit.create(WeatherService.class);

            Call<WeatherResponse> weatherResponseCall = weatherService.getWeather(mCityName, apiKey);

            weatherResponseCall.enqueue(new Callback<WeatherResponse>() {
                @Override
                public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                    WeatherResponse weatherResponse = response.body();

                    String cityName = weatherResponse.getName();
                    String weather = weatherResponse.getWeather().get(0).getMain();
                    String pressure = weatherResponse.getMain().getPressure().toString();
                    String humidity = weatherResponse.getMain().getHumidity().toString();
                    String temperature = weatherResponse.getMain().getTemp().toString();

                    mCityView.setText("City: " + cityName);
                    mWeatherView.setText("Weather " + weather);
                    mPressureView.setText("Pressure " + pressure);
                    mHumidityView.setText("Humidity :" + humidity);
                    mTemperatureView.setText("Temperature :" + temperature);


                }

                @Override
                public void onFailure(Call<WeatherResponse> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Weather Request Failed", Toast.LENGTH_SHORT).show();
                    Log.w(TAG, "onFailure: " + t.getMessage(), t );

                }
            });


        } else {
            Toast.makeText(this, "No Network Connection", Toast.LENGTH_SHORT).show();
        }

    }

}
