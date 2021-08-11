package com.example.manitbattlegrounds;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    TextView updatedText;
    ImageView walletImageView;


    @Override
    public void onBackPressed() {
        if (ParseUser.getCurrentUser().getUsername().matches("181112226") || ParseUser.getCurrentUser().getUsername().matches("181116227")) {
            Intent intent = new Intent(getApplicationContext(),CoreActivity.class);
            startActivity(intent);

        }else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    public void joinGame(View view){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("PersonalDetails");
        query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if(e == null){
                    if(parseObject != null){

                        ParseObject requestList = new ParseObject("RequestListForParticipation");
                        requestList.put("username",parseObject.getString("username"));
                        requestList.put("name",parseObject.getString("name"));
                        requestList.put("pubgusername",parseObject.getString("pubgusername"));
                        requestList.put("branch",parseObject.getString("branch"));
                        requestList.put("year",parseObject.getString("year"));
                        requestList.put("mobilenumber",parseObject.getString("mobilenumber"));
                        requestList.put("email",parseObject.getString("email"));

                        requestList.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(e == null){
                                    Log.i("request list","updated successfully");
                                }
                            }
                        });
                    }
                }else {
                    Log.i("Error",e.getMessage());
                }
            }
        });


        Intent intent = new Intent(HomeActivity.this,JoinGame.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        walletImageView = (ImageView) findViewById(R.id.walletImageView);

        walletImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Image","clicked");
                Intent intent = new Intent(HomeActivity.this,WalletActivity.class);
                startActivity(intent);

            }
        });

        updatedText = (TextView) findViewById(R.id.updatedText);
        updatedText.setMovementMethod(new ScrollingMovementMethod());
        Log.i("new text",CoreActivity.text);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("UpdatedCondition");
        query.orderByDescending("updatedAt");
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if(e == null){
                    if(parseObject != null){
                        parseObject.getString("condition");
                        updatedText.setText(parseObject.getString("condition"));
                    }
                }
            }
        });



        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("ParticipantList");

        query1.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {

                if(e == null){
                    if (list != null && list.size() > 0){
                        Log.i("totsl objects",String.valueOf(list.size()));
                        ListView participantListView = (ListView)findViewById(R.id.participantListView);
                        List<Map<String,String>> participantList = new ArrayList<Map<String, String>>();

                        for(int i=0;i<list.size();i++) {

                            ParseObject object = list.get(i);
                            Log.i("object name",object.getString("name"));
                            Map<String, String> participant = new HashMap<String, String>();
                            participant.put("name",object.getString("name"));
                            participant.put("detail",object.getString("branch") + "," + object.getString("year") + " year");
                            participantList.add(participant);
                        }
                        SimpleAdapter simpleAdapter = new SimpleAdapter(HomeActivity.this,participantList,android.R.layout.simple_list_item_2,new String[]{"name","detail"},new int[] {android.R.id.text1,android.R.id.text2});
                        participantListView.setAdapter(simpleAdapter);


                    }
                }else{
                    Log.i("error",e.getMessage());
                }
            }
        });


    }

    public void showMenu(View v){
        PopupMenu popupMenu = new PopupMenu(this,v);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.main_menu);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logOut :
                ParseUser.logOut();
                Intent intent = new Intent(getApplicationContext(),LoginOrSignupActivity.class);
                startActivity(intent);
                return true;
            case R.id.dashboard :
                Intent intent1 = new Intent(getApplicationContext(),DashboardActivity.class);
                startActivity(intent1);
                return true;
            default:
                return false;
        }
    }
}
