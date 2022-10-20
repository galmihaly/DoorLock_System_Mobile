package hu.unideb.inf.nfcapp;

import hu.unideb.inf.nfcapp.Databases.Repository;
import hu.unideb.inf.nfcapp.Enums.SQLEnums;
import hu.unideb.inf.nfcapp.Models.MyLog;
import hu.unideb.inf.nfcapp.helpers.Helper;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarpageActivity extends AppCompatActivity {

    private CalendarView myCalendarView;
    private List<MyLog> myLogs;
    private Repository repository;
    private LinearLayout myLinearLayout;
    private ImageButton refreshScrollViewButton;
    private LayoutParams textViewParams;
    private LayoutParams cardViewParams;
    private CardView myCardView;
    private TextView myTextView;
    private String setText;
    private int year1;
    private int mounth1;
    private int dayOfMounth1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_page);
        myLinearLayout = findViewById(R.id.eventsLinearLayout);
        myCalendarView = findViewById(R.id.myCalendar);
        refreshScrollViewButton = findViewById(R.id.refreshScrollViewBtn);

        textViewParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        textViewParams.setMargins(20,10,20,10);

        cardViewParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL);
        cardViewParams.setMargins(0,10,0,10);

        String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        String[] date = timeStamp.split("-");
        processDate(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));

        getSelectedDate();
    }

    @SuppressLint("DefaultLocale")
    private void getSelectedDate(){

        myCalendarView.setOnDateChangeListener((calendarView, year, mounth, dayOfMounth) -> {

            mounth = mounth + 1;

            year1 = year;
            mounth1 = mounth;
            dayOfMounth1 = dayOfMounth;

            processDate(year, mounth, dayOfMounth);
        });
    }

    private void processDate(int year, int mounth, int dayOfMounth){

        repository = new Repository(Repository.CommunicatorTypeEnum.MsSqlServer);
        myLogs = new ArrayList<>();
        Log.e("k:", year + " " + mounth + " " + dayOfMounth );
        myLogs = repository.Communicator.getLogsbyDate(year, mounth, dayOfMounth);

        if(MyLog._myMessage == SQLEnums.SQL_READING_SUCCES){

            myLinearLayout.removeAllViews();
            addDateListToTextbox(myLogs);
            Toast.makeText(CalendarpageActivity.this, "Sikeres!", Toast.LENGTH_LONG).show();
        }
        else if(MyLog._myMessage == SQLEnums.SQL_NO_EVENTS){
            myLinearLayout.removeAllViews();
            Toast.makeText(CalendarpageActivity.this, "Ezen a napon nem történt belépés!", Toast.LENGTH_LONG).show();
        }
        else if(MyLog._myMessage == SQLEnums.SQL_READING_FAILED){
            Toast.makeText(CalendarpageActivity.this, "Nem sikerült az adatbázisból olvasni", Toast.LENGTH_LONG).show();
        }
        else if(MyLog._myMessage == SQLEnums.SQL_CONNECTION_FAILED){
            Toast.makeText(CalendarpageActivity.this, "Nem sikerült az adatbázishoz csatlakozni!", Toast.LENGTH_LONG).show();
        }
    }

    private void addDateListToTextbox(List<MyLog> myLogs) {

        if(myLogs.size() % 2 == 0){
            for (int i  = 0; i < myLogs.size(); i += 2){
                setText = Helper.getCardLogTextSucces(myLogs.get(i), myLogs.get(i + 1));

                myTextView = new TextView(this);
                myTextView.setLayoutParams(textViewParams);
                myTextView.setTextColor(Color.WHITE);
                myTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

                myCardView = new CardView(this);
                myCardView.setLayoutParams(cardViewParams);
                myCardView.setRadius(17);
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
                myTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

                myCardView = new CardView(this);
                myCardView.setRadius(17);
                myCardView.setLayoutParams(cardViewParams);

                if (k == myLogs.size() - 1) {
                    setText = Helper.getCardLogTextFailed(myLogs.get(k));
                    myCardView.setCardBackgroundColor(Color.rgb(229,45,33));

                    if(setText != null) myTextView.setText(setText);

                } else {
                    setText = Helper.getCardLogTextSucces(myLogs.get(k), myLogs.get(k + 1));
                    myCardView.setCardBackgroundColor(Color.rgb(42,150,42));

                    if(setText != null) myTextView.setText(setText);

                }
                myCardView.addView(myTextView);
                myLinearLayout.addView(myCardView);
            }
        }
    }

    public void refreshScrollViewButton(View view) {

        processDate(year1, mounth1, dayOfMounth1);
    }
}

