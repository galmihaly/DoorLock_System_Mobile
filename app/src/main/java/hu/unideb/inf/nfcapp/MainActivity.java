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
    private TextView userName;
    private Button loginButton;
    private LoginTypeEnum isLogin = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginButton = findViewById(R.id.loginButton);

        // inputTexts
        textUsername = (EditText) findViewById(R.id.textUsername);
        textPassword = (EditText) findViewById(R.id.textPassword);

        userName = (TextView) findViewById(R.id.userName);

    }

    @SuppressLint("ShowToast")
    public void loginUser(View view) {

        Repository repository = new Repository(Repository.CommunicatorTypeEnum.MsSqlServer);
        if(repository.Communicator == null){
            Toast.makeText(MainActivity.this, "Nem sikerült csatlakozni az adatbázishoz !!!", Toast.LENGTH_SHORT).show();
        }

       if(!(textPassword.getText().toString().isEmpty()) && !(textUsername.getText().toString().isEmpty())){
           isLogin = repository.Communicator.loginUser(textUsername.getText().toString(), textPassword.getText().toString());

           if(isLogin == LoginTypeEnum.LOGIN_ACCESS){
               userName.setText(repository.LoggedInUser.name);
               Toast.makeText(MainActivity.this, "Sikeres bejelentkezés !!!", Toast.LENGTH_SHORT).show();
           }
           else if(isLogin == LoginTypeEnum.LOGIN_FAILED){
               Toast.makeText(MainActivity.this, "Sikertelen bejelentkezés !!!", Toast.LENGTH_SHORT).show();
           }
           else if(isLogin == LoginTypeEnum.SQL_READING_FAILED){
               Toast.makeText(MainActivity.this, "Nem sikerült az adatbázisból lekérdezni rekordot !!!", Toast.LENGTH_SHORT).show();
           }
           else if(isLogin == LoginTypeEnum.SQL_CONNECTION_FAILED){
               Toast.makeText(MainActivity.this, "Nem sikerült az adatbázishoz csatlakozni !!!", Toast.LENGTH_SHORT).show();
           }

       }
       else if(textUsername.getText().toString().isEmpty()){
           Toast.makeText(MainActivity.this, "Felhasználónév megadása kötelező !!!", Toast.LENGTH_SHORT).show();
       }
       else if(textPassword.getText().toString().isEmpty()){
           Toast.makeText(MainActivity.this, "Jelszó megadása kötelező !!!", Toast.LENGTH_SHORT).show();
       }
       else if(textUsername.getText().toString().isEmpty() || textPassword.getText().toString().isEmpty()){
           Toast.makeText(MainActivity.this, "Felhasználónév és jelszó megadása kötelező !!!", Toast.LENGTH_SHORT).show();
       }

    }
}