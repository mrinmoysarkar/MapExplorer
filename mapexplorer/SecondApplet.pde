import javax.swing.*;
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
      total_path *= 2*.115;
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
      int nox = int(w/(50*scale_value));
      for (int i = 0; i<=nox; i++)
      {
        stroke(0, 0, 255);
        line(i*50-translatex/scale_value, -translatey/scale_value, i*50-translatex/scale_value, w/scale_value-translatey/scale_value);
      }
      int noy=int(h/(50*scale_value));
      for (int i = 0; i<=noy; i++)
      {
        stroke(0, 0, 255);
        line(-translatex/scale_value, i*50-translatey/scale_value, h/scale_value-translatex/scale_value, i*50-translatey/scale_value);
      }

      //fill(0, 255, 0);
      //ellipse((w/2-3)/scale_value, (h/2-15)/scale_value, 10, 10);
      //int col = int(random(100, 255)); 
      float col1 = int(random(150, 255));
      float col2 = int(random(20, 60));
      float col3 = int(random(10, 60));
      fill(col1, col2, col3); 
      stroke(col1, col2, col3);
      ellipse(30, 0, 10, 10);//ghostX/scale_value, ghostY/scale_value, 5, 5);
    }
  }

  void change_grid()
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

  void trans()
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

  void zoom(int scal)
  {
    if (scal == 1)
    {
      scale_value+=.01;
    } else if (scal==-1)
    {
      scale_value+=-.01;
    }
  }
  void show_total_path()
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
          int distata = (int)(distance * 2*.115);
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
  void draw_path()
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
  void settx(float x)
  {
    if (!fl)
      tx += x;
  }
  void setty(float y)
  {
    if (!fl)
      ty += y;
  }
  void setfl(boolean tl)
  {
    fl=tl;
  }
  ////save path

  int save_total_path(PGraphics pg, int w, int h)
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
      float txx = w/2.0;
      float trany = y1;
      float tyy = h/2.0;
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
          int distata = (int)(distance * 2*.115);
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
      total_path *= 2*.115;
      String s= "Net Distance Covered: "+total_path+"cm";
      pg.fill(0);
      pg.textSize(36);
      pg.text(s, (-w/2.2), 70);
      pg.endDraw();
    }
    return len;
  }

  point_2 get_max_point(int len)
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

