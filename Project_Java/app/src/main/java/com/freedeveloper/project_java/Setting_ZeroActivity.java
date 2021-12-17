package com.freedeveloper.project_java;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Setting_ZeroActivity extends AppCompatActivity {

    Float recentValue;
    Float adjustValue;
    Float afterValue;
    TextView View_before;
    EditText Edit_input;
    TextView View_after;
    Button Button_save;
    Button Button_set;

    EditText input_url;
    String testurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_zero);
        View_before = findViewById(R.id.View_before);
        Edit_input = findViewById(R.id.Edit_input);
        View_after = findViewById(R.id.View_after);
        Button_save = findViewById(R.id.Button_save);
        Button_set = findViewById(R.id.Button_set);
        input_url = findViewById(R.id.input_url);

        getResentValue();
        setRecentValue();

        Button_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Edit_input.getText().toString().equals("")) {
                    adjustValue = Float.parseFloat(Edit_input.getText().toString());
                    showAfterValue();
                }
                else {
                    View_after.setText("숫자를 입력해주세요");
                }
            }
        });

        Button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAdjustValue();
                Intent intent = new Intent(Setting_ZeroActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(Setting_ZeroActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    void getResentValue() {
        SharedPreferences sharedPref = getSharedPreferences("myPref",Context.MODE_PRIVATE);
        recentValue = sharedPref.getFloat("recentValue",38);
        adjustValue = sharedPref.getFloat("adjustValue",-38);

        testurl = sharedPref.getString("testurl","http://15.164.244.22:8080/FineDustJava_Spring");
    }

    void setRecentValue() {
        View_before.setText(recentValue.toString());
        Edit_input.setHint("현재 : " + adjustValue.toString());
        Float temp = recentValue + adjustValue;
        View_after.setText(String.format("%.1f",temp));

        input_url.setText(testurl);
    }

    void showAfterValue() {
        afterValue = recentValue + adjustValue;
        View_after.setText(afterValue.toString());
    }

    void updateAdjustValue() {
        SharedPreferences sharedPref = getSharedPreferences("myPref",Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPref.edit();

        prefEditor.putFloat("adjustValue",adjustValue);

        testurl = input_url.getText().toString();
        prefEditor.putString("testurl",testurl);
        prefEditor.commit();
    }

}
