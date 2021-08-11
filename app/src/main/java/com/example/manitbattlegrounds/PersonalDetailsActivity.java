package com.example.manitbattlegrounds;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class PersonalDetailsActivity extends AppCompatActivity {

    private static final String[] branches = new String[]{"CSE","ECE","EE","MECH","CHEM","MSME","ARCHITECTURE","PLANNING"};
    private static final String[] years = new String[]{"1st","2nd","3rd","4th","5th"};

    EditText nameEditText;
    AutoCompleteTextView branchEditText;
    AutoCompleteTextView yearEditText;
    ImageView branchDropImageView;
    ImageView yearDropImageView;
    EditText pubgUsernameEditText;
    EditText mobileNumberEditText;
    EditText mailEditText;



    public void proceedToRelatedActivity(View view){

        boolean correctBranch = false;
        boolean correctYear = false;


        for(String string : branches){
            if(branchEditText.getText().toString().matches(string)){
                correctBranch = true;
            }
        }

        for (String string : years){
            if(yearEditText.getText().toString().matches(string)){
                correctYear = true;
            }
        }


        if(nameEditText.getText().toString().matches("") || branchEditText.getText().toString().matches("") || yearEditText.getText().toString().matches("") || pubgUsernameEditText.getText().toString().matches("") || mobileNumberEditText.getText().toString().matches("") || mailEditText.getText().toString().matches("")){
            Toast.makeText(this, "Please fill all the required entries", Toast.LENGTH_SHORT).show();
        }
        else{

            if(!correctBranch || !correctYear){
                Toast.makeText(this, "Invalid branch/year,please use the suggetion in the dropbox.", Toast.LENGTH_LONG).show();
            }else if(mobileNumberEditText.getText().toString().length() != 10){
                Toast.makeText(this, "Mobile no. should contain 10 digits. Please check your mobile again!", Toast.LENGTH_LONG).show();
            }
            else {
                ParseObject personalDetails = new ParseObject("PersonalDetails");
                personalDetails.put("username", ParseUser.getCurrentUser().getUsername());
                personalDetails.put("name", nameEditText.getText().toString());
                personalDetails.put("branch", branchEditText.getText().toString());
                personalDetails.put("year", yearEditText.getText().toString());
                personalDetails.put("mobilenumber",mobileNumberEditText.getText().toString());
                personalDetails.put("email",mailEditText.getText().toString());
                personalDetails.put("pubgusername",pubgUsernameEditText.getText().toString());

                personalDetails.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {

                            Toast.makeText(PersonalDetailsActivity.this, "signed up successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(PersonalDetailsActivity.this, "sign up failed, Please try again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                Intent intent;
                if(ParseUser.getCurrentUser().getUsername().matches("181112226") || ParseUser.getCurrentUser().getUsername().matches("181116227")){
                    intent = new Intent(PersonalDetailsActivity.this,CoreActivity.class);
                }else{
                    intent = new Intent(PersonalDetailsActivity.this,HomeActivity.class);
                }
                startActivity(intent);

            }

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details);

        nameEditText = (EditText)findViewById(R.id.nameEditText);
        branchEditText= (AutoCompleteTextView) findViewById(R.id.branchEditText);
        yearEditText = (AutoCompleteTextView) findViewById(R.id.yearEditText);
        branchDropImageView = (ImageView)findViewById(R.id.branchDropImageView);
        yearDropImageView = (ImageView)findViewById(R.id.yearDropImageView);
        branchEditText.setThreshold(1);
        yearEditText.setThreshold(1);

        pubgUsernameEditText = (EditText)findViewById(R.id.pubgUsernameEditText);
        mobileNumberEditText = (EditText)findViewById(R.id.mobileNumberEditText);

        mailEditText = (EditText) findViewById(R.id.mailEditText);

        ArrayAdapter<String> branchAdapter = new ArrayAdapter<String>(PersonalDetailsActivity.this,android.R.layout.simple_dropdown_item_1line,branches);
        branchEditText.setAdapter(branchAdapter);

        branchDropImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                branchEditText.showDropDown();
            }
        });

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(PersonalDetailsActivity.this,android.R.layout.simple_dropdown_item_1line,years);
        yearEditText.setAdapter(yearAdapter);

        yearDropImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yearEditText.showDropDown();
            }
        });

    }
}
