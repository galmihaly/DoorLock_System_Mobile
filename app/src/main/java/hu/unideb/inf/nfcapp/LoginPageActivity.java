package hu.unideb.inf.nfcapp;

import hu.unideb.inf.nfcapp.Databases.Repository;
import hu.unideb.inf.nfcapp.Enums.LoginStatesEnum;
import hu.unideb.inf.nfcapp.Enums.SQLEnums;
import hu.unideb.inf.nfcapp.Models.User;
import hu.unideb.inf.nfcapp.databinding.LoginPageBinding;
import hu.unideb.inf.nfcapp.databinding.StatisticsPageBinding;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginPageActivity extends AppCompatActivity {

    private EditText textPassword;
    private EditText textUsername;
    private Enum isLoginTypeEnum = null;

    private LoginPageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.login_page);

        binding = LoginPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        textUsername = findViewById(R.id.textUsername);
        textPassword = findViewById(R.id.textPassword);
    }

    @SuppressLint("ShowToast")
    public void loginUserClicked(View view) {

        Repository repository = new Repository(Repository.CommunicatorTypeEnum.MsSqlServer);
        if(repository.Communicator == null){
            Toast.makeText(LoginPageActivity.this, "Nem sikerült csatlakozni az adatbázishoz !!!", Toast.LENGTH_SHORT).show();
        }

       if(!(textPassword.getText().toString().isEmpty()) && !(textUsername.getText().toString().isEmpty())){
           isLoginTypeEnum = repository.Communicator.loginUser(textUsername.getText().toString(), textPassword.getText().toString());
           if(isLoginTypeEnum == LoginStatesEnum.LOGIN_ACCESS){

               Toast.makeText(LoginPageActivity.this, "Sikeres bejelentkezés !!!", Toast.LENGTH_SHORT).show();

               Intent intent = new Intent(this, StatisticsPageActivity.class);
               intent.putExtra("Username", User._name);
               intent.putExtra("Accountname", User._account);
               intent.putExtra("Address", User._address);
               startActivity(intent);
           }
           else if(isLoginTypeEnum == LoginStatesEnum.LOGIN_FAILED){
               Toast.makeText(LoginPageActivity.this, "Sikertelen bejelentkezés!", Toast.LENGTH_SHORT).show();
           }
           else if(isLoginTypeEnum == SQLEnums.SQL_READING_FAILED){
               Toast.makeText(LoginPageActivity.this, "Nem sikerült az adatbázisból lekérdezni adatot!", Toast.LENGTH_SHORT).show();
           }
           else if(isLoginTypeEnum == SQLEnums.SQL_CONNECTION_FAILED){
               Toast.makeText(LoginPageActivity.this, "Nem sikerült az adatbázishoz csatlakozni!", Toast.LENGTH_SHORT).show();
           }
       }
       else if(textUsername.getText().toString().isEmpty() && textPassword.getText().toString().isEmpty()){
           Toast.makeText(LoginPageActivity.this, "Felhasználónév és jelszó megadása kötelező!", Toast.LENGTH_SHORT).show();
       }
       else if(textUsername.getText().toString().isEmpty()){
           Toast.makeText(LoginPageActivity.this, "Felhasználónév megadása kötelező!", Toast.LENGTH_SHORT).show();
       }
       else if(textPassword.getText().toString().isEmpty()){
           Toast.makeText(LoginPageActivity.this, "Jelszó megadása kötelező!", Toast.LENGTH_SHORT).show();
       }

       textUsername.setText("");
       textPassword.setText("");
    }
}