package com.freedeveloper.project_java;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private Context context = this;
    //시간 설정 id
    TextView TextView_time;
    TextView TextView_day;
    TextView TextView_location;
    ImageView ImageView_status;
    TextView TextView_per;
    TextView TextView_status;

    //그래프 추가
    int dayCount = 0;
    private LineChart chart;
    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
    ArrayList<Entry> values = new ArrayList<>();
    float sum = 0;
    int nowTime = -1;
    int nowDay = -1;
    int nowCount = 0;

    //데이터 get
    String mJsonString;
    ArrayList<HashMap<String, String>> arraylist1 = new ArrayList<HashMap<String, String>>();
    int location = 0;

    String testurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chart = findViewById(R.id.linechart);
        TextView_location = findViewById(R.id.TextView_location);
        TextView_location.setText("Seoul");
        ImageView_status = findViewById(R.id.ImageView_status);
        TextView_per = findViewById(R.id.TextView_per);
        TextView_status = findViewById(R.id.TextView_status);

        getLocation();

        //현재시간 1초마다 갱신
        startTime();
        ShowTimeMethod();

        //데이터 수신 코드
        GETTask getTask = new GETTask();
        getTask.execute();


        //메인 화면 설정
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.menuicon); //뒤로가기 버튼 이미지 지정
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                int id = menuItem.getItemId();
                String title = menuItem.getTitle().toString();

                if(id == R.id.menu_4){
                    Intent intent = new Intent(MainActivity.this, SelectDeviceActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if(id == R.id.menu_5){
                    Intent intent = new Intent(MainActivity.this, Setting_ZeroActivity.class);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ // 왼쪽 상단 버튼 눌렀을 때
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(MainActivity.this,Login_SelectActivity.class);
        startActivity(intent);
        finish();
    }

    //시간 갱신코드
    public void ShowTimeMethod() {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                //시간 설정 기능
                startTime();
            }
        };

        Runnable task = new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {}
                    handler.sendEmptyMessage(1);
                }
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    public void startTime() {
        TextView_day = findViewById(R.id.TextView_day);
        TextView_time = findViewById(R.id.TextView_time);
        //시간 설정 기능
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat format_Day = new SimpleDateFormat("yyyy년 MM월 dd일");
        SimpleDateFormat format_Time = new SimpleDateFormat("aa hh:mm");
        String day = format_Day.format(date);
        String time = format_Time.format(date);
        TextView_day.setText(day);
        TextView_time.setText(time);
    }

    public void addGraph(ArrayList<Entry> values) {

        ArrayList<Entry> inputValues = values;

        LineDataSet set;

        if(dayCount == 1) {

            if(location == 0) {
                set = new LineDataSet(inputValues, "Yesterday : Indoor");
            }
            else {
                set = new LineDataSet(inputValues, "Yesterday : Outdoor");
            }
            set.setColor(Color.BLACK);
            set.setValueTextColor(Color.WHITE);
        }
        else {
            if(location == 0) {
                set = new LineDataSet(inputValues, "Today : Indoor");
            }
            else {
                set = new LineDataSet(inputValues, "Today : Outdoor");
            }
            set.setColor(Color.RED);
            set.setValueTextColor(Color.BLACK);
        }

        set.setCircleColor(Color.WHITE);
        set.setLineWidth(3f);
        set.setCircleRadius(5f);

        set.setDrawCircleHole(false);

        set.setValueTextSize(13f);
        set.setValueTextColor(Color.WHITE);
        set.enableDashedHighlightLine(10f, 5f, 0f);

        dataSets.add(set); // add the data sets
    }

    public void setGraph() {
        // create a data object with the data sets
        LineData data = new LineData(dataSets);
        chart.getDescription().setEnabled(false);
        chart.setTouchEnabled(true);
        chart.setNoDataText("클릭해주세요!");
        // set data
        chart.setData(data);
        Log.e("SET GRAPH COMPLETE!","");
    }

    //json 데이터 get 내용

    class GETTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            JsonParsing();
            setGraph();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                String get_url = testurl+"/GET";
//                String get_url = "http://15.164.244.22:8080/FineDustJava_Spring/GET";
                get_url += "/Soeul/"+location;
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
            int TODAY = returnToday();
            Log.e("TODAY : ",TODAY+"");
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

//                Log.e("JSON : ",  no + ", "+ spotName+ ", " + dustDegree +", " + time + ", " + isIndoor);

                hashMap.put("no",no);
                hashMap.put("spotName", spotName);
                hashMap.put("dustDegree", dustDegree);
                hashMap.put("time",time);
                hashMap.put("isIndoor", isIndoor);
                //Log.e("PUT","FINISH");
                arraylist1.add(hashMap);



                //그래프에 데이터 추가
                int intDay = returnDay(time);
                Log.e("DAY : ",intDay+"");
                //어제 또는 오늘인 경우만 추가
                if(intDay >= TODAY-1 && intDay != 0) {

                    int intTime = returnHour(time);
                    //맨 처음값 : 초기값으로 변경
                    if(nowTime == -1) {
                        nowTime = intTime;
                        nowDay = intDay;
                    }

                    //현재 시간이 이전데이터의 시간과 동일한 경우 : 평균을 위해 더한다
                    if(nowTime == intTime && nowDay == intDay) {
                        sum += Float.parseFloat(dustDegree);
                        Log.e("TIME : ",nowTime+", SUM : " + sum);
                        nowCount++;
                    }
                    //시간이 다른경우
                    else if(nowDay == intDay && nowTime != intTime) {
                        //이전 시간까지 데이터를 추가한다
                        values.add(new Entry(nowTime, returnFloat()));
                        Log.e("ADD COMPLETE : ",nowTime+", "+returnFloat());

                        //새로들어온 시간으로 초기화를 한다
                        nowTime = intTime;
                        nowDay = intDay;
                        sum = Float.parseFloat(dustDegree);
                        Log.e("TIME : ",nowTime+", SUM : " + sum);
                        nowCount = 1;
                    }
                    //날짜가 달라진 경우 : 이전날짜를 그래프로 최종 마무리 한다
                    else {
                        //이전날짜의 마지막 데이터를 추가한다
                        values.add(new Entry(nowTime, returnFloat()));
                        Log.e("ADD COMPLETE : ",nowTime+", "+returnFloat());

                        //그래프에 추가한다
                        dayCount++;
                        addGraph(values);
                        Log.e("ADD GRAPH COMPLETE : ",dayCount+"");
                        values = new ArrayList<>();

                        //새로들어온 시간으로 초기화를 한다
                        nowTime = intTime;
                        nowDay = intDay;
                        sum = Float.parseFloat(dustDegree);
                        Log.e("TIME : ",nowTime+", SUM : " + sum);
                        nowCount = 1;
                    }

                }

            }
            //데이터 입력 종료 : 마지막 평균값 추가
            if(sum != 0) {
                Log.e("TIME : ",nowTime+", SUM : " + sum);
                float temp = returnFloat();
                values.add(new Entry(nowTime, temp));
                Log.e("ADD COMPLETE : ",nowTime+", "+temp);

                //그래프에 추가한다
                dayCount++;
                addGraph(values);
                Log.e("ADD GRAPH COMPLETE : ",dayCount+"");

                TextView_per.setText(temp+"");
                setImage(temp);
            }


        } catch (JSONException e) {

            Log.d("showResult : ", String.valueOf(e));
        }


    }

    int returnHour(String time) {
        String result = "";
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss");
            Date to = dateFormat.parse(time);
            SimpleDateFormat stringFormat = new SimpleDateFormat("HH");
            result = stringFormat.format(to);
        }catch(Exception e) {}
        return Integer.parseInt(result);
    }
    int returnDay(String time) {
        String result = "";
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss");
            Date to = dateFormat.parse(time);
            SimpleDateFormat stringFormat = new SimpleDateFormat("dd");
            result = stringFormat.format(to);
        }catch(Exception e) {}
        return Integer.parseInt(result);
    }
    int returnToday() {
        SimpleDateFormat todayFormat = new SimpleDateFormat("dd");
        Date today = new Date();
        return Integer.parseInt(todayFormat.format(today));
    }
    float returnFloat() {
        float temp = sum/nowCount;
        String ttemp = String.format("%.1f",temp);
        return Float.parseFloat(ttemp);
    }
    void setImage(float dust) {
        if(dust < 10) {
            TextView_status.setText("좋음");
            ImageView_status.setImageResource(R.drawable.good);
        }
        else if(dust < 20) {
            TextView_status.setText("보통");
            ImageView_status.setImageResource(R.drawable.soso);
        }
        else {
            TextView_status.setText("나쁨");
            ImageView_status.setImageResource(R.drawable.bad);
        }
    }
    void getLocation() {
        SharedPreferences sharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        location = sharedPref.getInt("location",0);

        testurl = sharedPref.getString("testurl","http://15.164.244.22:8080/FineDustJava_Spring");
    }
    void test() {
        dayCount++;
        float f1 = (float) 7.7;
        values.add(new Entry(16, f1));
        float f2 = (float) 8.9;
        values.add(new Entry(17, f2));
        float f3 = (float) 5.8;
        values.add(new Entry(18, f3));
        float f4 = (float) 9.2;
        values.add(new Entry(19, f4));
        addGraph(values);
        values = new ArrayList<>();
        dayCount++;
        float f11 = (float) 8.4;
        values.add(new Entry(16, f11));
        float f12 = (float) 5.7;
        values.add(new Entry(17, f12));
        float f13 = (float) 6.6;
        values.add(new Entry(18, f13));
        float f14 = (float) 9.2;
        values.add(new Entry(19, f14));
        addGraph(values);
        setGraph();
    }
}