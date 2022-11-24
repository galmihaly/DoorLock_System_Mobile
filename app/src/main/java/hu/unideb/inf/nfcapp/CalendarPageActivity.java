package hu.unideb.inf.nfcapp;

import hu.unideb.inf.nfcapp.Databases.Repository;
import hu.unideb.inf.nfcapp.Enums.SQLEnums;
import hu.unideb.inf.nfcapp.Models.MyLog;
import hu.unideb.inf.nfcapp.databinding.CalendarPageBinding;
import hu.unideb.inf.nfcapp.helpers.Helper;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CalendarPageActivity extends AppCompatActivity {

    private LayoutParams textViewParams = null;
    private LayoutParams cardViewParams = null;

    private CalendarPageBinding binding  = null;

    private int year1;
    private int mounth1;
    private int dayOfMounth1;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = CalendarPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        textViewParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        textViewParams.setMargins(25,10,15,10);
        textViewParams.gravity = Gravity.CENTER_HORIZONTAL;

        cardViewParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        cardViewParams.setMargins(25,10,25,10);

        @SuppressLint("SimpleDateFormat")
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        String[] date = timeStamp.split("-");

        processDate(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
        getSelectedDate();
    }

    @SuppressLint("DefaultLocale")
    private void getSelectedDate(){

        binding.myCalendar.setOnDateChangeListener((calendarView, year, mounth, dayOfMounth) -> {

            mounth = mounth + 1;

            year1 = year;
            mounth1 = mounth;
            dayOfMounth1 = dayOfMounth;

            processDate(year, mounth, dayOfMounth);
        });
    }

    private void processDate(int year, int mounth, int dayOfMounth){

        executor.execute(() -> {

            Repository repository = new Repository(Repository.CommunicatorTypeEnum.MsSqlServer);
            final List<MyLog> myLogs = repository.Communicator.getLogsbyDate(year, mounth, dayOfMounth);

            handler.post(() -> {

                if(MyLog._myMessage == SQLEnums.SQL_READING_SUCCES){

                    binding.eventsLinearLayout.removeAllViews();
                    addDateListToTextbox(myLogs);
                    Toast.makeText(CalendarPageActivity.this, "Sikeres!", Toast.LENGTH_LONG).show();
                }
                else if(MyLog._myMessage == SQLEnums.SQL_NO_EVENTS){
                    binding.eventsLinearLayout.removeAllViews();
                    Toast.makeText(CalendarPageActivity.this, "Ezen a napon nem történt belépés!", Toast.LENGTH_LONG).show();
                }
                else if(MyLog._myMessage == SQLEnums.SQL_READING_FAILED){
                    Toast.makeText(CalendarPageActivity.this, "Nem sikerült az adatbázisból olvasni", Toast.LENGTH_LONG).show();
                }
                else if(MyLog._myMessage == SQLEnums.SQL_CONNECTION_FAILED){
                    Toast.makeText(CalendarPageActivity.this, "Nem sikerült az adatbázishoz csatlakozni!", Toast.LENGTH_LONG).show();
                }
            });
        });

    }

    private void addDateListToTextbox(List<MyLog> myLogs) {

        executor.execute(() -> {

            CardView myCardView;
            TextView myTextView;
            String setText;

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

                    final CardView resultCardView = myCardView;

                    handler.post(() -> binding.eventsLinearLayout.addView(resultCardView));
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

                    } else {
                        setText = Helper.getCardLogTextSucces(myLogs.get(k), myLogs.get(k + 1));
                        myCardView.setCardBackgroundColor(Color.rgb(42,150,42));

                    }
                    if(setText != null) myTextView.setText(setText);
                    myCardView.addView(myTextView);

                    final CardView resultCardView = myCardView;

                    handler.post(() -> binding.eventsLinearLayout.addView(resultCardView));
                }
            }
        });
    }

    public void refreshScrollViewButton(View view) {

        processDate(year1, mounth1, dayOfMounth1);
    }
}

