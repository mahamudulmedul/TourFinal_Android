package bd.org.bitm.mad.batch33.tourmate.Utils;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface WeatherCurrentApi {

     @GET
     Call<CurrentWeather> getCurrentWeatherData(@Url String urlString);
}
