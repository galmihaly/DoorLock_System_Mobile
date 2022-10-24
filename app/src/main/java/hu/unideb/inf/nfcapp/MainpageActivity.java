package hu.unideb.inf.nfcapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import hu.unideb.inf.nfcapp.Databases.Repository;
import hu.unideb.inf.nfcapp.Enums.SQLEnums;
import hu.unideb.inf.nfcapp.Models.MyLog;
import hu.unideb.inf.nfcapp.helpers.Helper;

import java.util.List;


public class MainpageActivity extends AppCompatActivity {

    private TextView loggedUsername = null;
    private TextView loggedAccountname = null;
    private TextView loggedUserAddress = null;
    private TextView lastPassedTime = null;

    private TextView numberOfLogin = null;
    private TextView numberOfPasswordLogin = null;
    private TextView numberOfNfcLogin = null;

    private TextView numberOfLogout = null;
    private TextView numberOfPasswordLogout = null;
    private TextView numberOfNfcLogout = null;

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
        lastPassedTime = findViewById(R.id.lastPassedTime);

        numberOfLogin = findViewById(R.id.numberofLogin);
        numberOfPasswordLogin = findViewById(R.id.numberofPasswordLogin);
        numberOfNfcLogin = findViewById(R.id.numberofNfcLogin);

        numberOfLogout = findViewById(R.id.numberofLogout);
        numberOfPasswordLogout = findViewById(R.id.numberofPasswordLogout);
        numberOfNfcLogout = findViewById(R.id.numberofNfcLogout);

        Intent intent = getIntent();
        loggedUsername.setText(intent.getStringExtra("Username"));
        loggedAccountname.setText(intent.getStringExtra("Accountname"));
        loggedUserAddress.setText(intent.getStringExtra("Address"));

        getLastLogDate();
        getLastPassedTime();

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
        getLastPassedTime();
    }

    public void getLastLogDate(){

        Repository repository = new Repository(Repository.CommunicatorTypeEnum.MsSqlServer);

        String _lastLoginDate = repository.Communicator.getLastLoginDate();
        String _lastLogoutDate = repository.Communicator.getLastLogoutDate();

        if (_lastLoginDate != null && _lastLogoutDate != null) {
            if (Helper.compareLoginDates(_lastLoginDate, _lastLogoutDate)) {
                lastLoginDate.setText(String.format("%s.", _lastLoginDate));
                lastLogoutDate.setText(String.format("%s.", _lastLogoutDate));
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

    public void getLastPassedTime(){

        Repository repository = new Repository(Repository.CommunicatorTypeEnum.MsSqlServer);

        int _lastPassedTime = repository.Communicator.getLastPassedTime();

        int _numberOfLogin = repository.Communicator.getNumberOfLogin();
        int _numberOfPasswordLogin = repository.Communicator.getNumberOfPasswordLogin();
        int _numberOfNfcLogin = repository.Communicator.getNumberOfNfcLogin();

        int _numberOfLogout = repository.Communicator.getNumberOfLogout();
        int _numberOfPasswordLogout = repository.Communicator.getNumberOfPasswordLogout();
        int _numberOfNfcLogout = repository.Communicator.getNumberOfNfcLogout();

        if (_lastPassedTime != 0 || _numberOfLogin != 0 ||
            _numberOfPasswordLogin != 0 || _numberOfNfcLogin != 0 ||
            _numberOfPasswordLogout != 0 || _numberOfNfcLogout != 0) {
            lastPassedTime.setText(Helper.parseTimeToHoursAndDay(_lastPassedTime));

            numberOfLogin.setText(String.valueOf(_numberOfLogin));
            numberOfPasswordLogin.setText(String.valueOf(_numberOfPasswordLogin));
            numberOfNfcLogin.setText(String.valueOf(_numberOfNfcLogin));

            numberOfLogout.setText(String.valueOf(_numberOfLogout));
            numberOfPasswordLogout.setText(String.valueOf(_numberOfPasswordLogout));
            numberOfNfcLogout.setText(String.valueOf(_numberOfNfcLogout));

        } else if (!MyLog._myMessage.equals(SQLEnums.SQL_READING_FAILED)) {

            if (MyLog._myMessage.equals(SQLEnums.SQL_CONNECTION_FAILED)) {
                Toast.makeText(this, "Nem sikerült az adatbázishoz csatlakozni!", Toast.LENGTH_LONG).show();
            }

        } else {

            lastLoginDate.setText("Nincs bejelentkezési adat!");
        }

    }

}
