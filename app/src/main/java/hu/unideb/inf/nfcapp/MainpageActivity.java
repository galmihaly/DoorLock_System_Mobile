package hu.unideb.inf.nfcapp;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainpageActivity extends AppCompatActivity {

    private TextView loggedUsername;
    private TextView loggedAccountname;
    private TextView loggedUserAddress;
    private TextView loggedCardId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        loggedAccountname = findViewById(R.id.loggedUsername);
        loggedAccountname = findViewById(R.id.loggedAccountname);
        loggedUserAddress = findViewById(R.id.loggedUserAddress);
        loggedCardId = findViewById(R.id.loggedCardId);


    }
}
