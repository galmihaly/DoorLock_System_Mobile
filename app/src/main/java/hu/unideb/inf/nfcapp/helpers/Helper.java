package hu.unideb.inf.nfcapp.helpers;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import hu.unideb.inf.nfcapp.Enums.LogTypeEnums;
import hu.unideb.inf.nfcapp.Models.MyLog;

public class Helper {

    private static final String[] dates = new String[2];
    private static final int firsDayOfMounth = 1;
    public static String[] getFormattedDate(int year, int mounth, int dayOfMounth, char separator){
        StringBuilder sb = new StringBuilder();

       if(mounth < 10){
           if (dayOfMounth == 30 || dayOfMounth == 31) {
               dates[0] = sb.append(year).append(separator).append("0").append(mounth).append(separator).append(dayOfMounth).toString();
               sb.setLength(0); // StringBuilder kiürítése
               dates[1] = sb.append(year).append(separator).append("0").append(mounth + 1).append(separator).append(firsDayOfMounth).toString();
           } else {
               dates[0] = sb.append(year).append(separator).append("0").append(mounth).append(separator).append(dayOfMounth).toString();
               sb.setLength(0); // StringBuilder kiürítése
               dates[1] = sb.append(year).append(separator).append("0").append(mounth).append(separator).append(dayOfMounth + 1).toString();
           }
       }
       else {
           if (dayOfMounth == 30 || dayOfMounth == 31) {
               dates[0] = sb.append(year).append(separator).append(mounth).append(separator).append(dayOfMounth).toString();
               sb.setLength(0); // StringBuilder kiürítése
               dates[1] = sb.append(year).append(separator).append(mounth + 1).append(separator).append(firsDayOfMounth).toString();
           } else {
               dates[0] = sb.append(year).append(separator).append(mounth).append(separator).append(dayOfMounth).toString();
               sb.setLength(0); // StringBuilder kiürítése
               dates[1] = sb.append(year).append(separator).append(mounth).append(separator).append(dayOfMounth + 1).toString();
           }
       }
        return dates;
    }

    public static String getCardLogTextSucces(MyLog myLog, MyLog myLog1) {
        StringBuilder sb = new StringBuilder();

        if(myLog._logTypeId == LogTypeEnums.LOGIN_CARD.getLevelCode() && myLog1._logTypeId == LogTypeEnums.LOGOUT_CARD.getLevelCode()){
            return sb.append("Belépés ideje: ").append(myLog._time).append(", Kapu: ").append(myLog._gateId).append(", Típus: NFC kártya.\n")
                     .append("Kilépés ideje:  ").append(myLog1._time).append(", Kapu: ").append(myLog1._gateId).append(", Típus: NFC kártya.")
                     .toString();
        }
        else if(myLog._logTypeId == LogTypeEnums.LOGIN_CARD.getLevelCode() && myLog1._logTypeId == LogTypeEnums.LOGOUT_PASSWORD.getLevelCode()){
            return sb.append("Belépés ideje: ").append(myLog._time).append(", Kapu: ").append(myLog._gateId).append(", Típus: NFC kártya.\n")
                     .append("Kilépés ideje:  ").append(myLog1._time).append(", Kapu: ").append(myLog1._gateId).append(", Típus: Jelszó.")
                     .toString();
        }
        else if(myLog._logTypeId == LogTypeEnums.LOGIN_PASSWORD.getLevelCode() && myLog1._logTypeId == LogTypeEnums.LOGOUT_CARD.getLevelCode()){
            return sb.append("Belépés ideje: ").append(myLog._time).append(", Kapu: ").append(myLog._gateId).append(", Típus: Jelszó.\n")
                     .append("Kilépés ideje:  ").append(myLog1._time).append(", Kapu: ").append(myLog1._gateId).append(", Típus: NFC kártya.")
                     .toString();
        }
        else if(myLog._logTypeId == LogTypeEnums.LOGIN_PASSWORD.getLevelCode() && myLog1._logTypeId == LogTypeEnums.LOGOUT_PASSWORD.getLevelCode()){
            return sb.append("Belépés ideje: ").append(myLog._time).append(", Kapu: ").append(myLog._gateId).append(", Típus: Jelszó.\n")
                     .append("Kilépés ideje:  ").append(myLog1._time).append(", Kapu: ").append(myLog1._gateId).append(", Típus: Jelszó.")
                     .toString();
        }
        return null;
    }

    public static String getCardLogTextFailed(MyLog myLog) {
        StringBuilder sb = new StringBuilder();

        if(myLog._logTypeId == LogTypeEnums.LOGIN_CARD.getLevelCode()){
            return sb.append("Belépés ideje: ").append(myLog._time).append(", Kapu: ").append(myLog._gateId).append(", Típus: NFC kártya.\n")
                     .append("Kilépés ideje:  nem történt kilépés!")
                     .toString();
        }
        else if(myLog._logTypeId == LogTypeEnums.LOGIN_PASSWORD.getLevelCode()){
            return sb.append("Belépés ideje: ").append(myLog._time).append(", Kapu: ").append(myLog._gateId).append(", Típus: Jelszó.\n")
                     .append("Kilépés ideje:  nem történt kilépés!")
                     .toString();
        }
        return null;
    }


    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static Date d1;
    private static Date d2;
    public static boolean compareLoginDates(String date1, String date2) {

        try {
            d1 = sdf.parse(date1);
            d2 = sdf.parse(date2);

        } catch (ParseException e) {
            return false;
        }

        if (d2 != null && d1 != null){
            return d2.after(d1);
        }
        return false;
    }

    public static String parseTimeToHoursAndDay(String lastPassedTime) {

        int days = Integer.parseInt(lastPassedTime) / (60 * 24);
        int hours = (days % (60 * 24)) / 60;
        int minutes = (days % (60 * 24)) % 60;

        return days + " nap " + hours + " óra " + minutes + " perc.";
    }
}
