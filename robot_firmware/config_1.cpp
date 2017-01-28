//author: Mrinmoy Sarkar
//email:mrinmoy.pol@gmail.com
#include "config_1.h"
#include <arduino.h>
#include <Servo.h>
#include <math.h>
#include<Wire.h>


//motor  initialization
void config_1::motor_init(){

  pinMode(left_motor_1,OUTPUT);
  pinMode(left_motor_2,OUTPUT);
  pinMode(right_motor_1,OUTPUT);
  pinMode(right_motor_2,OUTPUT);
}

// led initialization
void config_1::led_init(){

  pinMode(led_1,OUTPUT);
  pinMode(led_2,OUTPUT);
  pinMode(led_3,OUTPUT);
  pinMode(led_4,OUTPUT);
  pinMode(led_5,OUTPUT);

}

// button initialization
void config_1::button_init(){

  pinMode(button_home,INPUT);
  pinMode(button_up,INPUT);
  pinMode(button_down,INPUT);
  pinMode(button_ok,INPUT);
  pinMode(button_back,INPUT);

}

//buzzer initialization
void config_1::buzzer_init()
{
  pinMode(buzzer,OUTPUT);
}

// servo initialization
void config_1::servo_init()
{
  myservo_1.attach(servo_1);
  myservo_2.attach(servo_2);
  myservo_3.attach(servo_3);
  myservo_4.attach(servo_4);
  myservo_5.attach(servo_5);
  myservo_6.attach(servo_6);
}


void config_1::servo_write(int servo_number,int servo_position)
{

  switch (servo_number) {
  case 1 :
    myservo_1.write(servo_position);
    break;

  case 2 :
    myservo_2.write(servo_position);
    break;

  case 3 :
    myservo_3.write(servo_position);
    break;

  case 4 :
    myservo_4.write(servo_position);
    break;

  case 5 :
    myservo_5.write(servo_position);
    break;

  case 6 :
    myservo_6.write(servo_position);
    break;  

//  default:
  //  break;      
  }

}

void config_1::left_motor(int motor_speed){

  motor_speed = constrain(motor_speed,-255,255);
  if (motor_speed>=0)
  {
    analogWrite(left_motor_1,motor_speed);
    analogWrite(left_motor_2,0);
  }
  else{

    analogWrite(left_motor_1,0);
    analogWrite(left_motor_2,abs(motor_speed));
  }

}


void config_1::right_motor(int motor_speed){

  motor_speed = constrain(motor_speed,-255,255);
  if (motor_speed>=0)
  {
    analogWrite(right_motor_1,motor_speed);
    analogWrite(right_motor_2,0);
  }
  else{

    analogWrite(right_motor_1,0);
    analogWrite(right_motor_2,abs(motor_speed));
  }

}


void config_1::led_on(int led_number)
{


  if (led_number>0)
  {
    switch(led_number)
    {

    case 1:
      digitalWrite(led_1,HIGH);
      break;

    case 2:
      digitalWrite(led_2,HIGH);
      break;

    case 3:
      digitalWrite(led_3,HIGH);
      break;

    case 4:
      digitalWrite(led_4,HIGH);
      break;

    case 5:
      digitalWrite(led_5,HIGH);
      break;

    default:
      break;
    }
  }

  else
  {
    int led=led_number*(-1);

    switch(led)
    {
    case 1:
      digitalWrite(led_1,LOW);
      break;

    case 2:
      digitalWrite(led_2,LOW);
      break;

    case 3:
      digitalWrite(led_3,LOW);
      break;

    case 4:
      digitalWrite(led_4,LOW);
      break;

    case 5:
      digitalWrite(led_5,LOW);
      break;

    default:
      digitalWrite(led_1,LOW);
      digitalWrite(led_2,LOW);
      digitalWrite(led_3,LOW);
      digitalWrite(led_4,LOW);
      digitalWrite(led_5,LOW);
      break;
    }


  }

}


// button pressed 

int config_1::button_pressed()
{

  if (digitalRead(button_home)==1) return 1;
  else if(digitalRead(button_up)==1) return 2;
  else if(digitalRead(button_down)==1) return 3;
  else if(digitalRead(button_ok)==1) return 4;
  else if(digitalRead(button_back)==1) return 5;
  else return 0;

}


void config_1::sonar_init()
{
  pinMode(sonar_trig, OUTPUT);
  pinMode(sonar_echo, INPUT);
}


long config_1::get_distance()
{

  long duration, cm;

  digitalWrite(sonar_trig, LOW);
  delayMicroseconds(2);
  digitalWrite(sonar_trig, HIGH);
  delayMicroseconds(5);
  digitalWrite(sonar_trig, LOW);

  duration = pulseIn(sonar_echo, HIGH);

  cm=(duration/29/2);

  return cm;
}



double config_1::read_angle() {
  int regb=0x01;
  int regbdata=0x40;
  int outputData[6];

  int i,x,y,z;
  double angle;

  Wire.beginTransmission(HMC5883_WriteAddress);
  Wire.write(regb);
  Wire.write(regbdata);
  Wire.endTransmission();

  delay(50);
  Wire.beginTransmission(HMC5883_WriteAddress); //Initiate a transmission with HMC5883 (Write address).
  Wire.write(HMC5883_ModeRegisterAddress);       //Place the Mode Register Address in write-buffer.
  Wire.write(HMC5883_ContinuousModeCommand);     //Place the command for Continuous operation Mode in write-buffer.
  Wire.endTransmission();                       //write the write-buffer to HMC5883 and end the I2C transmission.
  delay(50);


  Wire.beginTransmission(HMC5883_WriteAddress);  //Initiate a transmission with HMC5883 (Write address).
  Wire.requestFrom(HMC5883_WriteAddress,6);      //Request 6 bytes of data from the address specified.

  delay(10);


  //Read the value of magnetic components X,Y and Z

  if(6 <= Wire.available()) // If the number of bytes available for reading be <=6.
  {
    for(i=0;i<6;i++)
    {
      outputData[i]=Wire.read();  //Store the data in outputData buffer
    }
  }

  x=outputData[0] << 8 | outputData[1]; //Combine MSB and LSB of X Data output register
  z=outputData[2] << 8 | outputData[3]; //Combine MSB and LSB of Z Data output register
  y=outputData[4] << 8 | outputData[5]; //Combine MSB and LSB of Y Data output register


  angle= atan2((double)y,(double)x) * (180 / 3.14159265) + 180; // angle in degrees


  //Serial3.println(": Angle between X-axis and the South direction ");
  //Serial3.println(angle,2);
  //Serial3.println(" Deg");
  return angle;
}











