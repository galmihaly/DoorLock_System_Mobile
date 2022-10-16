package hu.unideb.inf.nfcapp.helpers;

public class Helper {

    public static String[] getFormattedDate(int year, int mounth, int dayOfMounth, char separator){
        String[] dates = new String[2];
        int firsDayOfMounth = 1;

       if(mounth < 10){
           if (dayOfMounth == 30 || dayOfMounth == 31) {
               dates[0] = String.valueOf(year) + separator + "0" + String.valueOf(mounth) + separator + String.valueOf(dayOfMounth);
               dates[1] = String.valueOf(year) + separator + "0" + String.valueOf(mounth + 1) + separator + String.valueOf(firsDayOfMounth);
           } else {
               dates[0] = String.valueOf(year) + separator + "0" + String.valueOf(mounth) + separator + String.valueOf(dayOfMounth);
               dates[1] = String.valueOf(year) + separator + "0" + String.valueOf(mounth) + separator + String.valueOf(dayOfMounth + 1);
           }
       }
       else {
           if (dayOfMounth == 30 || dayOfMounth == 31) {
               dates[0] = String.valueOf(year) + separator + String.valueOf(mounth) + separator + String.valueOf(dayOfMounth);
               dates[1] = String.valueOf(year) + separator + String.valueOf(mounth + 1) + separator + String.valueOf(firsDayOfMounth);
           } else {
               dates[0] = String.valueOf(year) + separator + String.valueOf(mounth) + separator + String.valueOf(dayOfMounth);
               dates[1] = String.valueOf(year) + separator + String.valueOf(mounth) + separator + String.valueOf(dayOfMounth + 1);
           }
       }

        return dates;
    }
}
