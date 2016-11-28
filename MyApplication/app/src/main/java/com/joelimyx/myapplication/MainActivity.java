package com.joelimyx.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    private EditText mInput;
    private Button mButton;
    private TextView mCityText;
    private TextView mDetailText;
    private TextView mHumidityText;
    private TextView mPressureText;
    private TextView mTemperatureText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInput = (EditText) findViewById(R.id.input);
        mButton = (Button) findViewById(R.id.button);

        mCityText = (TextView) findViewById(R.id.city);
        mDetailText= (TextView) findViewById(R.id.description);
        mHumidityText= (TextView) findViewById(R.id.humidity);
        mPressureText= (TextView) findViewById(R.id.pressure);
        mTemperatureText= (TextView) findViewById(R.id.temperature);

        String baseURL = "http://api.openweathermap.org/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final OpenWeatherService service =  retrofit.create(OpenWeatherService.class);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = mInput.getText().toString();
                Call<City> call = service.getCityWeather(input,"34d5689688afaaf8397ceecdf4c9f1e2","imperial");
                call.enqueue(new Callback<City>() {
                    @Override
                    public void onResponse(Call<City> call, Response<City> response) {
                        try{
                            String name = response.body().getName();
                            String detail = response.body().getWeather().get(0).getDescription();
                            int humidity = response.body().getMain().getHumidity();
                            int pressure = response.body().getMain().getPressure();
                            double temperature = response.body().getMain().getTemp();

                            mCityText.setText("City: "+name);
                            mDetailText.setText("Condition: "+detail);
                            mHumidityText.setText("Humidity: "+String.valueOf(humidity)+"%");
                            mPressureText.setText("Pressure: "+String.valueOf(pressure)+"hPa");
                            mTemperatureText.setText("Temperature: "+String.valueOf(temperature)+"F");

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<City> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        });

    }
}
