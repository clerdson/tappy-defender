package com.example.c1tappydefender;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class TDView extends SurfaceView implements Runnable {
volatile boolean playing;
Thread gameThread = null;
private PlayerShip player;
public EnemyShip enemy1;
public EnemyShip enemy2;
public EnemyShip enemy3;
//For drawing
private Paint paint;
private Canvas canvas;
private SurfaceHolder ourHolder;
  public TDView(Context context,int x,int y){

      super(context);
      //Initializze our drawing objects
      ourHolder = getHolder();
      paint = new Paint();
      player = new PlayerShip(context,x,y);
      enemy1= new EnemyShip(context,x,y);
      enemy2 = new EnemyShip(context,x,y);
      enemy3 = new EnemyShip(context,x,y);

   }

   @Override
   public void run(){
      while(playing){
          update();
          draw();
          control();
      }
   }

private void update(){
      player.update();
      enemy1.update(player.getSpeed());
      enemy2.update(player.getSpeed());
      enemy3.update(player.getSpeed());
}
private void draw(){
      if(ourHolder.getSurface().isValid()){
          canvas = ourHolder.lockCanvas();

          canvas.drawColor(Color.argb(255,0,0,0));
          canvas.drawBitmap(
                  player.getBitmap(),
                  player.getX(),
                  player.getY(),
                  paint
          );
          canvas.drawBitmap(enemy1.getBitmap(),enemy1.getX(),enemy1.getY(),paint);
          canvas.drawBitmap(enemy2.getBitmap(),enemy2.getX(),enemy2.getY(),paint);
          canvas.drawBitmap(enemy3.getBitmap(),enemy3.getX(),enemy3.getY(),paint);
          ourHolder.unlockCanvasAndPost(canvas);
      }
}
private void control(){}

//Clean up our thread if the game is interrupted or the player

    public void pause(){
      playing=false;
      try {
          gameThread.join();

      }catch (InterruptedException e ){}
    }

    //Make a new thread and start it
    //Execution moves to our R
    public void resume(){
      playing = true;
      gameThread = new Thread(this);
      gameThread.start();
    }

    @Override
    public  boolean onTouchEvent(MotionEvent motionEvent){
      switch (motionEvent.getAction()&MotionEvent.ACTION_MASK){
          case MotionEvent.ACTION_UP:
              player.stopBoosting();
              break;
          case MotionEvent.ACTION_DOWN:
              player.setBoosting();
              break;
      }
      return true;
    }
}
