package hu.unideb.inf.nfcapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText textPassword;
    private EditText textUsername;
    private TextView userName;
    private Button loginButton;
    private LoginTypeEnum isLogin = null;

    String firstname;
    String[] seged;

    ActivityResultLauncher addActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent newIntent = new Intent(MainActivity.this, MainpageActivity.class);
                        newIntent.putExtra("Username", User._name);
                        newIntent.putExtra("Address", User._address);
                        newIntent.putExtra("Accountname", User._name);
                        setResult(RESULT_OK, newIntent);
                    }
                }
            });

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
    public void loginUserClicked(View view) {

        Repository repository = new Repository(Repository.CommunicatorTypeEnum.MsSqlServer);
        if(repository.Communicator == null){
            Toast.makeText(MainActivity.this, "Nem sikerült csatlakozni az adatbázishoz !!!", Toast.LENGTH_SHORT).show();
        }

       if(!(textPassword.getText().toString().isEmpty()) && !(textUsername.getText().toString().isEmpty())){
           isLogin = repository.Communicator.loginUser(textUsername.getText().toString(), textPassword.getText().toString());
           if(isLogin == LoginTypeEnum.LOGIN_ACCESS){

               Toast.makeText(MainActivity.this, "Sikeres bejelentkezés !!!", Toast.LENGTH_SHORT).show();

               seged = User._name.split(" ");
               firstname = seged[1];

               Intent intent = new Intent(this, MainpageActivity.class);
               intent.putExtra("Username", firstname);
               intent.putExtra("Accountname", User._account);
               intent.putExtra("Address", User._address);
               startActivity(intent);

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
       else if(textUsername.getText().toString().isEmpty() && textPassword.getText().toString().isEmpty()){
           Toast.makeText(MainActivity.this, "Felhasználónév és jelszó megadása kötelező !!!", Toast.LENGTH_SHORT).show();
       }
       else if(textUsername.getText().toString().isEmpty()){
           Toast.makeText(MainActivity.this, "Felhasználónév megadása kötelező !!!", Toast.LENGTH_SHORT).show();
       }
       else if(textPassword.getText().toString().isEmpty()){
           Toast.makeText(MainActivity.this, "Jelszó megadása kötelező !!!", Toast.LENGTH_SHORT).show();
       }

    }
}