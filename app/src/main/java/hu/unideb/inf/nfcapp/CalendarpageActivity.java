package hu.unideb.inf.nfcapp;

import hu.unideb.inf.nfcapp.Databases.Repository;
import hu.unideb.inf.nfcapp.Enums.SQLEnums;
import hu.unideb.inf.nfcapp.Models.MyLog;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;
import java.util.List;

public class CalendarpageActivity extends AppCompatActivity {

    private CalendarView myCalendarView;
    private List<MyLog> myLogs;
    private Repository repository;
    private LinearLayout myLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_page);
        myLinearLayout = findViewById(R.id.eventsLinearLayout);

        myCalendarView = findViewById(R.id.myCalendar);

        getDate();
    }

    @SuppressLint("DefaultLocale")
    private void getDate(){

        myCalendarView.setOnDateChangeListener((calendarView, year, mounth, dayOfMounth) -> {

            mounth = mounth + 1;

            myLinearLayout.removeAllViews();

            repository = new Repository(Repository.CommunicatorTypeEnum.MsSqlServer);
            myLogs = new ArrayList<>();
            myLogs = repository.Communicator.getLogsbyDate(year, mounth, dayOfMounth);


            if(MyLog._myMessage == SQLEnums.SQL_READING_SUCCES){

                addDateListToTextbox(myLogs);
                Toast.makeText(CalendarpageActivity.this, "Sikeres!", Toast.LENGTH_LONG).show();
            }
            else if(MyLog._myMessage == SQLEnums.SQL_NO_EVENTS){
                Toast.makeText(CalendarpageActivity.this, "Ezen a napon nem történt belépés!", Toast.LENGTH_LONG).show();
            }
            else if(MyLog._myMessage == SQLEnums.SQL_READING_FAILED){
                Toast.makeText(CalendarpageActivity.this, "Nem sikerült az adatbázisból olvasni", Toast.LENGTH_LONG).show();
            }
            else if(MyLog._myMessage == SQLEnums.SQL_CONNECTION_FAILED){
                Toast.makeText(CalendarpageActivity.this, "Nem sikerült az adatbázishoz csatlakozni!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addDateListToTextbox(List<MyLog> myLogs) {

        LayoutParams textViewParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        LayoutParams cardViewParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL);

        textViewParams.setMargins(30,15,30,15);
        cardViewParams.setMargins(0,10,0,10);

        String setText;

        CardView myCardView;
        TextView myTextView;
        if(myLogs.size() % 2 == 0){
            for (int i  = 0; i < myLogs.size(); i += 2){
                setText = getCardLogTextSucces(myLogs.get(i), myLogs.get(i + 1));

                myTextView = new TextView(this);
                myTextView.setLayoutParams(textViewParams);
                myTextView.setTextColor(Color.WHITE);

                myCardView = new CardView(this);
                myCardView.setLayoutParams(cardViewParams);
                myCardView.setRadius(10);
                myCardView.setCardBackgroundColor(Color.rgb(42,150,42));


                if(setText != null) myTextView.setText(setText);

                myCardView.addView(myTextView);

                myLinearLayout.addView(myCardView);
            }
        }
        else{
            for (int k  = 0; k < myLogs.size(); k += 2){

                myTextView = new TextView(this);
                myTextView.setLayoutParams(textViewParams);
                myTextView.setTextColor(Color.WHITE);

                myCardView = new CardView(this);
                myCardView.setLayoutParams(cardViewParams);
                myCardView.setRadius(10);

                if (k == myLogs.size() - 1) {
                    setText = getCardLogTextFailed(myLogs.get(k));
                    myCardView.setCardBackgroundColor(Color.rgb(229,45,33));

                    if(setText != null) myTextView.setText(setText);

                } else {
                    setText = getCardLogTextSucces(myLogs.get(k), myLogs.get(k + 1));
                    myCardView.setCardBackgroundColor(Color.rgb(42,150,42));

                    if(setText != null) myTextView.setText(setText);

                }
                myCardView.addView(myTextView);
                myLinearLayout.addView(myCardView);
            }
        }
    }

    private String getCardLogTextFailed(MyLog myLog) {

        String seged = null;

        if(myLog._logTypeId == 200){
            seged = "Belépés ideje: " + myLog._time + ", Kapu: " + myLog._gateId + ", Típus: NFC kártya.\n" + "Kilépés ideje:  nem történt kilépés!";
        }
        else if(myLog._logTypeId == 201){
            seged = "Belépés ideje: " + myLog._time + ", Kapu: " + myLog._gateId + ", Típus: Jelszó.\n" + "Kilépés ideje:  nem történt kilépés!";
        }

        return seged;
    }

    private String getCardLogTextSucces(MyLog myLog, MyLog myLog1) {

        String seged = null;

        if(myLog._logTypeId == 200 && myLog1._logTypeId == 300){
            seged = "Belépés ideje: " + myLog._time + ", Kapu: " + myLog._gateId + ", Típus: NFC kártya.\n" + "Kilépés ideje:  " + myLog1._time + ", Kapu: " + myLog1._gateId + ", Típus: NFC kártya.";
        }
        else if(myLog._logTypeId == 200 && myLog1._logTypeId == 301){
            seged = "Belépés ideje: " + myLog._time + ", Kapu: " + myLog._gateId + ", Típus: NFC kártya.\n" + "Kilépés ideje:  " + myLog1._time + ", Kapu: " + myLog1._gateId + ", Típus: Jelszó.";
        }
        else if(myLog._logTypeId == 201 && myLog1._logTypeId == 300){
            seged = "Belépés ideje: " + myLog._time + ", Kapu: " + myLog._gateId + ", Típus: Jelszó.\n" + "Kilépés ideje:  " + myLog1._time + ", Kapu: " + myLog1._gateId + ", Típus: NFC kártya.";
        }
        else if(myLog._logTypeId == 201 && myLog1._logTypeId == 301){
            seged = "Belépés ideje: " + myLog._time + ", Kapu: " + myLog._gateId + ", Típus: Jelszó.\n" + "Kilépés ideje:  " + myLog1._time + ", Kapu: " + myLog1._gateId + ", Típus: Jelszó.";
        }

        return seged;
    }


}

