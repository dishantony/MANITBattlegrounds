package com.example.manitbattlegrounds;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class CoreActivity extends AppCompatActivity {

    EditText textToBeUpdated;
    EditText participantToBeAdded;

    EditText walletUpdateUsername;
    EditText updatedAmount;

    public static String text = "abc";
    public static String participantSchNo = "";


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void updateText(View view){

        text = textToBeUpdated.getText().toString();
        Log.i("text",text);
        ParseObject updatedCondition = new ParseObject("UpdatedCondition");
        updatedCondition.put("username", ParseUser.getCurrentUser().getUsername());
        updatedCondition.put("condition",text);

        updatedCondition.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    Log.i("condition","saved successfully");
                }
            }
        });
    }

    public void addParticipant(View view){

        participantSchNo = participantToBeAdded.getText().toString();
        Log.i("Participant Sch no.",participantSchNo);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("PersonalDetails");
        query.whereEqualTo("username",participantSchNo);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if(e == null){
                    Log.i("error","no");
                    if(parseObject == null){
                        Log.i("object","no");

                    }else {
                        ParseObject participantList = new ParseObject("ParticipantList");
                        participantList.put("username",parseObject.getString("username"));
                        participantList.put("name",parseObject.getString("name"));
                        participantList.put("branch",parseObject.getString("branch"));
                        participantList.put("year",parseObject.getString("year"));

                        participantList.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(e == null){
                                    Log.i("List","updated successfully");
                                    Toast.makeText(CoreActivity.this, "Participant added successfully!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }else {
                    Log.i("Error",e.getMessage());
                    Toast.makeText(CoreActivity.this, "Sorry! This user does not exits on our server", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    public void goToHomePage(View view){

        Intent intent = new Intent(CoreActivity.this,HomeActivity.class);
        startActivity(intent);
    }

    public void checkRequestsForParticipation(View view){

        Intent intent = new Intent(CoreActivity.this,ParticipationRequestListActivity.class);
        startActivity(intent);
    }

    public void updateWallet(View view){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("PersonalDetails");
        query.whereEqualTo("username",walletUpdateUsername.getText().toString());
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if(e == null){
                    if(parseObject != null){
                        parseObject.put("walletamount",Integer.valueOf(updatedAmount.getText().toString()));
                        parseObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                Log.i("saved","successfully");
                            }
                        });
                    }
                }else{
                    Toast.makeText(CoreActivity.this, "Sorry! This user does not exits on our server", Toast.LENGTH_LONG).show();
                }
            }
        });

        Toast.makeText(this, "wallet updated", Toast.LENGTH_SHORT).show();
    }

    public void openMoneyRequestList(View view){
        Intent intent = new Intent(CoreActivity.this,MoneyRequestListActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_core);

        textToBeUpdated = (EditText)findViewById(R.id.textToBeUpdated);
        participantToBeAdded = (EditText)findViewById(R.id.participantToBeAdded);
        walletUpdateUsername = (EditText)findViewById(R.id.walletUpdateUsername);
        updatedAmount = (EditText)findViewById(R.id.updatedAmount);

    }
}
