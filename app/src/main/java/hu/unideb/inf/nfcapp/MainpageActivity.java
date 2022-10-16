package hu.unideb.inf.nfcapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import hu.unideb.inf.nfcapp.Databases.Repository;
import hu.unideb.inf.nfcapp.Enums.SQLEnums;
import hu.unideb.inf.nfcapp.Models.MyLog;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainpageActivity extends AppCompatActivity {

    private ImageButton calendarButton;
    private TextView loggedUsername = null;
    private TextView loggedAccountname = null;
    private TextView loggedUserAddress = null;
    private Repository repository = null;
    private TextView lastLoginDate = null;
    private TextView lastLogoutDate = null;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private TextView loggedCardId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        this.loggedUsername = (TextView) findViewById(R.id.loggedUsername);
        this.loggedAccountname = (TextView) findViewById(R.id.loggedAccountname);
        this.loggedUserAddress = (TextView) findViewById(R.id.loggedUserAddress);
        this.loggedCardId = (TextView) findViewById(R.id.loggedCardId);
        this.calendarButton = (ImageButton) findViewById(R.id.calendarButton);
        this.lastLoginDate = (TextView) findViewById(R.id.lastLoginDate);
        this.lastLogoutDate = (TextView) findViewById(R.id.lastLogoutDate);

        Intent intent = getIntent();
        this.loggedUsername.setText(intent.getStringExtra("Username"));
        this.loggedAccountname.setText(intent.getStringExtra("Accountname"));
        this.loggedUserAddress.setText(intent.getStringExtra("Address"));

        Repository repository = new Repository(Repository.CommunicatorTypeEnum.MsSqlServer);
        this.repository = repository;

        String _lastLoginDate = repository.Communicator.getLastLoginDate();
        String _lastLogoutDate = this.repository.Communicator.getLastLogoutDate();
        Log.e("t: ", String.valueOf(_lastLoginDate));

        if (_lastLoginDate != null) {
            if (compareLoginDates(_lastLoginDate, _lastLogoutDate)) {
                this.lastLoginDate.setText(_lastLoginDate);
                this.lastLogoutDate.setText(_lastLogoutDate);
                return;
            }

            this.lastLoginDate.setText(_lastLoginDate);
            this.lastLogoutDate.setText("Még nem jelentkezett\n ki!");

        } else if (!MyLog._myMessage.equals(SQLEnums.SQL_READING_FAILED)) {

            if (MyLog._myMessage.equals(SQLEnums.SQL_CONNECTION_FAILED)) {
                Toast.makeText(this, "Nem sikerült az adatbázishoz csatlakozni!", Toast.LENGTH_LONG).show();
            }

        } else {

            this.lastLoginDate.setText("Nincs bejelentkezési adat!");
        }




    }


    public boolean compareLoginDates(String date1, String date2) {
        try {
            Date d1 = this.sdf.parse(date1);
            Date d2 = this.sdf.parse(date2);
            return d2.after(d1);
        } catch (ParseException e) {
            return false;
        }
    }

    public void logOutButtonClicked(View view) {
        this.loggedUsername.setText((CharSequence) null);
        this.loggedAccountname.setText((CharSequence) null);
        this.loggedUserAddress.setText((CharSequence) null);
        finish();
    }

    public void createCalendarClicked(View view) {
        Intent calendarIntent = new Intent(this, CalendarpageActivity.class);
        startActivity(calendarIntent);
    }

}
