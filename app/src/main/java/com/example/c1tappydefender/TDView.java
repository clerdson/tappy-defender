package com.example.c1tappydefender;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

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
    public ArrayList<SpaceDust> dustList = new ArrayList<SpaceDust>();


    private float distanceRemaining;
    private long timeTaken;
    private long timeStarted;
    private long fastestTime;

    private int screenX;
    private int screenY;


    public TDView(Context context, int x, int y) {

        super(context);
        //Initializze our drawing objects
        ourHolder = getHolder();
        paint = new Paint();
        player = new PlayerShip(context, x, y);
        enemy1 = new EnemyShip(context, x, y);
        enemy2 = new EnemyShip(context, x, y);
        enemy3 = new EnemyShip(context, x, y);
        int numSpecs = 40;
        for (int i = 0; i < numSpecs; i++) {
            SpaceDust spec = new SpaceDust(x, y);
            dustList.add(spec);
        }
        screenX = x;
        screenY = y;

    }

    @Override
    public void run() {
        while (playing) {
            update();
            draw();
            control();
        }
    }

    private void update() {
        player.update();
        enemy1.update(player.getSpeed());
        enemy2.update(player.getSpeed());
        enemy3.update(player.getSpeed());
        for (SpaceDust sd : dustList) {
            sd.updadte(player.getSpeed());
        }
        // Collision detection on new positions
// Before move because we are testing last frames
// position which has just been drawn
// If you are using images in excess of 100 pixels
// wide then increase the -100 value accordingly
        if (Rect.intersects
                (player.getHitbox(), enemy1.getHitbox())) {
            enemy1.setX(-100);

        }
        if (Rect.intersects
                (player.getHitbox(), enemy2.getHitbox())) {
            enemy2.setX(-100);
        }
        if (Rect.intersects
                (player.getHitbox(), enemy3.getHitbox())) {
            enemy3.setX(-100);
        }


    }

    private void draw() {
        if (ourHolder.getSurface().isValid()) {
            canvas = ourHolder.lockCanvas();
            paint.setColor(Color.argb(255, 255, 255, 255));
// Rub out the last frame
            canvas.drawColor(Color.argb(255, 0, 0, 0));
            // For debugging
// Switch to white pixels
            paint.setColor(Color.argb(255, 255, 255, 255));
// Draw Hit boxes
            canvas.drawRect(player.getHitbox().left,
                    player.getHitbox().top,
                    player.getHitbox().right,
                    player.getHitbox().bottom,
                    paint);
            canvas.drawRect(enemy1.getHitbox().left,
                    enemy1.getHitbox().top,
                    enemy1.getHitbox().right,
                    enemy1.getHitbox().bottom,
                    paint);
            canvas.drawRect(enemy2.getHitbox().left,
                    enemy2.getHitbox().top,
                    enemy2.getHitbox().right,
                    enemy2.getHitbox().bottom,
                    paint
            );

            canvas.drawRect(enemy3.getHitbox().left,
                    enemy3.getHitbox().top,
                    enemy3.getHitbox().right,
                    enemy3.getHitbox().bottom,
                    paint);
            canvas.drawBitmap(player.getBitmap(), player.getX(), player.getY(), paint);
            canvas.drawBitmap(enemy1.getBitmap(), enemy1.getX(), enemy1.getY(), paint);
            canvas.drawBitmap(enemy2.getBitmap(), enemy2.getX(), enemy2.getY(), paint);
            canvas.drawBitmap(enemy3.getBitmap(), enemy3.getX(), enemy3.getY(), paint);
            paint.setColor(Color.argb(255, 255, 255, 255));
            for (SpaceDust sd : dustList) {
                canvas.drawPoint(sd.getX(), sd.getY(), paint);
            }

            // Draw the hud
            paint.setTextAlign(Paint.Align.LEFT);
            paint.setColor(Color.argb(255, 255, 255, 255));
            paint.setTextSize(25);
            canvas.drawText("Fastest:"+ fastestTime + "s", 10, 20, paint);
            canvas.drawText("Time:" + timeTaken + "s", screenX / 2, 20,
                    paint);
            canvas.drawText("Distance:" +
                    distanceRemaining / 1000 +
                    " KM", screenX / 3, screenY - 20, paint);
            canvas.drawText("Shield:" +
                    player.getShieldStrength(), 10, screenY - 20, paint);

            canvas.drawText("Speed:" + player.getSpeed() * 60 +
                    " MPS", (screenX /3 ) * 2, screenY - 20, paint);

                    ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void control() {
    }

//Clean up our thread if the game is interrupted or the player

    public void pause() {
        playing = false;
        try {
            gameThread.join();

        } catch (InterruptedException e) {
        }
    }

    //Make a new thread and start it
    //Execution moves to our R
    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
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
