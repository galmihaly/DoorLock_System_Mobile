package hu.unideb.inf.nfcapp;

import hu.unideb.inf.nfcapp.Databases.Repository;
import hu.unideb.inf.nfcapp.Enums.LogEnums;
import hu.unideb.inf.nfcapp.Models.MyLog;

import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import net.sourceforge.jtds.jdbc.DateTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CalendarpageActivity extends AppCompatActivity {

    private CalendarView myCalendarView;
    private TextView testTextView;
    private long seged;
    private StringBuilder sb = new StringBuilder();
    private List<MyLog> myLogs;
    private Repository repository;
    private Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_page);
        testTextView = findViewById(R.id.testTextView);

        myCalendarView = findViewById(R.id.myCalendar);

        getDate();
    }

    private void getDate(){

        myCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int mounth, int dayOfMounth) {
                seged = myCalendarView.getDate();
                mounth = mounth + 1;
                if(mounth < 10){

                    testTextView.setText(String.valueOf(year) + "-0" + String.valueOf(mounth) + "-" + String.valueOf(dayOfMounth) + " és " + String.valueOf(year) + "-0" + String.valueOf(mounth) + "-" + String.valueOf(dayOfMounth + 1));
                }
                else{

                    testTextView.setText(String.valueOf(year) + "-" + String.valueOf(mounth) + "-" + String.valueOf(dayOfMounth));
                }

                repository = new Repository(Repository.CommunicatorTypeEnum.MsSqlServer);
                myLogs = new ArrayList<>();
                myLogs = repository.Communicator.getLogbyDate(year, mounth, dayOfMounth);

                try {
                    date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(myLogs.get(0)._date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                if(MyLog._myMessage == LogEnums.LOG_HAS_EVENTS){
                    testTextView.setText(String.valueOf(date.getTime()));
                    Toast.makeText(CalendarpageActivity.this, "Sikeres!", Toast.LENGTH_LONG).show();
                }
                else if(MyLog._myMessage == LogEnums.LOG_NO_EVENTS){
                    Toast.makeText(CalendarpageActivity.this, "Ezen a napon nem történt belépés!", Toast.LENGTH_LONG).show();
                }
                else if(MyLog._myMessage == LogEnums.SQL_READING_FAILED){
                    Toast.makeText(CalendarpageActivity.this, "Nem sikerült az adatbázisból olvasni", Toast.LENGTH_LONG).show();
                }
                else if(MyLog._myMessage == LogEnums.SQL_CONNECTION_FAILED){
                    Toast.makeText(CalendarpageActivity.this, "Nem sikerült az adatbázishoz csatlakozni!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}

