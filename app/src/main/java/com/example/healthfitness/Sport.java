package com.example.healthfitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class Sport extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport);

        String[] colors = {"Планка", "Планка на ліктях", "Віджимання", "Присідання", "Скручування", "Стрибки",
                "Випади вперед", "Випади в сторону", "Велосипед", "Кач спиною", "Махи назад", "Сідничний місток",
                "Сідничний місток на одній нозі", "Кішка", "Біг з високо піднятими колінами", "Підняття ніг"};

        ListView list = (ListView) findViewById(R.id.listview);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, R.layout.list_layout, colors);

        list.setAdapter(adapter);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            String userID = currentUser.getUid();

            DatabaseReference userSport = FirebaseDatabase.getInstance().getReference().child("user").
                    child(userID).child("sport");

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    int caloriesBurned = 0;
                    if (position == 0) {
                        startActivity(new Intent(Sport.this, Plank.class));
                        caloriesBurned = 10;
                    } else if (position == 1) {
                        startActivity(new Intent(Sport.this, ElbowPlank.class));
                        caloriesBurned = 10;
                    } else if (position == 2) {
                        startActivity(new Intent(Sport.this, Push_ups.class));
                        caloriesBurned = 10;
                    } else if (position == 3) {
                        startActivity(new Intent(Sport.this, Squats.class));
                        caloriesBurned = 10;
                    } else if (position == 4) {
                        startActivity(new Intent(Sport.this, Twisting.class));
                        caloriesBurned = 10;
                    } else if (position == 5) {
                        startActivity(new Intent(Sport.this, JumpingJ.class));
                        caloriesBurned = 20;
                    } else if (position == 6) {
                        startActivity(new Intent(Sport.this, ForwardLung.class));
                        caloriesBurned = 10;
                    } else if (position == 7) {
                        startActivity(new Intent(Sport.this, Lunges.class));
                        caloriesBurned = 10;
                    } else if (position == 8) {
                        startActivity(new Intent(Sport.this, Bike.class));
                        caloriesBurned = 10;
                    } else if (position == 9) {
                        startActivity(new Intent(Sport.this, BackExsers.class));
                        caloriesBurned = 10;
                    } else if (position == 10) {
                        startActivity(new Intent(Sport.this, KickBack.class));
                        caloriesBurned = 10;
                    } else if (position == 11) {
                        startActivity(new Intent(Sport.this, Gluteal_bridge.class));
                        caloriesBurned = 10;
                    } else if (position == 12) {
                        startActivity(new Intent(Sport.this, Gluteal_bridge_withLeg.class));
                        caloriesBurned = 10;
                    } else if (position == 13) {
                        startActivity(new Intent(Sport.this, Cat_Exsers.class));
                        caloriesBurned = 7;
                    } else if (position == 14) {
                        startActivity(new Intent(Sport.this, RunKneeHight.class));
                        caloriesBurned = 15;
                    } else if (position == 15) {
                        startActivity(new Intent(Sport.this, LegsUp.class));
                        caloriesBurned = 10;
                    }

                    DatabaseReference newSportRef = userSport.push();

                    newSportRef.setValue(caloriesBurned).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });

                }
            });
        } else {
        }
    }
}



