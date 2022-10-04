package hu.unideb.inf.nfcapp;

import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class CalendarpageActivity extends AppCompatActivity {

    private CalendarView myCalendarView;
    private TextView testTextView;
    private DateFormat dateFormat;
    private long seged;
    private StringBuilder sb = new StringBuilder();
    private List<MyLog> myLogs;
    private Repository repository;

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
                if(mounth < 10){

                    testTextView.setText(String.valueOf(year) + "-0" + String.valueOf(mounth) + "-" + String.valueOf(dayOfMounth) + " és " + String.valueOf(year) + "-0" + String.valueOf(mounth) + "-" + String.valueOf(dayOfMounth + 1));
                }
                else{

                    testTextView.setText(String.valueOf(year) + "-" + String.valueOf(mounth) + "-" + String.valueOf(dayOfMounth));
                }

                repository = new Repository(Repository.CommunicatorTypeEnum.MsSqlServer);

                myLogs = new ArrayList<>();
                myLogs = repository.Communicator.getLogbyDate(year, mounth, dayOfMounth);

                if(myLogs == null){
                    Toast.makeText(CalendarpageActivity.this, "Hiba !!!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}

