package hu.unideb.inf.nfcapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import hu.unideb.inf.nfcapp.Databases.Repository;
import hu.unideb.inf.nfcapp.Enums.LogTypeEnums;
import hu.unideb.inf.nfcapp.Enums.SQLEnums;
import hu.unideb.inf.nfcapp.Models.MyLog;
import hu.unideb.inf.nfcapp.Models.User;
import hu.unideb.inf.nfcapp.databinding.StatisticsPageBinding;
import hu.unideb.inf.nfcapp.helpers.Helper;


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

    private StatisticsPageBinding binding = null;

    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private Handler handler = new Handler(Looper.getMainLooper());

    private final StringBuilder sb = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.statistics_page);

        binding = StatisticsPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        binding.loggedUsername.setText(intent.getStringExtra("Username"));
        binding.loggedAccountname.setText(intent.getStringExtra("Accountname"));
        binding.loggedUserAddress.setText(intent.getStringExtra("Address"));

        getLastLogDate();
        getStatistics();
        getGatePermissions();
    }

    public void getLastLogDate(){

        executor.execute(() -> {

            Repository repository = new Repository(Repository.CommunicatorTypeEnum.MsSqlServer);
            String _lastLoginDate = repository.Communicator.getLastLoginDate();
            String _lastLogoutDate = repository.Communicator.getLastLogoutDate();

            final String final_lastLoginDate = _lastLoginDate;
            final String final_lastLogoutDate = _lastLogoutDate;

            handler.post(() -> {
                if (MyLog._myMessage.equals(SQLEnums.SQL_READING_SUCCES)) {
                    if (_lastLoginDate != null && _lastLogoutDate != null) {
                        if (Helper.compareLoginDates(_lastLoginDate, _lastLogoutDate)) {
                            binding.lastLoginDate.setText(final_lastLoginDate);
                            binding.lastLogoutDate.setText(final_lastLogoutDate);
                        }
                        else {

                            binding.lastLoginDate.setText(final_lastLoginDate);
                            binding.lastLogoutDate.setText("Még nem jelentkezett\nki!");
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
                    binding.lastLoginDate.setText(R.string.No_LogDate);
                    binding.lastLogoutDate.setText(R.string.No_LogDate);
                }
            });
        });
    }

    public void getStatistics(){

        executor.execute(() -> {

            Repository repository = new Repository(Repository.CommunicatorTypeEnum.MsSqlServer);
            sb.setLength(0); // StringBuilder kiürítése

            String command = sb.append("select CONVERT(int, DATEDIFF(MINUTE, EntryDate, GETDATE())) from Log")
                    .append(" where Id = (select MAX(id) From Log where UserId = ").append(User._id)
                    .append(" and (LogTypeId = ").append(LogTypeEnums.LOGOUT_CARD.getLevelCode())
                    .append(" or LogTypeId = ").append(LogTypeEnums.LOGOUT_PASSWORD.getLevelCode()).append("))")
                    .toString();

            String _lastPassedTime = Helper.parseTime(repository.Communicator.getStatistic(command));
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

            final String final_lastPassedTime = _lastPassedTime;
            final int final_numberOfLogin = _numberOfLogin;
            final int final_numberOfPasswordLogin = _numberOfPasswordLogin;
            final int final_numberOfNfcLogin = _numberOfNfcLogin;
            final int final_numberOfLogout = _numberOfLogout;
            final int final_numberOfPasswordLogout = _numberOfPasswordLogout;
            final int final_numberOfNfcLogout = _numberOfNfcLogout;

            handler.post(() -> {
                if (MyLog._myMessage.equals(SQLEnums.SQL_READING_SUCCES)) {

                    binding.lastPassedTime.setText(final_lastPassedTime);

                    if (   _numberOfLogin != 0 || _numberOfPasswordLogin != 0 || _numberOfNfcLogin != 0 ||
                            _numberOfPasswordLogout != 0 || _numberOfNfcLogout != 0    ) {

                        binding.numberofLogin.setText(String.valueOf(final_numberOfLogin));
                        binding.numberofPasswordLogin.setText(String.valueOf(final_numberOfPasswordLogin));
                        binding.numberofNfcLogin.setText(String.valueOf(final_numberOfNfcLogin));

                        binding.numberofLogout.setText(String.valueOf(final_numberOfLogout));
                        binding.numberofPasswordLogout.setText(String.valueOf(final_numberOfPasswordLogout));
                        binding.numberofNfcLogout.setText(String.valueOf(final_numberOfNfcLogout));
                    }
                }
                else if (MyLog._myMessage.equals(SQLEnums.SQL_READING_FAILED)) {
                    Toast.makeText(this, "Nem sikerült az adatbázisból olvasni!", Toast.LENGTH_LONG).show();
                }
                else if (MyLog._myMessage.equals(SQLEnums.SQL_CONNECTION_FAILED)) {
                    Toast.makeText(this, "Nem sikerült az adatbázishoz csatlakozni!", Toast.LENGTH_LONG).show();
                }
                else if (MyLog._myMessage.equals(SQLEnums.SQL_NO_EVENTS)) {

                    binding.lastPassedTime.setText("0");

                    binding.numberofLogin.setText("0");
                    binding.numberofPasswordLogin.setText("0");
                    binding.numberofNfcLogin.setText("0");

                    binding.numberofLogout.setText("0");
                    binding.numberofPasswordLogout.setText("0");
                    binding.numberofNfcLogout.setText("0");
                }
            });
        });
    }

    public void getGatePermissions(){

        executor.execute(() -> {

            Repository repository = new Repository(Repository.CommunicatorTypeEnum.MsSqlServer);
            final List<Integer> gatePermissions = repository.Communicator.getGatePermissionList();

            handler.post(() -> {

                if(MyLog._myMessage.equals(SQLEnums.SQL_READING_SUCCES)){
                    if(gatePermissions.size() != 0){

                        if(gatePermissions.size() == 1){
                            binding.loggedGateIDs.setText(String.valueOf(gatePermissions.get(0)));
                        }
                        else {
                            for(int i = 0; i < gatePermissions.size(); i++)
                                sb.append(gatePermissions.get(i)).append(", ");

                            binding.loggedGateIDs.setText(sb.toString());
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
                    binding.loggedGateIDs.setText(R.string.No_GatePermissions);
                }

            });
        });

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
        Toast.makeText(this, R.string.refresh, Toast.LENGTH_LONG).show();
        getLastLogDate();
        getStatistics();
        getGatePermissions();
    }
}
