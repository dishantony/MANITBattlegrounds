package com.example.manitbattlegrounds;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginOrSignupActivity extends AppCompatActivity implements View.OnClickListener,View.OnKeyListener{

    TextView changeMode;

    Boolean signUpModeActive = false;

    EditText passwordEditText;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void showParticipantList(ParseUser user){
        Log.i("Username",user.getUsername());
        Intent intent;

        if(signUpModeActive){
            intent = new Intent(LoginOrSignupActivity.this,PersonalDetailsActivity.class);
        }else {

            if (user.getUsername().matches("181112226") || user.getUsername().matches("181116227")) {
                intent = new Intent(LoginOrSignupActivity.this, CoreActivity.class);

            } else {
                Log.i("else", "working");
                intent = new Intent(LoginOrSignupActivity.this, HomeActivity.class);

            }
        }
        startActivity(intent);
    }

    public void signUp(View view){

        EditText usernameEditText = (EditText)findViewById(R.id.usernameEditText);

        if(usernameEditText.getText().toString().matches("") || passwordEditText.getText().toString().matches("")){
            Toast.makeText(this, "Username or password is empty", Toast.LENGTH_SHORT).show();
        }
        else {
            if(signUpModeActive) {
                final ParseUser user = new ParseUser();
                user.setUsername(usernameEditText.getText().toString());
                user.setPassword(passwordEditText.getText().toString());
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.i("Sign up", "Successful");
                            showParticipantList(user);
                        } else {
                            Toast.makeText(LoginOrSignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            else {
                ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        if(parseUser != null){
                            Log.i("Login","Successful");

                            showParticipantList(parseUser);

                        }
                        else {
                            Toast.makeText(LoginOrSignupActivity.this,"Invalid username/password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_sign_up);

        ConnectivityManager ConnectionManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=ConnectionManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()==true )
        {
            changeMode = (TextView)findViewById(R.id.textView);

            changeMode.setOnClickListener(this);

            passwordEditText = (EditText)findViewById(R.id.passwordEditText);

            passwordEditText.setOnKeyListener(this);

            ConstraintLayout backgroundConstraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayout);
            ImageView logoImageView = (ImageView)findViewById(R.id.logoImageView);

            backgroundConstraintLayout.setOnClickListener(this);

            logoImageView.setOnClickListener(this);

            if(ParseUser.getCurrentUser() != null){
                showParticipantList(ParseUser.getCurrentUser());
            }

            ParseAnalytics.trackAppOpenedInBackground(getIntent());


        }
        else
        {
            Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();

        }

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.textView){
            Button signUpbutton = (Button)findViewById(R.id.signUpButton);
            if(signUpModeActive){

                signUpbutton.setText("Login");
                changeMode.setText("or Sign up");
                signUpModeActive = false;

            }
            else {
                signUpbutton.setText("Sign up");
                changeMode.setText("or Login");
                signUpModeActive = true;
            }

        }else if(v.getId() == R.id.constraintLayout || v.getId() == R.id.logoImageView){
            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if(keyCode == event.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
            signUp(v);
        }

        return false;
    }
}
