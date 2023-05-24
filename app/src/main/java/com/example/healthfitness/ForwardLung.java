package com.example.healthfitness;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.Toast;

public class ForwardLung extends AppCompatActivity {

    Button start_timer, stop_timer;
    ImageView icanchor;
    Animation roundingalone;
    Chronometer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forward_lung);

        start_timer = findViewById(R.id.startTimer7);
        stop_timer = findViewById(R.id.stopTimer7);
        icanchor = findViewById(R.id.icanchor7);
        timer = findViewById(R.id.timer7);
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
                    Toast.makeText(ForwardLung.this,"Час вийшов!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}