package com.freedeveloper.project_java;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class NewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        Button Button_back = findViewById(R.id.Button_back);
        Button Button_login = findViewById(R.id.Button_login);

        Button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(NewActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
