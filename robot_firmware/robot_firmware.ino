//author: Mrinmoy Sarkar
//email:mrinmoy.pol@gmail.com
#include "config_1.h"
#include <Servo.h>
#include "I2Cdev.h"
#include "MPU6050_6Axis_MotionApps20.h"
#if I2CDEV_IMPLEMENTATION == I2CDEV_ARDUINO_WIRE
#include "Wire.h"
#endif

MPU6050 mpu;
#define OUTPUT_READABLE_YAWPITCHROLL

bool blinkState = false;

bool dmpReady = false;  // set true if DMP init was successful
uint8_t mpuIntStatus;   // holds actual interrupt status byte from MPU
uint8_t devStatus;      // return status after each device operation (0 = success, !0 = error)
uint16_t packetSize;    // expected DMP packet size (default is 42 bytes)
uint16_t fifoCount;     // count of all bytes currently in FIFO
uint8_t fifoBuffer[64]; // FIFO storage buffer

// orientation/motion vars
Quaternion q;           // [w, x, y, z]         quaternion container
VectorInt16 aa;         // [x, y, z]            accel sensor measurements
VectorInt16 aaReal;     // [x, y, z]            gravity-free accel sensor measurements
VectorInt16 aaWorld;    // [x, y, z]            world-frame accel sensor measurements
VectorFloat gravity;    // [x, y, z]            gravity vector
float euler[3];         // [psi, theta, phi]    Euler angle container
float ypr[3];           // [yaw, pitch, roll]   yaw/pitch/roll container and gravity vector

// packet structure for InvenSense teapot demo
uint8_t teapotPacket[14] = { 
  '$', 0x02, 0,0, 0,0, 0,0, 0,0, 0x00, 0x00, '\r', '\n' };

volatile bool mpuInterrupt = false;     // indicates whether MPU interrupt pin has gone high
void dmpDataReady() {
  mpuInterrupt = true;
}
double angle_compass = 0;
double previous_angle = 0;
int enco1=18;
int enco2=19;
long left_c=0;
long right_c = 0;
Servo s1;//camera
Servo s2;//arm
Servo s3;//griper
config_1 my;
int speed=140;
int angle=0;
int ang1=0;
int ang2=0;
boolean forward_flag=true;
boolean backword_flag=true;
long timer = 0;
int delay_cal = 1500;
boolean flag_conection=false;
int distance_measured[10]={
  -1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
int angle_measured[10]={
  0,0,0,0,0,0,0,0,0,0};
int index_measured = 0;
int index_measured2 = 0;
int angle_lr = 0;
char state_ch='q';
void setup(){
  my.motor_init();
  my.led_init();
  my.button_init();
  s1.attach(6);
  s2.attach(4);
  s3.attach(5);
  //my.servo_init();
  //my.sonar_init();
  Serial3.begin(38400); //serial3
#if I2CDEV_IMPLEMENTATION == I2CDEV_ARDUINO_WIRE
  Wire.begin();
  TWBR = 24; // 400kHz I2C clock (200kHz if CPU is 8MHz)
#elif I2CDEV_IMPLEMENTATION == I2CDEV_BUILTIN_FASTWIRE
  Fastwire::setup(400, true);
#endif
  Serial.begin(9600);

  while (!Serial); // wait for Leonardo enumeration, others continue immediately
  Serial.println(F("Initializing I2C devices..."));
  mpu.initialize();

  // verify connection
  Serial.println(F("Testing device connections..."));
  Serial.println(mpu.testConnection() ? F("MPU6050 connection successful") : F("MPU6050 connection failed"));

  // wait for ready
  Serial.println(F("\nSend any character to begin DMP programming and demo: "));
  //while (Serial.available() && Serial.read()); // empty buffer
  //while (!Serial.available());                 // wait for data
  //while (Serial.available() && Serial.read()); // empty buffer again

  // load and configure the DMP
  Serial.println(F("Initializing DMP..."));
  devStatus = mpu.dmpInitialize();

  // supply your own gyro offsets here, scaled for min sensitivity
  mpu.setXGyroOffset(220);
  mpu.setYGyroOffset(76);
  mpu.setZGyroOffset(-85);
  mpu.setZAccelOffset(1788); // 1688 factory default for my test chip

  // make sure it worked (returns 0 if so)
  if (devStatus == 0) {
    // turn on the DMP, now that it's ready
    Serial.println(F("Enabling DMP..."));
    mpu.setDMPEnabled(true);

    // enable Arduino interrupt detection
    Serial.println(F("Enabling interrupt detection (Arduino external interrupt 0)..."));
    attachInterrupt(1, dmpDataReady, RISING);
    mpuIntStatus = mpu.getIntStatus();

    // set our DMP Ready flag so the main loop() function knows it's okay to use it
    Serial.println(F("DMP ready! Waiting for first interrupt..."));
    dmpReady = true;
    packetSize = mpu.dmpGetFIFOPacketSize();
  } 
  else {
    Serial.print(F("DMP Initialization failed (code "));
    Serial.print(devStatus);
    Serial.println(F(")"));
  }
  long time=millis();
  attachInterrupt(4,right,FALLING);
  attachInterrupt(5,left,FALLING);
  //delay(4000);
  //rotate(-5);
  s2.write(130);
  delay(1000);
  while(millis()-time<15000)
  {
    get_angle(); 
  }

  previous_angle = angle_compass;
  Serial.println(previous_angle);
  //delay(3000);
//  my.led_init();
  //my.led_on(1);
  timer = millis();

}
int var=0;
void loop(){
  if (Serial3.available()>0)  //serial3
  {
    flag_conection=true;
    char ch=Serial3.read();  //serial3
    //Serial3.println(ch);  //serial3
    if(ch == '?')
    {
      load_map_data(ch);
    }
    else
    {
      cmd(ch);
    }
    timer=millis();
  }
  if(millis()-timer>7000 && flag_conection)
  {
    //my.led_on(3);
    my.left_motor(0);
    my.right_motor(0);
    distance_measured[index_measured] = (right_c+left_c)/(2*665.5478);
    index_measured++;
    rotate(180);
    get_back();
    //my.led_on(-3);
    while(!(Serial3.available()>0));
  }

  //s1.write(40);
  //my.servo_write(4,10);
}
//
void get_back()
{
  int bias_angle = 0;
  //rotate(-3);
  for(int i = 0;i<index_measured;i++)
  {
    long time=millis();
    while(millis()-time<delay_cal)
    {
      get_angle(); 
    }
    previous_angle = angle_compass;

    double dist = distance_measured[index_measured-i-1];
    double target_count = dist*665.5478;
    double total_count = 0;
    left_c=0;
    right_c = 0;
    while(total_count < target_count)
    {
      int kp = 4;
      int error = angle_compass - previous_angle;
      //error = constrain(error,-20,20);
      my.left_motor(-speed-kp*error);
      my.right_motor(-speed+kp*error); 
      get_angle();
      total_count += (left_c+right_c)/2;
      left_c = 0;
      right_c = 0;
    }
    my.left_motor(0);
    my.right_motor(0);
    delay(1000);
    if(true)
    {
      rotate(angle_measured[index_measured-i-1]-bias_angle);
    }
    delay(500);
  }
}
void load_map_data(char ch)
{
  if(ch == '?')
  {
    my.led_on(2);
    while(!(Serial3.available()>0));
    byte arlength = Serial3.read();
    //Serial3.write(arlength);

    byte distance[arlength];
    int angle[arlength];
    for(int i = 0;i<arlength;i++)
    {
      while(!(Serial3.available()>0));
      distance[i] = Serial3.read();
      while(!(Serial3.available()>0));
      byte ang = Serial3.read();
      if(ang == ',')
      {
        while(!(Serial3.available()>0));
        angle[i] = Serial3.read();
      }
      else
      {
        angle[i] = -1*ang;
      }
    }
    int bias_angle = 0;
    //rotate(-3);
    for(int i = 0;i<arlength;i++)
    {
      long time=millis();
      while(millis()-time<delay_cal)
      {
        get_angle(); 
      }
      previous_angle = angle_compass;

      double dist = distance[i];
      double target_count = dist*665.5478;
      double total_count = 0;
      left_c=0;
      right_c = 0;
      while(total_count < target_count)
      {
        int kp = 4;
        int error = angle_compass - previous_angle;
        //error = constrain(error,-20,20);
        my.left_motor(-speed-kp*error);
        my.right_motor(-speed+kp*error); 
        get_angle();
        total_count += (left_c+right_c)/2;
        left_c = 0;
        right_c = 0;
      }
      my.left_motor(0);
      my.right_motor(0);
      delay(1000);
      if((i+1) < arlength)
      {
        rotate(angle[i+1]-bias_angle);
      }
      delay(500);
    }
    rotate(180);
    for(int i = 0;i<arlength;i++)
    {
      long time=millis();
      while(millis()-time<delay_cal)
      {
        get_angle(); 
      }
      previous_angle = angle_compass;

      double dist = distance[arlength-i-1];
      double target_count = dist*665.5478;
      double total_count = 0;
      left_c=0;
      right_c = 0;
      while(total_count < target_count)
      {
        int kp = 4;
        int error = angle_compass - previous_angle;
        //error = constrain(error,-20,20);
        my.left_motor(-speed-kp*error);
        my.right_motor(-speed+kp*error); 
        get_angle();
        total_count += (left_c+right_c)/2;
        left_c = 0;
        right_c = 0;
      }
      my.left_motor(0);
      my.right_motor(0);
      delay(1000);
      if((i+1)<arlength)
      {
        rotate(-angle[arlength-i-1]-bias_angle);
      }
      delay(500);
    }



    /////////////////




    //my.led_on(-2);
    Serial3.write('?');
  }
}
void cmd (char c)
{
  switch (c)
  {
  case 'f':
    if(state_ch != 'f')
    {
      state_ch='f';
      angle_measured[index_measured2] = angle_lr;
      index_measured2++;
      angle_lr=0;
    }
    if(forward_flag)
    {
      long time=millis();
      while(millis()-time<delay_cal)
      {
        get_angle(); 
      }
      previous_angle = angle_compass;
    }
    forward_flag=false;
    backword_flag=true;
    go_forward();
    Serial3.print('d');
    break;
  case 'b':
    if(state_ch != 'b')
    {
      state_ch='b';
    }
    if(backword_flag)
    {
      long time=millis();
      while(millis()-time<delay_cal)
      {
        get_angle(); 
      }
      previous_angle = angle_compass;      
    }
    forward_flag=true;
    backword_flag=false;
    go_backword();
    right_c = 0;
    left_c = 0;
    Serial3.print('d');
    break;
  case 'r':
    if(state_ch != 'r')
    {
      state_ch='r';
      distance_measured[index_measured] = (right_c+left_c)/(2*665.5478);
      index_measured++;
    }
    forward_flag=true;
    backword_flag=true;
    rotate(-10);
    angle_lr +=10;
    right_c = 0;
    left_c = 0;
    Serial3.print('d');
    break;
  case 'l':
    if(state_ch != 'l')
    {
      distance_measured[index_measured] = (right_c+left_c)/(2*665.5478);
      index_measured++;      
      state_ch='l';
    }
    forward_flag=true;
    backword_flag=true;
    rotate(10);
    angle_lr -=10;
    right_c = 0;
    left_c = 0;
    Serial3.print('d');
    break;
  case 's':
    my.left_motor(0);
    my.right_motor(0);
    break;
  case 'd':
    angle=angle+3;
    angle=constrain(angle,0,180);
    s1.write(angle);
    Serial3.print('d');
    break;
  case 'u':
    angle=angle-3;
    angle=constrain(angle,0,180);
    s1.write(angle);
    Serial3.print('d');
    break;
  case 'a':    //grip above
    ang1=ang1+3;
    ang1=constrain(ang1,85,140);
    s2.write(ang1);
    Serial3.print('d');
    break;
  case 'e':   //grip below
    ang1=ang1-3;
    ang1=constrain(ang1,85,140);
    s2.write(ang1);
    Serial3.print('d');
    break;
  case 'h':   //grab 
    ang2=ang2-3;
    ang2=constrain(ang2,60,140);
    s3.write(ang2);
    Serial3.print('d');
    break;
  case 'g':   //release
    ang2=ang2+3;
    ang2=constrain(ang2,60,140);
    s3.write(ang2);
    Serial3.print('d');
    break;
  case'p':   //speed plus
    speed=speed+10;
    speed=constrain(speed,80,255);
    Serial3.println(speed); //serial3  //show current speed
    break;
  case'm':   //speed minus
    speed=speed-10;
    speed=constrain(speed,80,255);
    Serial3.println(speed);  //serial3
    break;
  }
}       
void go_forward()
{
  int kp = 4;
  int error = angle_compass - previous_angle;
  //error = constrain(error,-20,20);
  my.left_motor(-speed-kp*error);
  my.right_motor(-speed+kp*error); 
  get_angle();
}
void go_backword()
{
  int kp = 4;
  int error = angle_compass - previous_angle;
  //error = constrain(error,-20,20);
  my.left_motor(speed+kp*error);
  my.right_motor(speed-kp*error); 
  get_angle();
}

void right()
{
  right_c++;
}
void left()
{
  left_c++;
}

void rotate(int degree)
{
  left_c=0;
  right_c = 0;
  delay(100);
  double count = abs(degree*98);//abs(degree * 147);
  //left_c=0;
  //right_c = 0;
  if(degree < 0)
  {    
    my.left_motor(speed);
    my.right_motor(-speed); 
  }
  else
  {    
    my.left_motor(-speed);
    my.right_motor(speed);
  }
  while(left_c < count || right_c < count)
  {
    delay(2);
  }
  my.left_motor(0);
  my.right_motor(0);
}


void get_angle()
{
  if (!dmpReady) return;
  while (!mpuInterrupt && fifoCount < packetSize) {
  }

  // reset interrupt flag and get INT_STATUS byte
  mpuInterrupt = false;
  mpuIntStatus = mpu.getIntStatus();

  // get current FIFO count
  fifoCount = mpu.getFIFOCount();
  if ((mpuIntStatus & 0x10) || fifoCount == 1024) {
    // reset so we can continue cleanly
    mpu.resetFIFO();
    Serial.println(F("FIFO overflow!"));
  } 
  else if (mpuIntStatus & 0x02) {
    // wait for correct available data length, should be a VERY short wait
    while (fifoCount < packetSize) fifoCount = mpu.getFIFOCount();
    mpu.getFIFOBytes(fifoBuffer, packetSize);
    fifoCount -= packetSize;

#ifdef OUTPUT_READABLE_YAWPITCHROLL
    // display Euler angles in degrees
    mpu.dmpGetQuaternion(&q, fifoBuffer);
    mpu.dmpGetGravity(&gravity, &q);
    mpu.dmpGetYawPitchRoll(ypr, &q, &gravity);
    angle_compass = 0.5*(ypr[0] * 180/M_PI)+0.5*(euler[0]*180/M_PI);
#endif

  }
}














