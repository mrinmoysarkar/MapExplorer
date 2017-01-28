//author: Mrinmoy Sarkar
//email:mrinmoy.pol@gmail.com
#ifndef CONFIG_h
#define CONFIG_h

#include <arduino.h>
#include <Servo.h>

#define left_motor_1  11
#define left_motor_2  10
#define right_motor_1 9
#define right_motor_2 8


#define button_home 22
#define button_up	23
#define button_down 24
#define button_ok	25
#define button_back	26

#define buzzer 	13


#define led_1 38
#define led_2 39
#define led_3 40
#define led_4 41
#define led_5 42


#define IR1 A0;
#define IR2 A1;
#define IR3 A2;
#define IR4 A3;
#define IR5 A5;
#define IR6 A6;
#define IR7 A7;


#define  servo_1 3
#define  servo_2 4
#define  servo_3 5
#define  servo_4 6
#define  servo_5 7
#define  servo_6 43



#define sonar_trig 	44
#define sonar_echo 	2


#define 	SD_card_cs 		53
#define 	SD_card_scl 	21
#define 	SD_card_sda 	20
#define 	SD_card_miso 	50
#define 	SD_card_mosi 	51
#define 	SD_card_sck 	52


#define HMC5883_WriteAddress 0x1E 
#define HMC5883_ModeRegisterAddress 0x02
#define HMC5883_ContinuousModeCommand 0x00
#define HMC5883_DataOutputXMSBAddress  0x03


class  config_1 {

private:
  int motor_speed;
  int led_number;
  int servo_number;
  int servo_position;

  Servo  myservo_1,  myservo_2,myservo_3,myservo_4,myservo_5,myservo_6;


public:
  void motor_init();
  void led_init();
  void button_init();
  void buzzer_init();
  void servo_init();
  void sonar_init();


  void left_motor(int motor_speed);
  void right_motor(int motor_speed);


  void led_on(int led_number );

  int button_pressed();

  void servo_write(int servo_number,int servo_position);

  long  get_distance();
  double read_angle();


};

#endif

