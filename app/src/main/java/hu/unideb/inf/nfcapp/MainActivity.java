package hu.unideb.inf.nfcapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;

public class MainActivity extends AppCompatActivity {

    private EditText textPassword;
    private EditText textUsername;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginButton = findViewById(R.id.loginButton);

        // inputTexts
        textUsername = (EditText) findViewById(R.id.textUsername);
        textPassword = (EditText) findViewById(R.id.textPassword);

    }

    @SuppressLint("ShowToast")
    public void loginUser(View view) {

        Repository.LoggedInUser = Repository.Communicator.loginUser();

        if(textUsername.getText().toString().isEmpty()){
            Toast.makeText(MainActivity.this, "Felhasználónév megadása kötelező !!!", Toast.LENGTH_SHORT).show();
        }
        else if(textPassword.getText().toString().isEmpty()){
            Toast.makeText(MainActivity.this, "Jelszó megadása kötelező !!!", Toast.LENGTH_SHORT).show();
        }
        else if(textUsername.getText().toString().isEmpty() || textPassword.getText().toString().isEmpty()){
            Toast.makeText(MainActivity.this, "Felhasználónév és jelszó megadása kötelező !!!", Toast.LENGTH_SHORT).show();
        }
        else{
            if(textUsername.getText().toString().equals("a") || textPassword.getText().toString().equals("a")){
                Toast.makeText(MainActivity.this, "Sikeres bejelentkezés !!!", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(MainActivity.this, "Sikertelen bejelentkezés !!!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}