package bd.org.bitm.mad.batch33.tourmate.Utils;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import bd.org.bitm.mad.batch33.tourmate.R;
import bd.org.bitm.mad.batch33.tourmate.Weather.SharedPreference;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder> {

    private List<ForecastWeather.List> forecastWeatherList;
    private Context context;
    SharedPreference sharedPreference;
    private String units;

    public ForecastAdapter(List<ForecastWeather.List> forecastWeatherList, Context context) {
        this.forecastWeatherList = forecastWeatherList;
        this.context = context;
        sharedPreference = new SharedPreference(context);
        units = sharedPreference.getTempUnit();
    }

    @Override
    public ForecastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.forecast_list_item, parent, false);

        return new ForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ForecastViewHolder holder, int position) {
        ForecastWeather.List currentForecastWeather = forecastWeatherList.get(position);
        ForecastWeather.Temp temperature = currentForecastWeather.getTemp();

        String iconId = currentForecastWeather.getWeather().get(0).getIcon();

        if (position == 0) {
            holder.dayTV.setText("Today");
        } else if (position == 1) {
            holder.dayTV.setText("Tomorrow");
        } else {
            holder.dayTV.setText(unixToDay(currentForecastWeather.getDt()));
        }

        if (units.equals("metric")){
            holder.maxTV.setText("Max: " + temperature.getMax() + "\u00B0 C");
            holder.minTV.setText("Min: " + temperature.getMin() + "\u00B0 C");
        }else if (units.equals("imperial")){
            holder.maxTV.setText("Max: " + temperature.getMax() + "\u00B0 F");
            holder.minTV.setText("Min: " + temperature.getMin() + "\u00B0 F");
        }
        //holder.maxTV.setText("Max: " + temperature.getMax() + "\u00B0 C");
        holder.dateTV.setText(unixToDate(currentForecastWeather.getDt()));
        //holder.minTV.setText("Min: " + temperature.getMin() + "\u00B0 C");

        Picasso.get().load("https://openweathermap.org/img/w/" + iconId + ".png")
                .into(holder.weatherIV);

    }

    @Override
    public int getItemCount() {
        return forecastWeatherList.size();
    }

    public class ForecastViewHolder extends RecyclerView.ViewHolder {

        TextView dayTV;
        TextView minTV;
        ImageView weatherIV;
        TextView dateTV;
        TextView maxTV;

        public ForecastViewHolder(View itemView) {
            super(itemView);

            dayTV = itemView.findViewById(R.id.dayTextView);
            minTV = itemView.findViewById(R.id.minTempTextView);
            weatherIV = itemView.findViewById(R.id.weatherIconImageView);
            dateTV = itemView.findViewById(R.id.dateTextView);
            maxTV = itemView.findViewById(R.id.maxTempTextView);
        }
    }

    private String unixToDay(long timeStamp) {
        Date dateTime = new Date((long)timeStamp*1000);
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateTime);
        int dayInt = cal.get(Calendar.DAY_OF_WEEK);

        String dayStr = "Saturday";

        switch (dayInt) {
            case Calendar.SATURDAY:
                dayStr = "Saturday";
                break;
            case Calendar.SUNDAY:
                dayStr = "Sunday";
                break;
            case Calendar.MONDAY:
                dayStr = "Monday";
                break;
            case Calendar.TUESDAY:
                dayStr = "Tuesday";
                break;
            case Calendar.WEDNESDAY:
                dayStr = "Wednesday";
                break;
            case Calendar.THURSDAY:
                dayStr = "Thursday";
                break;
            case Calendar.FRIDAY:
                dayStr = "Friday";
                break;
        }

        return dayStr;
    }

    private String unixToDate(long timestamp) {
        // convert seconds to milliseconds
        Date date = new Date(timestamp*1000L);
        // the format of your date
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = sdf.format(date);

        return formattedDate;
    }

    private String getTimeFromUnix(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp * 1000);

        Date d = calendar.getTime();
        String timeStr = new SimpleDateFormat("hh:mm a").format(d);

        return timeStr;
    }
}
