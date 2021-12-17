package com.freedeveloper.project_java;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class TempActivity extends AppCompatActivity {

    String mJsonString;
    ArrayList<HashMap<String, String>> arraylist1 = new ArrayList<HashMap<String, String>>();


    String stringJsonArray;
    ArrayList<String> listdata = new ArrayList<>();
    TextView showData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temp);

        Button back = findViewById(R.id.back);
        showData = findViewById(R.id.showData);

        getJsonData();


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TempActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(TempActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    void getJsonData() {
        GETTask gettask = new GETTask();
        gettask.execute();
    }

    class GETTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            JsonParsing();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
//                String get_url = "http://172.20.10.2:8080/FineDust/GET";
                String get_url = "http://192.168.55.15:8080/FineDust/GET";
                get_url += "/Soeul/1";                                         //  "/spotName/isIndoor UI에 띄울 값들에 대한 조
                URL url = new URL(get_url);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                //conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                conn.setRequestMethod("GET");
                //conn.setRequestProperty("User-Agent", USER_AGENT);


                int responseCode = conn.getResponseCode();
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();
                // print result
                System.out.println("HTTP 응답 코드 : " + responseCode);
                System.out.println("HTTP body : " + response.toString());
                mJsonString = response.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return receiveMsg;
        }
    }

    private void JsonParsing(){
        try {
            JSONArray jsonArray = new JSONArray(mJsonString);
            for(int i = 0; i<jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String no = jsonObject.getString("no");
                String spotName = jsonObject.getString("spotName");
                String dustDegree = jsonObject.getString("dustDegree");
                String time = jsonObject.getString("time");
                String isIndoor = jsonObject.getString("isIndoor");

                HashMap<String,String> hashMap = new HashMap<>();
                //Log.e("HASH","FINISH");

                Log.e("JSON : ",  no + ", "+ spotName+ ", " + dustDegree +", " + time + ", " + isIndoor);

                hashMap.put("no",no);
                hashMap.put("spotName", spotName);
                hashMap.put("dustDegree", dustDegree);
                hashMap.put("time",time);
                hashMap.put("isIndoor", isIndoor);
                //Log.e("PUT","FINISH");
                arraylist1.add(hashMap);
            }

        } catch (JSONException e) {

            Log.d("showResult : ", String.valueOf(e));
        }

    }

    String transformTime(String time) {
        String result = "";
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss");
            Date to = dateFormat.parse(time);
            SimpleDateFormat stringFormat = new SimpleDateFormat("HH시");
            result = stringFormat.format(to);
        }catch(Exception e) {}
        return result;
    }


}
