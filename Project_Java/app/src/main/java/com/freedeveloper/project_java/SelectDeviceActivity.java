package com.freedeveloper.project_java;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.Vector;

public class SelectDeviceActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 10; // 블루투스 활성화 상태
    private BluetoothAdapter bluetoothAdapter; // 블루투스 어댑터
    private Set<BluetoothDevice> devices; // 블루투스 디바이스 데이터 셋
    private BluetoothDevice bluetoothDevice; // 블루투스 디바이스
    private BluetoothSocket bluetoothSocket = null; // 블루투스 소켓
    private OutputStream outputStream = null; // 블루투스에 데이터를 출력하기 위한 출력 스트림
    private InputStream inputStream = null; // 블루투스에 데이터를 입력하기 위한 입력 스트림
    private Thread workerThread = null; // 문자열 수신에 사용되는 쓰레드
    private byte[] readBuffer; // 수신 된 문자열을 저장하기 위한 버퍼
    private int readBufferPosition; // 버퍼 내 문자 저장 위치

    private TextView View_adjust;
    private TextView textViewReceive; // 수신 된 데이터를 표시하기 위한 텍스트 뷰
    private Button buttonSend; // 송신하기 위한 버튼
    private TextView countView;
    private ScrollView scrollView;
    private TextView sendData;
    private Button setting_zero;

    int location = 0;
    int sendCount = 0;


    private int pariedDeviceCount;
    byte mDelimiter = 10;
    Float[] data = new Float[60];
    int count = 0;
    Float adjustValue;


    boolean isReady = false;
//    public ArrayList<String> list = new ArrayList<>();
    JSONArray jsonArray = new JSONArray();

    //보내는 데이터
    String TIME;
    String DATA;

    String testurl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_device);

        getLocation();

        // 각 컨테이너들의 id를 매인 xml과 맞춰준다.
        View_adjust = (TextView)findViewById(R.id.View_adjust);
        textViewReceive = (TextView)findViewById(R.id.textView_receive);
        countView = (TextView)findViewById(R.id.countView);
        scrollView = (ScrollView)findViewById(R.id.scrollView);
        sendData = (TextView)findViewById(R.id.sendData);
        setting_zero = (Button)findViewById(R.id.setting_zero);

        countView.setText("수신된 데이터 수 : " + count);
        setting_zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bluetoothSocket!=null) //If the btSocket is busy
                {
                    try
                    {
                        bluetoothSocket.close(); //close connection
                    }
                    catch (IOException e)
                    { Log.d("Error",e.toString());}
                }
                finish();
                Intent intent = new Intent(SelectDeviceActivity.this, Setting_ZeroActivity.class);
                startActivity(intent);
            }
        });

        //영점조절값 가져오기
        getAdjustValue();

        //외부저장 테스트




        // 블루투스 활성화하기
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); // 블루투스 어댑터를 디폴트 어댑터로 설정



        if(bluetoothAdapter == null) { // 디바이스가 블루투스를 지원하지 않을 때
            // 여기에 처리 할 코드를 작성하세요.
            textViewReceive.setText("연결이 되지 않았습니다.\n다시 시도하시길 바랍니다.");
        }

        else { // 디바이스가 블루투스를 지원 할 때
            if(bluetoothAdapter.isEnabled()) { // 블루투스가 활성화 상태 (기기에 블루투스가 켜져있음)
                selectBluetoothDevice(); // 블루투스 디바이스 선택 함수 호출
            }

            else { // 블루투스가 비 활성화 상태 (기기에 블루투스가 꺼져있음)
                // 블루투스를 활성화 하기 위한 다이얼로그 출력
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                // 선택한 값이 onActivityResult 함수에서 콜백된다.
                startActivityForResult(intent, REQUEST_ENABLE_BT);
            }

        }

    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        switch (requestCode) {
            case REQUEST_ENABLE_BT :
                if(requestCode == RESULT_OK) { // '사용'을 눌렀을 때
                    selectBluetoothDevice(); // 블루투스 디바이스 선택 함수 호출
                }

                else { // '취소'를 눌렀을 때
                    textViewReceive.setText("연결이 되지 않았습니다.\n다시 시도하시길 바랍니다.");
                }
                break;
        }
    }

    public void selectBluetoothDevice() {

        // 이미 페어링 되어있는 블루투스 기기를 찾습니다.
        devices = bluetoothAdapter.getBondedDevices();
        // 페어링 된 디바이스의 크기를 저장
        pariedDeviceCount = devices.size();
        // 페어링 되어있는 장치가 없는 경우
        if(pariedDeviceCount == 0) {
            // 페어링을 하기위한 함수 호출
        }

        // 페어링 되어있는 장치가 있는 경우
        else {
            // 디바이스를 선택하기 위한 다이얼로그 생성
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("페어링 되어있는 블루투스 디바이스 목록");
            // 페어링 된 각각의 디바이스의 이름과 주소를 저장
            List<String> list = new ArrayList<>();
            // 모든 디바이스의 이름을 리스트에 추가
            for(BluetoothDevice bluetoothDevice : devices) {
                list.add(bluetoothDevice.getName());
            }

//            list.add("취소");

            // List를 CharSequence 배열로 변경
            final CharSequence[] charSequences = list.toArray(new CharSequence[list.size()]);
            list.toArray(new CharSequence[list.size()]);



            // 해당 아이템을 눌렀을 때 호출 되는 이벤트 리스너
            builder.setItems(charSequences, new DialogInterface.OnClickListener() {

                @Override

                public void onClick(DialogInterface dialog, int which) {
                    // 해당 디바이스와 연결하는 함수 호출
                    connectDevice(charSequences[which].toString());
                }
            });



            // 뒤로가기 버튼 누를 때 창이 안닫히도록 설정

            builder.setCancelable(false);

            // 다이얼로그 생성

            AlertDialog alertDialog = builder.create();

            alertDialog.show();

        }

    }

    public void connectDevice(String deviceName) {

        // 페어링 된 디바이스들을 모두 탐색

        for(BluetoothDevice tempDevice : devices) {
            // 사용자가 선택한 이름과 같은 디바이스로 설정하고 반복문 종료
            if(deviceName.equals(tempDevice.getName())) {
                bluetoothDevice = tempDevice;
                break;
            }
        }

        // UUID 생성
        UUID uuid = java.util.UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

        // Rfcomm 채널을 통해 블루투스 디바이스와 통신하는 소켓 생성
        try {
            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
            bluetoothSocket.connect();

            // 데이터 송,수신 스트림을 얻어옵니다.
            outputStream = bluetoothSocket.getOutputStream();
            inputStream = bluetoothSocket.getInputStream();

            // 데이터 수신 함수 호출
            receiveData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveData() {

        final Handler handler = new Handler();
        // 데이터를 수신하기 위한 버퍼를 생성
        readBufferPosition = 0;
        readBuffer = new byte[1024];


        // 데이터를 수신하기 위한 쓰레드 생성
        workerThread = new Thread(new Runnable() {

            @Override

            public void run() {
                while(!Thread.currentThread().isInterrupted() && !isReady) {
                    try {
                        // 데이터를 수신했는지 확인합니다.
                        int byteAvailable = inputStream.available();
                        // 데이터가 수신 된 경우
                        if(byteAvailable > 0) {
                            // 입력 스트림에서 바이트 단위로 읽어 옵니다.
                            byte[] bytes = new byte[byteAvailable];
                            inputStream.read(bytes);
                            // 입력 스트림 바이트를 한 바이트씩 읽어 옵니다.

                            for(int i = 0; i < byteAvailable; i++) {
                                byte tempByte = bytes[i];
                                // 개행문자를 기준으로 받음(한줄)
                                if(tempByte == 10) {
                                    // readBuffer 배열을 encodedBytes로 복사
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    // 인코딩 된 바이트 배열을 문자열로 변환


                                    final String text = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;
                                    handler.post(new Runnable() {

                                        @Override

                                        public void run() {
                                            // 텍스트 뷰에 출력
                                            textViewReceive.append(text + "\n");
                                            //string -> double 변환, array에 추가하기
                                            addData(text);
                                            if(count == 60) {
                                                workerThread.interrupt();
                                            }
                                        }

                                    });

                                } // 개행 문자가 아닐 경우

                                else {
                                    readBuffer[readBufferPosition++] = tempByte;
                                }

                            }

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }

        });
        workerThread.start();

    }

    void sendData(String text) {
        // 문자열에 개행문자("\n")를 추가해줍니다.
        text += "\n";
        try{
            // 데이터 송신
            outputStream.write(text.getBytes());
        }catch(Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed(){
        if (bluetoothSocket!=null) //If the btSocket is busy
        {
            try
            {
                bluetoothSocket.close(); //close connection
            }
            catch (IOException e)
            { Log.d("Error",e.toString());}
        }
        finish();


        Intent intent = new Intent(SelectDeviceActivity.this, MainActivity.class);
        startActivity(intent);
    }

    void addData(String inputData) {
        try {
            float temp = Float.parseFloat(inputData);
            data[count] = temp;
            count++;
            countView.setText("수신된 데이터 수 : " + count);

            scrollView.post(new Runnable(){
                public void run(){
                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                }
            });

            if(count == 60 && !isReady) {
                workerThread.interrupt();
                isReady = true;
                count = 0;
                stopInputData();
            }
        } catch(Exception e) {}

    }

    void stopInputData() {
        //1. 시간 설정
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat format_Now = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss");
        //2. 평균값 생성
        float sum = 0;
        for(float temp : data) {
            sum += temp;
        }
        sum /= 60;
        //영점조절값
        sum += adjustValue;
        //3. 보낼 데이터 두가지
        TIME = format_Now.format(date);
        DATA = String.format("%.1f",sum);

        sendCount++;
        String sendMsg = "Send Count : " + sendCount + ", DATA=" + DATA;
        //보내기
        sendData.setText(sendMsg);
        setJsonData();

        textViewReceive.setText("");
        isReady = false;
        receiveData();
    }

    void setJsonData() {
        try {
            JSONObject object = new JSONObject();
            object.put("TIME",TIME);
            object.put("DATA",DATA);
            jsonArray.put(object);

            //내부저장 테스트
            String stringJson = jsonArray.toString();
            SharedPreferences sharedPref = getSharedPreferences("myPref",Context.MODE_PRIVATE);
            SharedPreferences.Editor prefEditor = sharedPref.edit();
            prefEditor.putString("jsonArray",stringJson);
            prefEditor.putFloat("recentValue",Float.parseFloat(DATA)-adjustValue);
            prefEditor.commit();

            //외부저장 테스트
            POSTTask task = new POSTTask();
            task.execute();

//            GETTask getTask = new GETTask();
//            getTask.execute();

        } catch(JSONException e) {
            Log.e("Faild to vreate JSONObject",e.toString());
        }

    }

    void getAdjustValue() {
        SharedPreferences sharedPref = getSharedPreferences("myPref",Context.MODE_PRIVATE);
        adjustValue = sharedPref.getFloat("adjustValue",-38);
        View_adjust.setText("조절값 : " + adjustValue.toString());
    }


    class POSTTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try{
                String str;
                URL url=new URL(testurl+"/POST");
//                URL url = new URL("http://15.164.244.22:8080/FineDustJava_Spring/POST");


                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg="spotName=Soeul&dustDegree="+DATA+"&time="+TIME+"&isIndoor="+location;     //isIndoor 0은 실내 1은 실외
                osw.write(sendMsg);                                                                //spotName은 사용자가 입력한 장소
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

    void getLocation() {
        SharedPreferences sharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        location = sharedPref.getInt("location",0);

        testurl = sharedPref.getString("testurl","http://15.164.244.22:8080/FineDustJava_Spring");
    }



}