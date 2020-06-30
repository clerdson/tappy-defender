package com.example.c1tappydefender;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

public class GameActivity extends Activity {
    //Our object to handle the View
    TDView gameView;

    //This is where the play button from HomeActivity sends us
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        //setContentView(R.layout.activity_game);
        //Create an instance of our Tappy defender View (TDView)
        //Also passing in "this" whitch is the Context or uor app
        gameView = new TDView(this, size.x, size.y);

        setContentView(gameView);
    }

    //If the activity is pause make sure to pause our thread
    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    //If the activity is resume make sure to resume our thread
    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }
}
