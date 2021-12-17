package com.freedeveloper.project_java;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Login_SelectActivity extends AppCompatActivity {

    ImageView Button_in_check;
    Button Button_in;
    ImageView Button_out_check;
    Button Button_out;
    int location = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_select);

        Button_in_check = findViewById(R.id.Button_in_check);
        Button_in = findViewById(R.id.Button_in);
        Button_out_check = findViewById(R.id.Button_out_check);
        Button_out = findViewById(R.id.Button_out);

        setUrl();
        getLocation();
        setImage();

        Button Button_enter = findViewById(R.id.Button_enter);

        Button_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLocation();
                Intent intent = new Intent(Login_SelectActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        Button_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button_in_check = findViewById(R.id.Button_in_check);
                Button_out_check = findViewById(R.id.Button_out_check);
                Log.e("BUTTON CLICK : ","IN");
                location = 0;
                Button_in_check.setBackgroundResource(R.drawable.check_t);
                Button_out_check.setBackgroundResource(R.drawable.check_f);
            }
        });

        Button_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button_in_check = findViewById(R.id.Button_in_check);
                Button_out_check = findViewById(R.id.Button_out_check);
                Log.e("BUTTON CLICK : ","OUT");
                location = 1;
                Button_in_check.setBackgroundResource(R.drawable.check_f);
                Button_out_check.setBackgroundResource(R.drawable.check_t);
            }
        });

    }

    void getLocation() {
        SharedPreferences sharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        location = sharedPref.getInt("location",0);
    }
    void setImage() {
        if(location == 0) {
            Button_in_check.setBackgroundResource(R.drawable.check_t);
            Button_out_check.setBackgroundResource(R.drawable.check_f);
        }
        else {
            Button_in_check.setBackgroundResource(R.drawable.check_f);
            Button_out_check.setBackgroundResource(R.drawable.check_t);
        }
    }
    void setLocation() {
        SharedPreferences sharedPref = getSharedPreferences("myPref",Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPref.edit();
        prefEditor.putInt("location",location);
        prefEditor.commit();
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

    void setUrl() {
        SharedPreferences sharedPref = getSharedPreferences("myPref",Context.MODE_PRIVATE);
        String temp = sharedPref.getString("testurl","null");

        if(temp == "null") {
            SharedPreferences.Editor prefEditor = sharedPref.edit();
            prefEditor.putString("testurl", "http://15.164.244.22:8080/FineDustJava_Spring");
            prefEditor.commit();
        }
    }
}
