package com.freedeveloper.project_java;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button Button_login = findViewById(R.id.Button_login);
        Button Button_new = findViewById(R.id.Button_new);

        Button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, Login_SelectActivity.class);
//                POSTTask task = new POSTTask();
//                task.execute();
//
//                GETTask getTask = new GETTask();
//                getTask.execute();
                startActivity(intent);
                finish();
            }
        });

        Button_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, NewActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private long time= 0;
    @Override
    public void onBackPressed(){
        if(System.currentTimeMillis()-time>=2000){
            time=System.currentTimeMillis();
            Toast.makeText(getApplicationContext(),"뒤로 버튼을 한번 더 누르면 종료합니다.",Toast.LENGTH_SHORT).show();
        }else if(System.currentTimeMillis()-time<2000){
            finish();
        }
    }

    class POSTTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try{
                String str;
                URL url=new URL("http://172.20.10.2:8080/sample/POST");


                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg="time=3&data=3";
                osw.write(sendMsg);
                osw.flush();

                if(conn.getResponseCode()==conn.HTTP_OK){
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(),"UTF-8");
                    BufferedReader reader= new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while((str=reader.readLine())!=null){
                        buffer.append(str);
                    }
                    receiveMsg=buffer.toString();
                }else{
                    Log.i("통신 결과1", conn.getResponseCode()+"에러");
                }

                Log.e("RCV DATA", receiveMsg);
            }catch (Exception e){
                e.printStackTrace();
            }

            return receiveMsg;
        }

    }
    class GETTask extends AsyncTask<String, Void, String>{
        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try{
                String str;
                String get_url = "http://172.20.10.2:8080/sample/GET";
                get_url += "/ABC";
                URL url=new URL(get_url);

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



/*
                if(conn.getResponseCode()==conn.HTTP_OK){
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(),"UTF-8");
                    BufferedReader reader= new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while((str=reader.readLine())!=null){
                        buffer.append(str);
                    }
                    receiveMsg=buffer.toString();
                }else{
                    Log.i("통신 결과2", conn.getResponseCode()+"에러");
                }*/


            }catch (Exception e){
                e.printStackTrace();
            }

            return receiveMsg;
        }

    }
}
