package com.example.manitbattlegrounds;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class WalletActivity extends AppCompatActivity {

    TextView amountTextView;

    EditText amountToBeRedeemed;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void redeem(View view){

        if(amountTextView.getText().toString().contains(".")){
            Toast.makeText(this, "Please type an integer value!", Toast.LENGTH_SHORT).show();
        }
        else if(Integer.valueOf(amountTextView.getText().toString()) <= 20){
            Toast.makeText(this, "You cannot redeem now as your account balance is less than 20 rupees", Toast.LENGTH_LONG).show();
        }else{
            if(Integer.valueOf(amountToBeRedeemed.getText().toString()) > Integer.valueOf(amountTextView.getText().toString())){
                Toast.makeText(this, "Sorry! you don't have enough balance", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Money will be received in your respected account within 24 hours", Toast.LENGTH_LONG).show();

                ParseQuery<ParseObject> query = ParseQuery.getQuery("PersonalDetails");
                query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject parseObject, ParseException e) {
                        parseObject.put("walletamount",parseObject.getInt("walletamount") - Integer.valueOf(amountToBeRedeemed.getText().toString()));
                        parseObject.saveInBackground(
                                new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(e == null){
                                    Log.i("saved","successfully");
                                }
                            }
                        });
                    }
                });

                ParseQuery<ParseObject> query1 = ParseQuery.getQuery("PersonalDetails");
                query1.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
                query1.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject parseObject, ParseException e) {
                        if(e == null){
                            if(parseObject != null){

                                ParseObject moneyRequestList = new ParseObject("MoneyRequestList");
                                moneyRequestList.put("username",parseObject.getString("username"));
                                moneyRequestList.put("name",parseObject.getString("name"));
                                moneyRequestList.put("pubgusername",parseObject.getString("pubgusername"));
                                moneyRequestList.put("branch",parseObject.getString("branch"));
                                moneyRequestList.put("year",parseObject.getString("year"));
                                moneyRequestList.put("mobilenumber",parseObject.getString("mobilenumber"));
                                moneyRequestList.put("email",parseObject.getString("email"));
                                moneyRequestList.put("moneyrequested",amountToBeRedeemed.getText().toString());

                                moneyRequestList.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if(e == null){
                                            Log.i("money request list","updated successfully");
                                        }
                                    }
                                });
                            }
                        }else {
                            Log.i("Error",e.getMessage());
                        }
                    }
                });



            }
        }
        Intent intent = new Intent(WalletActivity.this,HomeActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        amountTextView = (TextView)findViewById(R.id.amountTextView);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("PersonalDetails");
        query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                amountTextView.setText(String.valueOf(parseObject.getInt("walletamount")));
            }
        });
        amountToBeRedeemed = (EditText)findViewById(R.id.amountToBeRedeemed);

    }
}
