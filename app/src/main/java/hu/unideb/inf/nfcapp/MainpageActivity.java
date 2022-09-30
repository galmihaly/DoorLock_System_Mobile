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

    private TextView loggedUsername;
    private TextView loggedAccountname;
    private TextView loggedUserAddress;
    private TextView loggedCardId;

    ActivityResultLauncher addActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {

                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        loggedAccountname = findViewById(R.id.loggedUsername);
        loggedAccountname = findViewById(R.id.loggedAccountname);
        loggedUserAddress = findViewById(R.id.loggedUserAddress);
        loggedCardId = findViewById(R.id.loggedCardId);

        Intent replyIntent = new Intent();
        setResult(RESULT_OK, replyIntent);

        addActivityResultLauncher.launch(replyIntent);

        /*loggedUsername.setText(User._name);
        loggedUserAddress.setText(User._address);
        loggedAccountname.setText(User._account);*/
    }

    public void logOutButtonClicked(View view) {
        finish();
    }
}
