package com.example.manitbattlegrounds;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MoneyRequestListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_request_list);

        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("MoneyRequestList");

        query1.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {

                if(e == null){
                    if (list != null && list.size() > 0){
                        Log.i("total objects",String.valueOf(list.size()));
                        ListView moneyRequestListView = (ListView)findViewById(R.id.moneyRequestListView);
                        List<Map<String,String>> requestList = new ArrayList<Map<String, String>>();

                        for(int i=0;i<list.size();i++) {

                            ParseObject object = list.get(i);
                            Log.i("object name",object.getString("name"));
                            Map<String, String> request = new HashMap<String, String>();
                            request.put("name",object.getString("name") + "," + object.getString("branch") + "," + object.getString("year") + " year requested " + object.getString("moneyrequested") + " rupees on " + String.valueOf(object.getUpdatedAt()));
                            request.put("detail",object.getString("pubgusername") + "," + object.getString("email") + "," + object.getString("mobilenumber"));
                            requestList.add(request);
                        }
                        SimpleAdapter simpleAdapter = new SimpleAdapter(MoneyRequestListActivity.this,requestList,android.R.layout.simple_list_item_2,new String[]{"name","detail"},new int[] {android.R.id.text1,android.R.id.text2});
                        moneyRequestListView.setAdapter(simpleAdapter);


                    }
                }else{
                    Log.i("error",e.getMessage());
                }
            }
        });

    }
}
