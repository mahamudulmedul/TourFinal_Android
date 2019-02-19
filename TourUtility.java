package bd.org.bitm.mad.batch33.tourmate.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import bd.org.bitm.mad.batch33.tourmate.model.Expense;

public class TourUtility {
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

    public static double sumOfExpenses(List<Expense> expenses) {
        double sum = 0;
        for(Expense e : expenses) {
            sum = sum + e.getExpenseAmount();
        }
        return sum;
    }
    public static int getProgressPercentage(double totalBudget, double totalExpense) {
        int result = (int) ((totalExpense/totalBudget)*100);
        return result;
    }

}
