package shuvalov.nikita.weatheragain;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import shuvalov.nikita.weatheragain.JSONPOJOS.WeatherData;

public class MainActivity extends AppCompatActivity {
    TextView mCity, mWeather, mPressure, mTemperature, mHumidity;
    EditText mUserInput;
    Button mFetchButton;

    public static final String FREEDOM_UNITS ="imperial";
    public static final String CELSIUS = "metric";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        setEmUp();
    }

    public void findViews(){
        mCity = (TextView)findViewById(R.id.city_name_view);
        mWeather = (TextView)findViewById(R.id.current_weather_view);
        mPressure = (TextView)findViewById(R.id.barometric_view);
        mTemperature = (TextView)findViewById(R.id.temp_view);
        mHumidity = (TextView)findViewById(R.id.humidity_view);

        mUserInput = (EditText)findViewById(R.id.user_entry_box);
        mFetchButton = (Button)findViewById(R.id.fetch_button);

    }

    public void setEmUp(){
        mFetchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userInput = mUserInput.getText().toString();
                ConnectivityManager connectivityManager =(ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo info = connectivityManager.getActiveNetworkInfo();
                if (info!=null && info.isConnected()){
                    Retrofit retrofit = new Retrofit.Builder().baseUrl(WeatherAppConstants.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
                    OpenWeatherMapService service = retrofit.create(OpenWeatherMapService.class);
                    Call<WeatherData> call = service.getWeather(userInput, FREEDOM_UNITS);

                    call.enqueue(new Callback<WeatherData>() {
                        @Override
                        public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                            try{
                                String cityName = response.body().getName();
                                String humidity = response.body().getMain().getHumidity().toString();
                                String temperature = String.valueOf(response.body().getMain().getTemp().intValue());
                                String pressure = response.body().getMain().getPressure().toString();
                                String description = response.body().getWeather().get(0).getDescription();


                                knockEmDown(cityName,pressure,description,humidity,temperature);

                            }catch (Exception e ){
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<WeatherData> call, Throwable t) {

                        }
                    });

                }else{
                    Toast.makeText(view.getContext(), "No internet Connectivity", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
    public void knockEmDown(String cityname, String pressure, String description, String humidity, String temperature){
        mCity.setText("City : " +cityname);
        mPressure.setText("Pressure : "+pressure+"mm");
        mWeather.setText("Description : "+description);
        mHumidity.setText("Humidity : "+humidity+ "%");
        mTemperature.setText("Temperature : "+temperature+"°F");
    }

}
