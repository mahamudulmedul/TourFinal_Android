package bd.org.bitm.mad.batch33.tourmate.model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NetworkConnectivity {
    public static String getDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = simpleDateFormat.format(date);
        return dateString;
    }

    public static long daysDifference(Date currentDate, String departureDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        try {
            Date dd = dateFormat.parse(departureDate);

            long dif = dd.getTime() - currentDate.getTime();

            long hours = dif / (60*60*60);
            long days = hours/24;

            return days;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String milliToDate(long millisecond) {
        Date date =  new Date(millisecond);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        return dateFormat.format(date);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    }

