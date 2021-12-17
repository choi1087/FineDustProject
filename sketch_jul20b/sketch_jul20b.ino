#include <SoftwareSerial.h>
#define BT_RX 3
#define BT_TX 2
byte buffer[1024];
int bufferPosition = 0; 

float Vo = 0;
float Voc = 0;
float DD = 0;
String mystring;

char command;
String string;

SoftwareSerial HC06(BT_TX,BT_RX);  // RX핀(7번)은 HM10의 TX에 연결 
                                   // TX핀(8번)은 HM10의 RX에 연결  
void setup() {  
  pinMode(7,OUTPUT);
  pinMode(A0, INPUT);
  
  Serial.begin(9600);
  HC06.begin(9600);
}
void loop() {

  // 수신 테스트 코드
  
//  if (Serial.available() > 0)
//  { string = "";  }
//  while(Serial.available() > 0)
//  { command = ((byte)Serial.read());
//    if(command == ':')
//    {
//      break;
//    }
//    else
//    {
//      string += command;
//    }
//    delay(1);
//  }
//  
//  if(string == "TO")
//  {
//    Serial.println(string); //debug
//  }



  //블루투스 초기 셋팅 코드

//  if (HC06.available()) {
//    Serial.write(HC06.read());
//    Serial.write(10);
//  }
//  if (Serial.available()) {
//    HC06.write(Serial.read());
//  }

  // 1초간 데이터 전송 코드
  
  digitalWrite(7, LOW);
  delayMicroseconds(280);
  Vo = analogRead(A0);
  delayMicroseconds(40);
  digitalWrite(7, HIGH);
  delayMicroseconds(9680);

  Voc = Vo * 5 / 1024.0;
  DD = (Voc * 0.172 - 0.0999) * 1000;
  if (DD < 0) {
    DD = 0.00;
  }

  Serial.print("먼지 밀도: ");
  Serial.println(DD);

  if(DD < 75) {
    Serial.println("보통");
  }
  if(DD >= 75) {
    Serial.println("나쁨");
  }
  
  if (Serial.available()) {
    HC06.write(DD);
    HC06.write("============");
    HC06.write(buffer, bufferPosition);
  }

  int one = (int)DD / 10;
  int two = (int)DD % 10;
  int three = (int)(DD*10) % 10;
  int four = (int)(DD*100) % 10;
  if(one!=0)
  {
    HC06.write(one+48);
    HC06.write(two+48);
    HC06.write(".");
    HC06.write(three+48);
    HC06.write(four+48);
    HC06.write(10);
  }
  else
  {
    HC06.write(one+48);
  }
  delay(800);
  
}
