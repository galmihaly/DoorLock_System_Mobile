package hu.unideb.inf.nfcapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import hu.unideb.inf.nfcapp.Databases.Repository;
import hu.unideb.inf.nfcapp.Enums.LogTypeEnums;
import hu.unideb.inf.nfcapp.Enums.SQLEnums;
import hu.unideb.inf.nfcapp.Models.MyLog;
import hu.unideb.inf.nfcapp.Models.User;
import hu.unideb.inf.nfcapp.helpers.Helper;

import java.util.List;


public class StatisticsPageActivity extends AppCompatActivity {

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

    private TextView loggedGateIDs = null;

    private TextView lastLoginDate;
    private TextView lastLogoutDate;

    private final StringBuilder sb = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics_page);

        loggedUsername = findViewById(R.id.loggedUsername);
        loggedAccountname = findViewById(R.id.loggedAccountname);
        loggedUserAddress = findViewById(R.id.loggedUserAddress);
        loggedGateIDs = findViewById(R.id.loggedGateIDs);
        lastLoginDate = findViewById(R.id.lastLoginDate);
        lastLogoutDate = findViewById(R.id.lastLogoutDate);
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
        getStatistics();
        getGatePermissions();
    }

    public void getLastLogDate(){

        Repository repository = new Repository(Repository.CommunicatorTypeEnum.MsSqlServer);
        String _lastLoginDate = repository.Communicator.getLastLoginDate();
        String _lastLogoutDate = repository.Communicator.getLastLogoutDate();

        if (MyLog._myMessage.equals(SQLEnums.SQL_READING_SUCCES)) {
            if (_lastLoginDate != null && _lastLogoutDate != null) {
                if (Helper.compareLoginDates(_lastLoginDate, _lastLogoutDate)) {
                    lastLoginDate.setText(String.format("%s.", _lastLoginDate));
                    lastLogoutDate.setText(String.format("%s.", _lastLogoutDate));
                }
                else {
                    lastLoginDate.setText(_lastLoginDate);
                    lastLogoutDate.setText("Még nem jelentkezett\nki!");
                }
            }
        }
        else if (MyLog._myMessage.equals(SQLEnums.SQL_READING_FAILED)) {
            Toast.makeText(this, "Nem sikerült az adatbázisból olvasni adatokat!", Toast.LENGTH_LONG).show();
        }
        else if(MyLog._myMessage.equals(SQLEnums.SQL_CONNECTION_FAILED)) {
            Toast.makeText(this, "Nem sikerült az adatbázishoz csatlakozni!", Toast.LENGTH_LONG).show();
        }
        else if(MyLog._myMessage.equals(SQLEnums.SQL_NO_EVENTS)) {
            lastLoginDate.setText(R.string.No_LogDate);
        }
    }

    public void getStatistics(){

        Repository repository = new Repository(Repository.CommunicatorTypeEnum.MsSqlServer);
        sb.setLength(0); // StringBuilder kiürítése

        String command = sb.append("select CONVERT(int, DATEDIFF(MINUTE, EntryDate, GETDATE())) from Log")
                .append(" where Id = (select MAX(id) From Log where UserId = ").append(User._id)
                .append(" and (LogTypeId = ").append(LogTypeEnums.LOGOUT_CARD.getLevelCode())
                .append(" or LogTypeId = ").append(LogTypeEnums.LOGOUT_PASSWORD.getLevelCode()).append("))")
                .toString();

        int _lastPassedTime = repository.Communicator.getStatistic(command);
        sb.setLength(0);

        command = sb.append("select count(*) from Log where UserId = ").append(User._id)
                    .append(" and (LogTypeId = ").append(LogTypeEnums.LOGIN_CARD.getLevelCode())
                    .append(" or LogTypeId = ").append(LogTypeEnums.LOGIN_PASSWORD.getLevelCode()).append(")")
                    .toString();

        int _numberOfLogin = repository.Communicator.getStatistic(command);
        sb.setLength(0);

        command = sb.append("select count(*) from Log where UserId = ").append(User._id)
                    .append(" and (LogTypeId = ").append(LogTypeEnums.LOGOUT_CARD.getLevelCode())
                    .append(" or LogTypeId = ").append(LogTypeEnums.LOGOUT_PASSWORD.getLevelCode()).append(")")
                    .toString();

        int _numberOfLogout = repository.Communicator.getStatistic(command);
        sb.setLength(0);

        command = sb.append("select count(*) from Log where UserId = ").append(User._id)
                    .append(" and LogTypeId = ").append(LogTypeEnums.LOGIN_PASSWORD.getLevelCode())
                    .toString();

        int _numberOfPasswordLogin = repository.Communicator.getStatistic(command);
        sb.setLength(0);

        command = sb.append("select count(*) from Log where UserId = ").append(User._id)
                    .append(" and LogTypeId = ").append(LogTypeEnums.LOGIN_CARD.getLevelCode())
                    .toString();

        int _numberOfNfcLogin = repository.Communicator.getStatistic(command);
        sb.setLength(0);

        command = sb.append("select count(*) from Log where UserId = ").append(User._id)
                    .append(" and LogTypeId = ").append(LogTypeEnums.LOGOUT_PASSWORD.getLevelCode())
                    .toString();

        int _numberOfPasswordLogout = repository.Communicator.getStatistic(command);
        sb.setLength(0);

        command = sb.append("select count(*) from Log where UserId = ").append(User._id)
                    .append(" and LogTypeId = ").append(LogTypeEnums.LOGOUT_CARD.getLevelCode())
                    .toString();

        int _numberOfNfcLogout = repository.Communicator.getStatistic(command);
        sb.setLength(0);

        if (MyLog._myMessage.equals(SQLEnums.SQL_READING_SUCCES)) {

            if (    _lastPassedTime != 0 || _numberOfLogin != 0 ||
                    _numberOfPasswordLogin != 0 || _numberOfNfcLogin != 0 ||
                    _numberOfPasswordLogout != 0 || _numberOfNfcLogout != 0    ) {

                lastPassedTime.setText(Helper.parseTime(_lastPassedTime));

                numberOfLogin.setText(String.valueOf(_numberOfLogin));
                numberOfPasswordLogin.setText(String.valueOf(_numberOfPasswordLogin));
                numberOfNfcLogin.setText(String.valueOf(_numberOfNfcLogin));

                numberOfLogout.setText(String.valueOf(_numberOfLogout));
                numberOfPasswordLogout.setText(String.valueOf(_numberOfPasswordLogout));
                numberOfNfcLogout.setText(String.valueOf(_numberOfNfcLogout));
            }

        }
        else if (MyLog._myMessage.equals(SQLEnums.SQL_READING_FAILED)) {
            Toast.makeText(this, "Nem sikerült az adatbázisból olvasni!", Toast.LENGTH_LONG).show();
        }
        else if (MyLog._myMessage.equals(SQLEnums.SQL_CONNECTION_FAILED)) {
            Toast.makeText(this, "Nem sikerült az adatbázishoz csatlakozni!", Toast.LENGTH_LONG).show();
        }
        else if (MyLog._myMessage.equals(SQLEnums.SQL_NO_EVENTS)) {

            lastPassedTime.setText(R.string.No_PassedTime);

            numberOfLogin.setText("0");
            numberOfPasswordLogin.setText("0");
            numberOfNfcLogin.setText("0");

            numberOfLogout.setText("0");
            numberOfPasswordLogout.setText("0");
            numberOfNfcLogout.setText("0");
        }
    }

    public void getGatePermissions(){

        Repository repository = new Repository(Repository.CommunicatorTypeEnum.MsSqlServer);
        List<Integer> gatePermissions = repository.Communicator.getGatePermissionList();

        if(MyLog._myMessage.equals(SQLEnums.SQL_READING_SUCCES)){
            if(gatePermissions.size() != 0){

                if(gatePermissions.size() == 1){
                    loggedGateIDs.setText(String.valueOf(gatePermissions.get(0)));
                }
                else {
                    for(int i = 0; i < gatePermissions.size(); i++)
                        sb.append(gatePermissions.get(i)).append(", ");

                    loggedGateIDs.setText(sb.toString());
                    sb.setLength(0);
                }
            }
        }
        else if (MyLog._myMessage.equals(SQLEnums.SQL_READING_FAILED)) {
            Toast.makeText(this, "Nem sikerült az adatbázisból olvasni adatokat!", Toast.LENGTH_LONG).show();
        }
        else if(MyLog._myMessage.equals(SQLEnums.SQL_CONNECTION_FAILED)) {
            Toast.makeText(this, "Nem sikerült az adatbázishoz csatlakozni!", Toast.LENGTH_LONG).show();
        }
        else if(MyLog._myMessage.equals(SQLEnums.SQL_NO_EVENTS)) {
            loggedGateIDs.setText(R.string.No_GatePermissions);
        }
    }

    public void logOutButtonClicked(View view) {
        loggedUsername.setText(null);
        loggedAccountname.setText(null);
        loggedUserAddress.setText(null);
        finish();
    }

    public void createCalendarClicked(View view) {
        Intent calendarIntent = new Intent(this, CalendarPageActivity.class);
        startActivity(calendarIntent);
    }

    public void refreshStatisticsClicked(View view){
        Toast.makeText(this, "Frissítés...", Toast.LENGTH_LONG).show();
        getLastLogDate();
        getStatistics();
    }
}
