package bd.org.bitm.mad.batch33.tourmate.Weather;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import bd.org.bitm.mad.batch33.tourmate.R;
import bd.org.bitm.mad.batch33.tourmate.Utils.ForecastAdapter;
import bd.org.bitm.mad.batch33.tourmate.Utils.ForecastWeather;
import bd.org.bitm.mad.batch33.tourmate.Utils.WeatherForecastApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForeCastWeatherFragment extends Fragment {
    public static boolean forecastFragmentRefresh = false;

    private double latitude;
    private double longitude;
    private int cnt = 7;
    private String units = "metric"; //imperial

    TextView noDataTV;

    private RecyclerView forecastRecyclerView;
    private RecyclerView.Adapter forecastAdapter;

    List<ForecastWeather.List> weatherList;

    private Retrofit retrofit;
    private WeatherForecastApi weatherForecastApi;
    private String urlString;

    SharedPreference sharedPreference;
    private static final String FORECAST_BASE_URL = "https://api.openweathermap.org/data/2.5/forecast/";

    public ForeCastWeatherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fore_cast_weather, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreference = new SharedPreference(getContext());
        forecastRecyclerView = view.findViewById(R.id.weatherForecastRecyclerView);
        forecastRecyclerView.setHasFixedSize(true);
        forecastRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        sharedPreference = new SharedPreference(getContext());
        units = sharedPreference.getTempUnit();
        latitude = sharedPreference.getCityLat();
        longitude = sharedPreference .getCityLon();

        noDataTV = view.findViewById(R.id.noDataTextView);

        retrofit = new Retrofit.Builder()
                .baseUrl(FORECAST_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        weatherForecastApi = retrofit.create(WeatherForecastApi.class);

        urlString = String.format("daily?lat=%f&lon=%f&units=%s&cnt=%d&appid=%s",
                latitude,
                longitude,
                units,
                cnt,
                getString(R.string.weather_api_key));

        Call<ForecastWeather> responseCall = weatherForecastApi.getWeatherData(urlString);

        responseCall.enqueue(new Callback<ForecastWeather>() {
            @Override
            public void onResponse(Call<ForecastWeather> call, Response<ForecastWeather> response) {
                if (response.isSuccessful()) {
                    ForecastWeather forecastWeather = response.body();

                    weatherList = forecastWeather.getList();
                    forecastAdapter = new ForecastAdapter(weatherList, getContext());

                    forecastRecyclerView.setAdapter(forecastAdapter);
                }
            }

            @Override
            public void onFailure(Call<ForecastWeather> call, Throwable t) {
                noDataTV.setVisibility(View.VISIBLE);
            }
        });
    }
}


