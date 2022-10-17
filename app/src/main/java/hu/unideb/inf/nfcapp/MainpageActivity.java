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
import hu.unideb.inf.nfcapp.helpers.Helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class MainpageActivity extends AppCompatActivity {

    private TextView loggedUsername = null;
    private TextView loggedAccountname = null;
    private TextView loggedUserAddress = null;
    private final Repository repository = null;
    private TextView lastLoginDate;
    private TextView lastLogoutDate;
    private final StringBuilder sb = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        loggedUsername = (TextView) findViewById(R.id.loggedUsername);
        loggedAccountname = (TextView) findViewById(R.id.loggedAccountname);
        loggedUserAddress = (TextView) findViewById(R.id.loggedUserAddress);
        TextView loggedCardId = (TextView) findViewById(R.id.loggedCardId);
        ImageButton calendarButton = (ImageButton) findViewById(R.id.calendarButton);
        lastLoginDate = (TextView) findViewById(R.id.lastLoginDate);
        lastLogoutDate = (TextView) findViewById(R.id.lastLogoutDate);

        Intent intent = getIntent();
        loggedUsername.setText(intent.getStringExtra("Username"));
        loggedAccountname.setText(intent.getStringExtra("Accountname"));
        loggedUserAddress.setText(intent.getStringExtra("Address"));

        getLastLogDate();


        Repository repository = new Repository(Repository.CommunicatorTypeEnum.MsSqlServer);

        List<Integer> gatePermissions = repository.Communicator.getGatePermissions();

        if(gatePermissions.size() != 0){

            for(int i = 0; i < gatePermissions.size(); i++)
                sb.append(gatePermissions.get(i)).append(", ");

            loggedCardId.setText(sb.toString());
            sb.setLength(0);
        }
    }

    public void logOutButtonClicked(View view) {
        loggedUsername.setText((CharSequence) null);
        loggedAccountname.setText((CharSequence) null);
        loggedUserAddress.setText((CharSequence) null);
        finish();
    }

    public void createCalendarClicked(View view) {
        Intent calendarIntent = new Intent(this, CalendarpageActivity.class);
        startActivity(calendarIntent);
    }

    public void refreshStatisticsClicked(View view){
        getLastLogDate();
    }

    public void getLastLogDate(){

        Repository repository = new Repository(Repository.CommunicatorTypeEnum.MsSqlServer);

        String _lastLoginDate = repository.Communicator.getLastLoginDate();
        String _lastLogoutDate = repository.Communicator.getLastLogoutDate();

        if (_lastLoginDate != null && _lastLogoutDate != null) {
            if (Helper.compareLoginDates(_lastLoginDate, _lastLogoutDate)) {
                lastLoginDate.setText(_lastLoginDate);
                lastLogoutDate.setText(_lastLogoutDate);
            }
            else {
                lastLoginDate.setText(_lastLoginDate);
                lastLogoutDate.setText("Még nem jelentkezett\n ki!");
            }

        } else if (!MyLog._myMessage.equals(SQLEnums.SQL_READING_FAILED)) {

            if (MyLog._myMessage.equals(SQLEnums.SQL_CONNECTION_FAILED)) {
                Toast.makeText(this, "Nem sikerült az adatbázishoz csatlakozni!", Toast.LENGTH_LONG).show();
            }

        } else {

            lastLoginDate.setText("Nincs bejelentkezési adat!");
        }

    }

}
