package com.example.healthfitness;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.Toast;

public class Plank extends AppCompatActivity {

    Button start_timer, stop_timer;
    ImageView icanchor;
    Animation roundingalone;
    Chronometer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plank);

        start_timer = findViewById(R.id.startTimer);
        stop_timer = findViewById(R.id.stopTimer);
        icanchor = findViewById(R.id.icanchor);
        timer = findViewById(R.id.timer);
        roundingalone = AnimationUtils.loadAnimation(this, R.anim.roundingalone);

        start_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                icanchor.startAnimation(roundingalone);
                timer.setBase(SystemClock.elapsedRealtime() + 60000);
                timer.start();
            }
        });

        stop_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.stop();
                icanchor.clearAnimation();
            }
        });

        timer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if ((SystemClock.elapsedRealtime() - chronometer.getBase()) >= 0) {
                    chronometer.stop();
                    icanchor.clearAnimation();
                    Toast.makeText(Plank.this,"Час вийшов!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}