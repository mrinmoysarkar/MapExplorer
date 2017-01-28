import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.serial.*; 
import javax.swing.*; 
import org.gamecontrolplus.gui.*; 
import org.gamecontrolplus.*; 
import net.java.games.input.*; 
import javax.swing.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class cgr extends PApplet {

//Author: Mrinmoy Sarkar
//version 1.1
 
 




ControlIO control;
ControlDevice stick;

SecondApplet s;
float botx;
float boty;
float speed;
float theta;
float omg;
int state = 0;
char state_ch = 's';

Serial myPort;
Serial robot_cmd;
String input_port="COM16";
String output_port="COM4";//////
int xx1 = 0, xx2 = 50, xx3 = 100, xx4 = 150, xx5 = 200, xx6 = 250, xx7 = 300, xx8 = 350;
int yy1 = 0, yy2 = 50, yy3 = 100, yy4 = 150, yy5 = 200, yy6 = 250, yy7 = 300, yy8 = 350;
int y = 0;
int m = 0;
boolean flag = true;

float x1 = 150, y1=250, x2=250, y2=250, x3=200, y3=150;
float h1=150, k1=200, h2=250, k2=200;
float theta1=90, theta2=323, theta3=217;
float theta_left = 90, theta_right = 90;
float global_theta_left = 0, global_theta_right = 0;
float centrex = 200;
float centrey = 212.5f;
float radius =62.5f;
float radius_h = 50;
float r1 = 50, r2=111.8f, r3=70.71f;
float r4 = 111.8f, r5=50, r6=70.71f;

float camera_a = 0, camera_b=0;
float camera_value = 0;
int inc = 10;
int dec=10;

int col1=255, col2=255, col3=255, col4=255, col5=255, col6=255;

int col7 = 200, col8=100;
int col9 = 100, col10=200;
boolean field = true;
boolean feedback_rotate=true;
boolean flag_connected=true;
boolean manual = false;
boolean auto = false;
boolean clear_button = true;
boolean play_button = false;
boolean task_complete = false;
ArrayList<point_2int> diatance_angle;
point_2int p1, p2, p3;
public void setup()
{
  p1=new point_2int(0, 0);
  p2=new point_2int(0, 0);
  p3=new point_2int(0, 0);
  diatance_angle = new ArrayList<point_2int>();
  size(400, 400);
  background(0);
  println(Serial.list()); 
  // myPort = new Serial(this, input_port, 9600);
  //robot_cmd = new Serial(this, output_port, 38400);
  botx = 170;
  boty = 250;
  speed = 3;
  theta=90;
  omg = 10;
  PFrame f = new PFrame(507, 530);
  frame.setTitle("Robot Motion and Direction");
  f.setTitle("Robot In The Field");
  fill(0);
  f.setResizable(false);
  control = ControlIO.getInstance(this);
  stick = control.getMatchedDevice("moybot");
  if (stick == null) {
    println("No suitable device configured");
    //System.exit(-1); // End the program NOW!
  } else
  {
    stick.getButton("forward").plug(this, "keyReleased", ControlIO.ON_RELEASE);
    stick.getButton("backword").plug(this, "keyReleased", ControlIO.ON_RELEASE);
    stick.getButton("left").plug(this, "keyReleased", ControlIO.ON_RELEASE);
    stick.getButton("right").plug(this, "keyReleased", ControlIO.ON_RELEASE);

    stick.getButton("forward").plug(this, "Goforward", ControlIO.ON_PRESS);
    stick.getButton("backword").plug(this, "Gobackword", ControlIO.ON_PRESS);
    stick.getButton("left").plug(this, "Goleft", ControlIO.ON_PRESS);
    stick.getButton("right").plug(this, "Goright", ControlIO.ON_PRESS);
    stick.getButton("showfield").plug(this, "Showfield", ControlIO.ON_PRESS);
    stick.getButton("zoomin").plug(this, "Zoomin", ControlIO.ON_PRESS);
    stick.getButton("zoomin").plug(this, "keyReleased", ControlIO.ON_RELEASE);
    stick.getButton("zoomout").plug(this, "Zoomout", ControlIO.ON_PRESS);
    stick.getButton("zoomout").plug(this, "keyReleased", ControlIO.ON_RELEASE);

    stick.getButton("shiftleft").plug(this, "camera_up", ControlIO.ON_PRESS);
    stick.getButton("shiftleft").plug(this, "keyReleased", ControlIO.ON_RELEASE);

    stick.getButton("shiftright").plug(this, "camera_down", ControlIO.ON_PRESS);
    stick.getButton("shiftright").plug(this, "keyReleased", ControlIO.ON_RELEASE);
  }
  //frameRate(20);
}
public void draw()
{
  if (!flag_connected)
  {
    //robot_cmd.write("@");//check if device is alive
  }

  if (manual)
  {
    /*
  if (myPort.available() > 0) {
     char inByte = myPort.readChar();
     println(inByte);
     if (inByte=='F')
     {
     up();
     } else if (inByte=='B')
     {
     down();
     } else if (inByte=='L')
     {
     left();
     } else if (inByte=='R')
     {
     right();
     } else if (inByte=='G')
     {
     grab();
     } else if (inByte=='M')
     {
     release();
     } else if (inByte=='U')
     {
     camera_up();
     } else if (inByte=='D')
     {
     camera_down();
     }
     }
     */
    switch(state_ch)
    {
    case 'f':
      //if (feedback_rotate)
      {
        //feedback_rotate=false;
        Goforward();
      }
      break;
    case 'b':
      // if (feedback_rotate)
      {
        //feedback_rotate=false;
        Gobackword();
      }
      break;
    case 'l':
      println("left is called");
      //if (feedback_rotate)
      {
        //feedback_rotate=false;
        Goleft();
      }
      break;
    case 'r':
      //if (feedback_rotate)
      {
        //feedback_rotate=false;
        Goright();
      }
      break;
    case '-':
      Zoomout();
      break;
    case '=':
      Zoomin();
      break;
    case 'i':
      shiftrr();
      break;
    case 'k':
      shiftrl();
      break;
    case 'j':
      shiftlr();
      break;
    case 'o':
      shiftll();
      break;
    case '9':
      camera_up();
      break;
    case '0':
      camera_down();
      break;/*
    case '8':
       grab();
       break;
       case '7':
       release();
       break;
       case '5':
       hands_up_boby();
       break;
       case '6':
       hands_down_boby();
       break;*/
    }
    background(0);

    xgrid();
    ygrid();

    fill(col1, col2, 0);
    ellipse(200, 212.5f, 125, 125);

    fill(col3, 0, col3);
    ellipse(200, 212.5f, 70, 70);
    fill(col4, col5, col6);
    triangle(x1, y1, x2, y2, x3, y3);
    fill(col5, 100, col6);
    ellipse(200, 212.5f, camera_a, camera_b);
    stroke(255, col1, col2);
    strokeWeight(7); 
    line(x1, y1, h1, k1);
    line(x2, y2, h2, k2);
    noStroke();
    /*
  fill(col7, col7, 0);
     textSize(32);
     text("Speed+", 50, 30); 
     fill(col8, 0, col8);
     textSize(32);
     text("Speed-", 250, 30);
     fill(col9, 0, 0);
     ellipse(380, 100, 30, 30);
     fill(0, col10, 0);
     ellipse(380, 200, 30, 30);
     */
    if (feedback_rotate && !flag_connected)
    {
      fill(0, 255, 0);
      ellipse(380, 200, 30, 30);
    } else
    {
      fill(255, 0, 0);
      ellipse(380, 200, 30, 30);
    }
  }
  if (flag_connected)
  {
    fill(0);
    rect(0, 0, width, 30);
    for (int i=0; i<Serial.list ().length; i++)
    {   
      fill(255, 255, 255);
      textSize(10);
      text(Serial.list()[i], 2+40*i, 15);
    }
  }
  if (!flag_connected && !auto && !manual)
  {
    background(0);
    fill(0, 255, 0);
    rect(45, 115, 130, 140);
    fill(255, 255, 255);
    textSize(180);
    text("A", 50, 250);
    fill(0, 0, 250);
    rect(217, 115, 130, 140);
    fill(255, 255, 255);
    textSize(180);
    text("M", 25+180, 250);
  }
  if (auto)
  {
    if (clear_button)
    {
      background(20, 200, 160);
      clear_button = false;
    }
    fill(200, 100, col7);
    rect(5, 5, 50, 50);
    fill(255, 255, 255);
    textSize(30);
    text("C", 20, 35);
    fill(100, col7, 100);
    rect(75, 5, 50, 50);
    fill(255, 255, 255);
    textSize(30);
    text("G", 90, 35);
  }
  s.setGhostCursor(botx, boty, theta, state);
  get_user_input();
}
public void get_user_input()
{
  int g=(int)map(stick.getSlider("griprelease").getValue(), -1, 1, -100.0f, 100.0f);
  int h=(int)map(stick.getSlider("cameraupdown").getValue(), -1, 1, -100.0f, 100.0f);

  if (g<0 )
  {
    release();
  } else if (g>0)
  {
    grab();
  }
  if (h<0)
  {
    println("hands down");
    hands_down_boby();
  } else if (h>0)
  {
    println("hands up"); 
    hands_up_boby();
  }
}
public void serialEvent(Serial p) { 
  int data = p.read();
  println("Serial Data: "+data);
  feedback_rotate=true;
  if (data == '?')
  {
    println("Serial Data: ?");
    clear_button = true;
    play_button = false;
    task_complete = false;
    p1.x=0;
    p1.y=0;
    p2.x=0;
    p2.y=0;
    p3.x=0;
    p3.y=0;
    col7 = 200;
    for (int i = 0; i<diatance_angle.size (); i++)
    {
      diatance_angle.remove(i);
    }
  }
}
public void mousePressed()
{
  if (auto)
  {
    if (mouseX>5 && mouseX<55 && mouseY>5 && mouseY<55 && !play_button)
    {
      clear_button = true;
      println("clear");
      p1.x=0;
      p1.y=0;
      p2.x=0;
      p2.y=0;
      p3.x=0;
      p3.y=0;
      for (int i = 0; i<diatance_angle.size (); i++)
      {
        diatance_angle.remove(i);
      }
    } else if (mouseX>75 && mouseX<125 && mouseY>5 && mouseY<55 && !play_button)
    {
      play_button = true;
      col7=100;
      //send data
      robot_cmd.write('?');
      int data_length = diatance_angle.size();
      robot_cmd.write(PApplet.parseByte(data_length));
      println("data length: "+PApplet.parseByte(data_length));
      for (int i = 0; i<diatance_angle.size (); i++)
      {
        int dis = diatance_angle.get(i).x;
        int ang = diatance_angle.get(i).y;
        println("distance = "+dis+" angle = "+ang);
        robot_cmd.write(PApplet.parseByte(dis));
        if (ang<0)
        {
          ang = abs(ang);
          robot_cmd.write(",");
          robot_cmd.write(PApplet.parseByte(ang));
        } else
        {
          robot_cmd.write(PApplet.parseByte(ang));
        }
      }
    } else if (!play_button)
    {
      fill(0, 0, 255);
      ellipse(mouseX, mouseY, 10, 10);
      p1.x=p2.x;
      p1.y=p2.y;
      p2.x=p3.x;
      p2.y=p3.y;
      p3.x=mouseX;
      p3.y=mouseY;
      println("-----------------------------------------------");
      println("p1.x = "+p1.x+" p1.y = "+p1.y);
      println("p2.x = "+p2.x+" p2.y = "+p2.y);
      println("p3.x = "+p3.x+" p3.y = "+p3.y);
      if (p2.x !=0 || p2.y !=0)
      {
        stroke(255, 100, 150);
        strokeWeight(3); 
        line(p2.x, p2.y, p3.x, p3.y);
        int x = (p2.x+p3.x)/2;
        int y = (p2.y+p3.y)/2;
        noStroke();
        fill(0);
        double m = (double)(p2.x-p1.x)/(p1.y-p2.y);
        double c = p2.y - m*p2.x;
        double mdas = (double)(p1.y-p3.y)/(p1.x-p3.x);
        double cdas = p1.y-mdas*p1.x;
        double x_ = (c-cdas)/(mdas-m);
        double y_=m*x_+c;
        int dist=(int)(Math.sqrt((p2.x-p3.x)*(p2.x-p3.x) + (p2.y-p3.y)*(p2.y-p3.y))*0.6375f);
        double d1 = (Math.sqrt((p1.x-x_)*(p1.x-x_) + (y_-p1.y)*(y_-p1.y))*0.6375f);
        double d2 = (Math.sqrt((p1.x-p3.x)*(p1.x-p3.x) + (p1.y-p3.y)*(p1.y-p3.y))*0.6375f);
        int angle = 0;
        if (p1.x !=0 || p1.y!=0)
        {
          double m1 = (double)(p2.y-p1.y)/((double)(p2.x-p1.x));
          double m2 = (double)(p2.y-p3.y)/((double)(p2.x-p3.x));
          println("m1 = "+m1);
          println("m2 = "+m2);
          double an = degrees(atan((float)((m2-m1)/(1+m1*m2))));
          println("an = "+an);
          if (d2 > d1)
          {
            angle = (int)an;
          } else
          {
            if (an <=0)
            {
              angle = (int)(180+an);
            } else
            {
              angle = (int)(an-180);
            }
          }
          println(angle+" degree");
        }
        diatance_angle.add(new point_2int((int)dist, angle));
        textSize(10);
        text(dist+" cm", x, y);
      }
    }
  }
  if (flag_connected)
  {
    for (int i=0; i<Serial.list ().length; i++)
    {
      if (mouseX>(2+40*i) && mouseX<(2+40*(i+1)) && mouseY>5 && mouseY<30)
      {
        println(Serial.list()[i]);
        try
        {
          robot_cmd = new Serial(this, Serial.list()[i], 38400);
          flag_connected=false;
        }
        catch(Exception ee)
        {
          println("Can not open port");
        }
      }
    }
  }
  if (!flag_connected && !auto && !manual)
  {
    if (mouseX>217 && mouseX<(217+130) && mouseY>115 && mouseY<(115+140))
    {
      manual = true;
    }
    if (mouseX>45 && mouseX<(45+130) && mouseY>115 && mouseY<(115+140))
    {
      auto = true;
    }
  }
}
public void keyReleased()
{
  if (!flag_connected && manual)
  {
    robot_cmd.write("s");
  }
  state = 0;
  state_ch='s';
}
public void Showfield()
{
  field = !field;
  s.setfl(field);
}
public void Zoomin()
{
  state_ch = '=';
  s.zoom(1);
}
public void Zoomout()
{
  state_ch = '-';
  s.zoom(-1);
}
public void shiftrr()
{
  state_ch ='i';

  s.settx(5);
}
public void shiftrl()
{
  state_ch ='k';
  s.settx(-5);
}
public void shiftlr()
{
  state_ch ='j';
  s.setty(-5);
}
public void shiftll()
{
  state_ch ='o';
  s.setty(5);
}
public void keyPressed()
{
  println("keypressed");
  if ( key=='f')
  {
    field = !field;
    s.setfl(field);
  }
  if (key == '=')
  {
    s.zoom(1);
  }
  if (key == '-')
  {
    s.zoom(-1);
  }
  if (key == 'p')
  {
    s.show_total_path();
  }
  if (key == 'i')
  {
    s.setty(-5);
  }
  if (key == 'k')
  {
    s.setty(5);
  }  
  if (key == 'j')
  {
    s.settx(-5);
  }  
  if (key == 'l')
  {
    s.settx(5);
  }
  if (!field)
  {
    return;
  }
  if (key == 'a' || key == 'A')
  {
    col9 = PApplet.parseInt(random(256));
    ////robot_cmd.write('a');
  } else if (key == 'b' || key == 'B')
  {
    col10 = PApplet.parseInt(random(256));
    ////robot_cmd.write('e');
  } else if (key == 'g' || key == 'G')
  {
    grab();
    println("G pressed");
    ////robot_cmd.write("g");
  } else if (key == 'r' || key == 'R')
  {
    release();
    println("R pressed");
    ////robot_cmd.write("h");
  } else if (key == 'u' || key == 'U')
  {
    camera_up();
    println("U pressed");
    ////robot_cmd.write("u");
  } else if (key == 'd' || key == 'D')
  {
    camera_down();
    println("D pressed");
    ////robot_cmd.write("d");
  }
  if (key == CODED)
  {
    if (keyCode == UP)
    {
      Goforward();
    } else if (keyCode == DOWN)
    {
      Gobackword();
    } else if (keyCode == LEFT)
    {
      Goleft();
    } else if (keyCode == RIGHT)
    {
      Goright();
    }
  }
}
public void Goright()
{
  if (!flag_connected && manual)
  {
    if (field)
    {
      if (feedback_rotate)
      {
        state_ch='r';
        right();
        println("RIGHT arrow pressed");
        robot_cmd.write("r");
        theta += omg;
        theta = theta % 360;
        println("Theta: "+theta);
        feedback_rotate=false;
      }
    } else
    {
      shiftrr();
    }
  }
}
public void Goleft()
{
  if (!flag_connected && manual)
  {
    if (field)
    {
      if (feedback_rotate)
      {
        state_ch='l';
        left();
        println("LEFT arrow pressed");
        robot_cmd.write("l");
        theta -= omg;
        theta = theta % 360; 
        println("Theta: "+theta);
        feedback_rotate=false;
      }
    } else
    {
      shiftrl();
    }
  }
}
public void Gobackword()
{
  if (!flag_connected && manual)
  {
    if (field)
    {
      if (feedback_rotate)
      {
        state_ch='b';
        down();
        println("DOWN arrow pressed");
        robot_cmd.write("b");
        float dx = speed*cos(radians(theta));
        float dy = speed*sin(radians(theta));
        botx += dx;
        boty += dy;
        state = 1;
        feedback_rotate=false;
      }
    } else
    {
      shiftll();
    }
  }
}
public void Goforward()
{
  if (!flag_connected && manual)
  {
    if (field)
    {
      if (feedback_rotate)
      {
        state_ch='f';
        up();
        println("UP arrow pressed");
        robot_cmd.write("f");
        float dx = speed*cos(radians(theta));
        float dy = speed*sin(radians(theta));
        botx -= dx;
        boty -= dy;
        state = -1;
        feedback_rotate=false;
      }
    } else
    {
      shiftlr();
    }
  }
}

public void release()
{
  if (!flag_connected && manual && field && feedback_rotate)
  {
    robot_cmd.write('h');
    rotate_hand(-5);
    println("in release");
    //state_ch='7';
    feedback_rotate = false;
  }
}
public void grab()
{
  if (!flag_connected && manual && field && feedback_rotate)
  {
    robot_cmd.write('g');
    rotate_hand(5);
    println("in grab");
    //state_ch='8';
    feedback_rotate = false;
  }
}

public void camera_down()
{
  if (!flag_connected && manual && field && feedback_rotate)
  {
    robot_cmd.write('d');
    camera_value--;
    camera_value = constrain(camera_value, -60, 60);
    rotate_camera(camera_value);
    println("in camera Down");
    state_ch='0';
    feedback_rotate = false;
  }
}
public void camera_up()
{
  if (!flag_connected && manual && field && feedback_rotate)
  {
    robot_cmd.write('u');
    camera_value++;
    camera_value = constrain(camera_value, -60, 60);
    rotate_camera(camera_value);
    println("in camera up");
    state_ch='9';
    feedback_rotate = false;
  }
}

public void hands_up_boby()
{
  if (!flag_connected && manual && field && feedback_rotate)
  {
    robot_cmd.write('a');
    println("in hands up boby");
    feedback_rotate=false;
  }
}

public void hands_down_boby()
{
  if (!flag_connected && manual && field && feedback_rotate)
  {
    robot_cmd.write('e');
    println("in hands down boby");
    feedback_rotate=false;
  }
}

public void up()
{
  col1 = PApplet.parseInt(random(256));
  col2 = PApplet.parseInt(random(256));
  col3 = PApplet.parseInt(random(256));
  col4 = PApplet.parseInt(random(256));
  col5 = PApplet.parseInt(random(256));
  col6 = PApplet.parseInt(random(256));
  float theta=theta1;
  if (theta1<0)
  {
    theta1 += 360;
  }
  if (theta1>0 && theta1<90)
  {
    change_xgrid_plus();
    change_ygrid_minus();
  } else if (theta1>90 && theta1<180)
  {
    change_xgrid_plus();
    change_ygrid_plus();
  } else if (theta1>180 && theta1<270)
  {
    change_xgrid_minus();
    change_ygrid_plus();
  } else if (theta1>270 && theta1<360)
  {
    change_xgrid_minus();
    change_ygrid_minus();
  } else if (theta1 == 0)
  {
    change_ygrid_minus();
  } else if (theta1 == 180)
  {
    change_ygrid_plus();
  } else if (theta1 == 270)
  {
    change_xgrid_minus();
  } else if (theta1 == 90)
  {
    change_xgrid_plus();
  }
  theta1 = theta;
}
public void down()
{  
  col1 = PApplet.parseInt(random(256));
  col2 = PApplet.parseInt(random(256));
  col3 = PApplet.parseInt(random(256));
  col4 = PApplet.parseInt(random(256));
  col5 = PApplet.parseInt(random(256));
  col6 = PApplet.parseInt(random(256));
  float theta=theta1;
  if (theta1<0)
  {
    theta1 += 360;
  }
  if (theta1>0 && theta1<90)
  {
    change_xgrid_minus();
    change_ygrid_plus();
  } else if (theta1>90 && theta1<180)
  {

    change_xgrid_minus();
    change_ygrid_minus();
  } else if (theta1>180 && theta1<270)
  {
    change_xgrid_plus();
    change_ygrid_minus();
  } else if (theta1>270 && theta1<360)
  {
    change_xgrid_plus();
    change_ygrid_plus();
  } else if (theta1 == 0)
  {
    change_ygrid_plus();
  } else if (theta1 == 180)
  {
    change_ygrid_minus();
  } else if (theta1 == 270)
  {
    change_xgrid_plus();
  } else if (theta1 == 90)
  {
    change_xgrid_minus();
  }
  theta1 = theta;
}
public void left()
{
  rotate_robot(-omg);
}

public void right()
{
  rotate_robot(omg);
}

public void change_xgrid_plus()
{

  xx1+=inc;
  xx1=xx1>400?0:xx1;

  xx2+=inc;
  xx2=xx2>400?0:xx2;

  xx3+=inc;
  xx3=xx3>400?0:xx3;

  xx4+=inc;
  xx4=xx4>400?0:xx4;

  xx5+=inc;
  xx5=xx5>400?0:xx5;

  xx6+=inc;
  xx6=xx6>400?0:xx6;

  xx7+=inc;
  xx7=xx7>400?0:xx7;

  xx8+=inc;
  xx8=xx8>400?0:xx8;
}
public void xgrid()
{

  fill(255, 0, 0);
  rect(0, xx1, width, 4);

  fill(255, 255, 0);
  rect(0, xx2, width, 4);

  fill(255, 0, 255);
  rect(0, xx3, width, 4);

  fill(0, 255, 255);
  rect(0, xx4, width, 4);

  fill(0, 255, 0);
  rect(0, xx5, width, 4);

  fill(0, 0, 255);
  rect(0, xx6, width, 4);

  fill(100, 255, 0);
  rect(0, xx7, width, 4);

  fill(100, 0, 255);
  rect(0, xx8, width, 4);
}
public void change_xgrid_minus()
{
  xx1-=dec;
  xx1=xx1<0?400:xx1;

  xx2-=dec;
  xx2=xx2<0?400:xx2;

  xx3-=dec;
  xx3=xx3<0?400:xx3;

  xx4-=dec;
  xx4=xx4<0?400:xx4;

  xx5-=dec;
  xx5=xx5<0?400:xx5;

  xx6-=dec;
  xx6=xx6<0?400:xx6;

  xx7-=dec;
  xx7=xx7<0?400:xx7;

  xx8-=dec;
  xx8=xx8<0?400:xx8;
}
public void change_ygrid_plus()
{

  yy1+=inc;
  yy1=yy1>400?0:yy1;

  yy2+=inc;
  yy2=yy2>400?0:yy2;

  yy3+=inc;
  yy3=yy3>400?0:yy3;

  yy4+=inc;
  yy4=yy4>400?0:yy4;

  yy5+=inc;
  yy5=yy5>400?0:yy5;

  yy6+=inc;
  yy6=yy6>400?0:yy6;

  yy7+=inc;
  yy7=yy7>400?0:yy7;

  yy8+=inc;
  yy8=yy8>400?0:yy8;
}
public void ygrid()
{

  fill(255, 0, 0);
  rect(yy1, 0, 4, height);

  fill(255, 255, 0);
  rect(yy2, 0, 4, height);

  fill(255, 0, 255);
  rect(yy3, 0, 4, height);

  fill(0, 255, 255);
  rect(yy4, 0, 4, height);

  fill(0, 255, 0);
  rect(yy5, 0, 4, height);

  fill(0, 0, 255);
  rect(yy6, 0, 4, height);

  fill(100, 255, 0);
  rect(yy7, 0, 4, height);

  fill(100, 0, 255);
  rect(yy8, 0, 4, height);
}
public void change_ygrid_minus()
{
  yy1-=dec;
  yy1=yy1<0?400:yy1;

  yy2-=dec;
  yy2=yy2<0?400:yy2;

  yy3-=dec;
  yy3=yy3<0?400:yy3;

  yy4-=dec;
  yy4=yy4<0?400:yy4;

  yy5-=dec;
  yy5=yy5<0?400:yy5;

  yy6-=dec;
  yy6=yy6<0?400:yy6;

  yy7-=dec;
  yy7=yy7<0?400:yy7;

  yy8-=dec;
  yy8=yy8<0?400:yy8;
}

public void rotate_robot(float degree)
{
  theta1 -= degree;
  theta2 -= degree; 
  theta3 -= degree;
  theta1 %=360;
  theta2 %=360;
  theta3 %=360;


  x3 = centrex+radius*cos(radians(theta1));
  y3 = centrey-radius*sin(radians(theta1));

  x2 = centrex+radius*cos(radians(theta2));
  y2 = centrey-radius*sin(radians(theta2));

  x1 = centrex+radius*cos(radians(theta3));
  y1 = centrey-radius*sin(radians(theta3));


  float c1 = r2*r2 - r1*r1 - x2*x2 + x1*x1 - y2*y2 + y1*y1;
  float c2 = r3*r3 - r1*r1 - x3*x3 + x1*x1 - y3*y3 + y1*y1;
  float b1 = 2*y1 - 2*y2;
  float b2 = 2*y1 - 2*y3;
  float a1 = 2*x1 -2*x2;
  float a2 = 2*x1 -2*x3;

  float det = a1*b2 - a2*b1;
  float detx = c1*b2 - c2*b1;
  float dety = a1*c2 - a2*c1;

  h1 = detx/det;
  k1 = dety/det;

  c1 = r5*r5 - r4*r4 - x2*x2 + x1*x1 - y2*y2 + y1*y1;
  c2 = r6*r6 - r4*r4 - x3*x3 + x1*x1 - y3*y3 + y1*y1;

  det = a1*b2 - a2*b1;
  detx = c1*b2 - c2*b1;
  dety = a1*c2 - a2*c1;

  h2 = detx/det;
  k2 = dety/det;
  //float th = theta1<0?(360+theta1):theta1;
  theta_left = global_theta_left+theta1;//-90;
  theta_right = global_theta_right+theta1;//-90;
  theta_left %=360;
  theta_right %=360;
  //println("theta1:"+theta1);
}
public void rotate_hand(float degree)
{
  theta_left -= degree;
  theta_right += degree;

  theta_left %=360;
  theta_right %=360;

  h1 = x1+radius_h*cos(radians(theta_left));
  k1 = y1-radius_h*sin(radians(theta_left));

  h2 = x2+radius_h*cos(radians(theta_right));
  k2 = y2-radius_h*sin(radians(theta_right));

  r2 = sqrt((h1 - x2)*(h1 - x2) + (k1 - y2)*(k1 - y2));
  r3 = sqrt((h1 - x3)*(h1 - x3) + (k1 - y3)*(k1 - y3));

  r4 = sqrt((h2 - x1)*(h2 - x1) + (k2 - y1)*(k2 - y1));
  r6 = sqrt((h2 - x3)*(h2 - x3) + (k2 - y3)*(k2 - y3));

  global_theta_left = theta_left-theta1;//<0?360+theta_left:theta_left;
  global_theta_right = theta_right-theta1;//<0?360+theta_right:theta_right;
  //println("theta_left:"+theta_left);
  //println("theta_right:"+theta_right);
}

public void rotate_camera(float value)
{

  if (value<0)
  {
    camera_a = abs(value);
    camera_b = 10;
  } else if (value>0)
  {
    camera_a = 10;
    camera_b = abs(value);
  }
}
public class PFrame extends JFrame {

  public PFrame(int width, int height) 
  {
    setBounds(100, 100, width, height);
    s = new SecondApplet(width, height);
    add(s);
    s.init();
    show();
  }
}
public class point_2int
{
  public int x;
  public int y;
  public point_2int(int x, int y)
  {
    this.x = x;
    this.y = y;
  }
}
//void destroy()
//{
//  println("destroy");
//}
//void dispose()
//{
//  println("dispose");
//}
public void exit () {
  PGraphics pg;
  int w=720;
  int h=720;
  pg = createGraphics(w, h);
  println( "exit" );
  //createOutput("demo/outputImage.png");
  s.show_total_path();
  int len = s.save_total_path(pg, w, h);
  if (len>1)
  {
    String filename = year()+"_"+month()+"_"+day()+"__"+hour()+"_"+minute()+"_"+second();
  
    pg.save("ALL_MAP/"+filename+".png");
  }
  super.exit();
}


public class SecondApplet extends PApplet
{
  float w, h;
  float gridx, gridy, gridmx, gridmy;
  float ghostX=0, ghostY=0;
  float ghostXp=0, ghostYp=0;
  float theta;
  ArrayList<point_2> pointlist;
  boolean savedata = true;
  boolean fl =true;
  float scale_value = 1;
  float translatex;
  float translatey;
  float temx;
  float temy;
  int state = 0;
  float previous_ang = -10;
  int previous_state = -10;
  float maxx= 0;
  float minx = 1000;
  float maxy= 0;
  float miny = 1000;
  PShape path;
  float tem = 0;
  float tx = 0;
  float ty = 0;
  float total_path = 0;
  float x=0, y=0;
  public SecondApplet(float width, float height)
  {
    w = width;
    h = height;
    gridx=w;
    gridy=h;
    gridmx = 0;
    gridmy = 0;
    theta = -1;
    pointlist = new ArrayList<point_2>();
    translatex =0;
    translatey =0; 
    temx=0;
    temy=0;
  }
  public void setup()
  {
    //size(int(w), int(h), P2D);
    background(255);
    noStroke();
  }

  public void draw() 
  {
    if (!fl)
    {
      background(0, 255, 0);
      translate(tx, ty);
      scale(scale_value);
      show_total_path();
      //shape(path);
      total_path *= 2*.115f;
      String s= "Net Distance Covered: "+total_path+"cm";
      fill(0);
      textSize(36);
      text(s, 20, 250);
    }
    if (fl)
    {
      scale_value =1;
      background(255);
      //change_grid();
      trans();
      scale(scale_value); 
      translate(translatex/scale_value, translatey/scale_value);
      //draw_path();
      int nox = PApplet.parseInt(w/(50*scale_value));
      for (int i = 0; i<=nox; i++)
      {
        stroke(0, 0, 255);
        line(i*50-translatex/scale_value, -translatey/scale_value, i*50-translatex/scale_value, w/scale_value-translatey/scale_value);
      }
      int noy=PApplet.parseInt(h/(50*scale_value));
      for (int i = 0; i<=noy; i++)
      {
        stroke(0, 0, 255);
        line(-translatex/scale_value, i*50-translatey/scale_value, h/scale_value-translatex/scale_value, i*50-translatey/scale_value);
      }

      //fill(0, 255, 0);
      //ellipse((w/2-3)/scale_value, (h/2-15)/scale_value, 10, 10);
      //int col = int(random(100, 255)); 
      float col1 = PApplet.parseInt(random(150, 255));
      float col2 = PApplet.parseInt(random(20, 60));
      float col3 = PApplet.parseInt(random(10, 60));
      fill(col1, col2, col3); 
      stroke(col1, col2, col3);
      ellipse(30, 0, 10, 10);//ghostX/scale_value, ghostY/scale_value, 5, 5);
    }
  }

  public void change_grid()
  {

    if ((abs(abs(ghostX)-abs(translatex)) > (w-50) || abs(abs(ghostX)-abs(translatex)) < 50) || (abs(abs(ghostY)-abs(translatey)) > (h-50) || abs(abs(ghostY)-abs(translatey)) < 50))
    {
      if (state == 1)
      {
        translatex -= 5*cos(radians(theta));
        translatey -= 5*sin(radians(theta));
      } else if (state == -1)
      {
        translatex += 5*cos(radians(theta));
        translatey += 5*sin(radians(theta));
      }
    }
  }

  public void trans()
  {
    float temp = theta;
    if (theta <0)
    {
      theta+=360;
    }
    if (x>(w-50) && ((theta <=90 && theta >=0) || (theta <=360 && theta >=270)))
    {
      x-=50*cos(radians(theta));
      y -= 50*sin(radians(theta));
    } else if (x>(w-50) && ((theta >=90 && theta <=180) || (theta <=270 && theta >=180)))
    {
      x+=50*cos(radians(theta));
      y += 50*sin(radians(theta));
    } else if (x<5 && ((theta <=90 && theta >=0) || (theta <=360 && theta >=270)))
    {
      x+=50*cos(radians(theta));
      y += 50*sin(radians(theta));
    } else if (x<5 && ((theta >=90 && theta <=180) || (theta <=270 && theta >=180)))
    {
      x-=50*cos(radians(theta));
      y -= 50*sin(radians(theta));
    } else if (y>(h-30) && ((theta <=90 && theta >=0) || (theta <=180 && theta >=90)))
    {
      x-=50*cos(radians(theta));
      y -= 50*sin(radians(theta));
    } else if (y>(h-30) && ((theta >=180 && theta <=270) || (theta <=360 && theta >=270)))
    {
      x+=50*cos(radians(theta));
      y += 50*sin(radians(theta));
    } else if (y<20 && ((theta <=90 && theta >=0) || (theta <=180 && theta >=90)))
    {
      x+=50*cos(radians(theta));
      y += 50*sin(radians(theta));
    } else if (y<20 && ((theta >=180 && theta <=270) || (theta <=360 && theta >=270)))
    {
      x-=50*cos(radians(theta));
      y -= 50*sin(radians(theta));
    }
    theta=temp;
    translatex = x;
    translatey = y;
  }

  public void zoom(int scal)
  {
    if (scal == 1)
    {
      scale_value+=.01f;
    } else if (scal==-1)
    {
      scale_value+=-.01f;
    }
  }
  public void show_total_path()
  {
    int len = pointlist.size();
    println("arraylength:"+len);
    if (len>=1)
    {

      if (previous_ang == theta && state == previous_state)
      {
        pointlist.remove(len-1);
      }
      pointlist.add(new point_2(ghostX, ghostY));

      previous_ang = theta;
      previous_state = state;


      float x1 = pointlist.get(0).x;
      float y1 = pointlist.get(0).y;
      fill(0, 0, 255);
      stroke(0, 0, 255);
      ellipse(x1, y1, 30, 30);
      textSize(32);
      fill(255, 242, 0);
      text("0", x1, y1);
      smooth();
      // path = createShape();
      // path.beginShape();
      //path.noFill();
      //path.stroke(255, 0, 0);
      //path.strokeWeight(5);
      total_path = 0;
      int k = 1;
      for (int i = 1; i<len; i++)
      {
        float x2 = pointlist.get(i).x;
        float y2 = pointlist.get(i).y;
        float distance = 0;
        if (i!=0)
        {
          distance = sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
          total_path += distance;
        }
        if (distance>40)
        {
          fill(0, 0, 255);
          stroke(0, 0, 255);
          ellipse(x2, y2, 30, 30);
        }
        stroke(255, 0, 0);
        strokeWeight(5);
        line(x1, y1, x2, y2);

        if (distance>40)
        {
          int distata = (int)(distance * 2*.115f);
          String s= distata+"cm";
          fill(0);
          textSize(16);
          text(s, x2+25, y2);
          strokeWeight(10);
          textSize(32);
          fill(255, 242, 0);
          text(k, x2, y2);
          k++;
          strokeWeight(1);
        }
        x1=x2;
        y1=y2;
        ////////

        ////
        //path.vertex(x2, y2);
      }
      //path.endShape();
    }
  }
  public void draw_path()
  {
    int len = pointlist.size();
    println("arraylength:"+len);
    if (len>=1)
    {
      if (previous_ang == theta && state == previous_state)
      {
        pointlist.remove(len-1);
      }
      pointlist.add(new point_2(ghostX, ghostY));
      maxx=ghostX>maxx?ghostX:maxx;
      minx=ghostX<minx?ghostX:minx;
      maxy=ghostY>maxy?ghostY:maxy;
      miny=ghostY<miny?ghostY:miny;
      previous_ang = theta;
      previous_state = state;
      float x1 = pointlist.get(0).x;
      float y1 = pointlist.get(0).y;
      for (int i = 1; i<len; i++)
      {
        float x2 = pointlist.get(i).x;
        float y2 = pointlist.get(i).y;
        stroke(255, 0, 0);
        strokeWeight(5);
        line(x1/scale_value, y1/scale_value, x2/scale_value, y2/scale_value);
        x1 = x2;
        y1 = y2;
      }
    }
    strokeWeight(1);
  }



  public void setGhostCursor(float ghostX, float ghostY, float theta, int state) 
  {
    this.state = state;
    if (theta != this.theta && savedata)
    {
      pointlist.add(new point_2(ghostX, ghostY));
      maxx=ghostX>maxx?ghostX:maxx;
      minx=ghostX<minx?ghostX:minx;
      maxy=ghostY>maxy?ghostY:maxy;
      miny=ghostY<miny?ghostY:miny;
      println("Changing Direction");
      this.theta = theta;
      savedata = false;
      //fl=true;
    }
    if (this.ghostX != ghostX || this.ghostY != ghostY)
    {
      savedata = true;
    }
    x+=-this.ghostX + ghostX;
    y+=-this.ghostY + ghostY;
    this.ghostX = ghostX;
    this.ghostY = ghostY;
  }
  public void settx(float x)
  {
    if (!fl)
      tx += x;
  }
  public void setty(float y)
  {
    if (!fl)
      ty += y;
  }
  public void setfl(boolean tl)
  {
    fl=tl;
  }
  ////save path

  public int save_total_path(PGraphics pg, int w, int h)
  {
    int len = pointlist.size();
    println("arraylength:"+len);
    if (len>=1)
    {
      pg.beginDraw();
      pg.background(0, 255, 0);
      if (previous_ang == theta && state == previous_state)
      {
        pointlist.remove(len-1);
      }
      pointlist.add(new point_2(ghostX, ghostY));
      point_2 max_point = get_max_point(len);
      previous_ang = theta;
      previous_state = state;
      float x1 = pointlist.get(0).x;
      float y1 = pointlist.get(0).y;
      float tranx = x1;
      float txx = w/2.0f;
      float trany = y1;
      float tyy = h/2.0f;
      float scalex = txx/(max_point.x+tranx+10);
      float scaley = tyy/(max_point.y+trany+10);
      println(scalex+"scalex");
      println(scaley+"scaley");
      float scale = min(scalex, scaley);
      println(scale+"scale");
      pg.scale(scale);
      pg.translate(txx/scale, tyy/scale);
      pg.fill(0, 0, 255);
      pg.stroke(0, 0, 255);
      pg.ellipse(0, 0, 30, 30);
      pg.textSize(32);
      pg.fill(255, 242, 0);
      pg.text("0", 0, 0);
      pg.smooth();
      println(x1);
      println(y1);

      total_path = 0;
      int k = 1;
      //x1 = x1 < 0 ? x1+tranx : x1-tranx;
      //y1 = y1 < 0 ? y1+trany : y1-trany;
      for (int i = 1; i<len; i++)
      {
        float x2 = pointlist.get(i).x;
        float y2 = pointlist.get(i).y;
        println(x2);
        println(y2);
        float xx2 = x2-tranx;// < 0 ? x2+abs(tranx) : x2-abs(tranx);
        float yy2 = y2-trany;// < 0 ? y2+abs(trany) : y2-abs(trany);
        float xx1 = x1-tranx;// < 0 ? x1+abs(tranx) : x1-abs(tranx);
        float yy1 = y1-trany;// < 0 ? y1+abs(trany) : y1-abs(trany);
        float distance = 0;
        if (i!=0)
        {
          distance = sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
          total_path += distance;
        }
        if (distance>40)
        {
          pg.fill(0, 0, 255);
          pg.stroke(0, 0, 255);
          pg.ellipse(xx2, yy2, 30, 30);
        }
        pg.stroke(255, 0, 0);
        pg.strokeWeight(5);
        pg.line(xx1, yy1, xx2, yy2);

        if (distance>40)
        {
          int distata = (int)(distance * 2*.115f);
          String s= distata+"cm";
          pg.fill(0);
          pg.textSize(16);
          pg.text(s, xx2+25, yy2);
          pg.strokeWeight(10);
          pg.textSize(32);
          pg.fill(255, 242, 0);
          pg.text(k, xx2, yy2);
          k++;
          pg.strokeWeight(1);
        }
        x1=x2;
        y1=y2;
      }
      total_path *= 2*.115f;
      String s= "Net Distance Covered: "+total_path+"cm";
      pg.fill(0);
      pg.textSize(36);
      pg.text(s, (-w/2.2f), 70);
      pg.endDraw();
    }
    return len;
  }

  public point_2 get_max_point(int len)
  {
    point_2 maxpoint=new point_2(0, 0);
    for (int i = 1; i<len; i++)
    {
      float x2 = abs(pointlist.get(i).x);
      float y2 = abs(pointlist.get(i).y);
      if (x2>maxpoint.x)maxpoint.x=x2;
      if (y2>maxpoint.y)maxpoint.y=y2;
    }
    return maxpoint;
  }

  //save path
}

public class point_2
{
  public float x;
  public float y;
  public point_2(float x, float y)
  {
    this.x = x;
    this.y = y;
  }
}

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "cgr" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
