package hu.unideb.inf.nfcapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class MainpageActivity extends AppCompatActivity {

    private TextView loggedUsername = null;
    private TextView loggedAccountname = null;
    private TextView loggedUserAddress = null;
    private TextView loggedCardId = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        loggedUsername = findViewById(R.id.loggedUsername);
        loggedAccountname = findViewById(R.id.loggedAccountname);
        loggedUserAddress = findViewById(R.id.loggedUserAddress);
        loggedCardId = findViewById(R.id.loggedCardId);

        Intent intent = getIntent();

        loggedUsername.setText(intent.getStringExtra("Username"));
        loggedAccountname.setText(intent.getStringExtra("Accountname"));
        loggedUserAddress.setText(intent.getStringExtra("Address"));

    }

    public void logOutButtonClicked(View view) {

        loggedUsername.setText(null);
        loggedAccountname.setText(null);
        loggedUserAddress.setText(null);

        finish();
    }
}
