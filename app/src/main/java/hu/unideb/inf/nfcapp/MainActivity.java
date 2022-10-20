package hu.unideb.inf.nfcapp;

import hu.unideb.inf.nfcapp.Databases.Repository;
import hu.unideb.inf.nfcapp.Enums.LoginTypeEnum;
import hu.unideb.inf.nfcapp.Enums.SQLEnums;
import hu.unideb.inf.nfcapp.Models.User;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText textPassword;
    private EditText textUsername;
    private Button loginButton;
    private Enum isLogin = null;

    String firstname;
    String[] seged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginButton = (Button) findViewById(R.id.loginButton);

        // inputTexts
        textUsername = (EditText) findViewById(R.id.textUsername);
        textPassword = (EditText) findViewById(R.id.textPassword);



    }

    @SuppressLint("ShowToast")
    public void loginUserClicked(View view) {

        Repository repository = new Repository(Repository.CommunicatorTypeEnum.MsSqlServer);
        if(repository.Communicator == null){
            Toast.makeText(MainActivity.this, "Nem sikerült csatlakozni az adatbázishoz !!!", Toast.LENGTH_SHORT).show();
        }

       if(!(textPassword.getText().toString().isEmpty()) && !(textUsername.getText().toString().isEmpty())){
           isLogin = repository.Communicator.loginUser(textUsername.getText().toString(), textPassword.getText().toString());
           if(isLogin == LoginTypeEnum.LOGIN_ACCESS){

               Toast.makeText(MainActivity.this, "Sikeres bejelentkezés !!!", Toast.LENGTH_SHORT).show();

               Intent intent = new Intent(this, MainpageActivity.class);
               intent.putExtra("Username", User._name);
               intent.putExtra("Accountname", User._account);
               intent.putExtra("Address", User._address);
               startActivity(intent);

           }
           else if(isLogin == LoginTypeEnum.LOGIN_FAILED){
               Toast.makeText(MainActivity.this, "Sikertelen bejelentkezés !!!", Toast.LENGTH_SHORT).show();
           }
           else if(isLogin == SQLEnums.SQL_READING_FAILED){
               Toast.makeText(MainActivity.this, "Nem sikerült az adatbázisból lekérdezni rekordot !!!", Toast.LENGTH_SHORT).show();
           }
           else if(isLogin == SQLEnums.SQL_CONNECTION_FAILED){
               Toast.makeText(MainActivity.this, "Nem sikerült az adatbázishoz csatlakozni !!!", Toast.LENGTH_SHORT).show();
           }

       }
       else if(textUsername.getText().toString().isEmpty() && textPassword.getText().toString().isEmpty()){
           Toast.makeText(MainActivity.this, "Felhasználónév és jelszó megadása kötelező !!!", Toast.LENGTH_SHORT).show();
       }
       else if(textUsername.getText().toString().isEmpty()){
           Toast.makeText(MainActivity.this, "Felhasználónév megadása kötelező !!!", Toast.LENGTH_SHORT).show();
       }
       else if(textPassword.getText().toString().isEmpty()){
           Toast.makeText(MainActivity.this, "Jelszó megadása kötelező !!!", Toast.LENGTH_SHORT).show();
       }

       textUsername.setText("");
       textPassword.setText("");

    }
}