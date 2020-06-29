package com.example.c1tappydefender;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class EnemyShip {

    private Bitmap bitmap;
    private int x,y;
    private int speed = 1;
    private int maxX;
    private int minX;
    private int maxY;
    private int minY;

    public EnemyShip(Context context,int screenX ,int screenY){
        bitmap = BitmapFactory.
                decodeResource(context.getResources(),R.drawable.enemy);
        maxX = screenX;
        maxY = screenY;
        minX = 0;
        minY = 0;
        Random generator  = new Random();
        speed = generator.nextInt(6)+10;
        x = screenX;
        y = generator.nextInt(maxY)-bitmap.getHeight();
    }

    public void update(int playerSpeed){
      x -= playerSpeed;
      x -=speed;
      if(x<minY-bitmap.getHeight()){
          Random generate  = new Random();
          speed =generate.nextInt(10)+10;
          x = maxX;
          y = generate.nextInt(maxY) - bitmap.getHeight();
      }
    }

    public Bitmap getBitmap(){
        return bitmap;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
}
