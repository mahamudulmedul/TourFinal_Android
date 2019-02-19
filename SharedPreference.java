package bd.org.bitm.mad.batch33.tourmate.Weather;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SharedPreference {

    private static final String PREFERENCE_NAME = "Shared Pref";
    private static final String TEMP_UNIT = "unit";
    private static final String DEFAULT_MSG = "metric";
    private static final String CITY_LAT = "lat";
    private static final String CITY_LONG= "long";
    private static final String IS_METRIC= "isMetric";
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SharedPreference(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME,MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void changeTempToFahrenheit(){
        editor.putString(TEMP_UNIT,"imperial");
        editor.commit();
    }
    public void changeTempToCelsius(){
        editor.putString(TEMP_UNIT,"metric");
        editor.commit();
    }
    public String getTempUnit(){
        return sharedPreferences.getString(TEMP_UNIT,DEFAULT_MSG);
    }

    public void setCityLatLong(double lat, double lon){
        editor.putString(CITY_LAT,String.valueOf(lat));
        editor.commit();
        editor.putString(CITY_LONG,String.valueOf(lon));
        editor.commit();
    }

    public double getCityLat(){
        String cityLat =  sharedPreferences.getString(CITY_LAT,"23.8103");
        return Double.valueOf(cityLat);
    }
    public double getCityLon(){
        String cityLon =  sharedPreferences.getString(CITY_LONG,"90.4125");
        return Double.valueOf(cityLon);
    }

    public void setIsMetric(boolean isMetric){
        editor.putBoolean(IS_METRIC,isMetric);
        editor.commit();
    }

    public boolean getIsMetric(){
        return sharedPreferences.getBoolean(IS_METRIC,true);
    }

}
