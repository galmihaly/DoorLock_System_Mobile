package hu.unideb.inf.nfcapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainpageActivity extends AppCompatActivity {

    private TextView loggedUsername = null;
    private TextView loggedAccountname = null;
    private TextView loggedUserAddress = null;
    private TextView loggedCardId = null;
    private ImageButton calendarButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        loggedUsername = findViewById(R.id.loggedUsername);
        loggedAccountname = findViewById(R.id.loggedAccountname);
        loggedUserAddress = findViewById(R.id.loggedUserAddress);
        loggedCardId = findViewById(R.id.loggedCardId);
        calendarButton = findViewById(R.id.calendarButton);

        Intent intent = getIntent();

        loggedUsername.setText(intent.getStringExtra("Username"));
        loggedAccountname.setText(intent.getStringExtra("Accountname"));
        loggedUserAddress.setText(intent.getStringExtra("Address"));

    }

    public void logOutButtonClicked(View view) {

        loggedUsername.setText(null);
        loggedAccountname.setText(null);
        loggedUserAddress.setText(null);

        this.finish();
    }

    public void createCalendarClicked(View view) {

        Intent calendarIntent = new Intent(this, CalendarpageActivity.class);
        startActivity(calendarIntent);
    }
}
