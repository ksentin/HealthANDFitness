package com.example.healthfitness;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.zip.Inflater;

public class todayActFragment extends Fragment {
    TextView count_calories_food, count_calories_sport;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private LayoutInflater inflater;
    private ViewGroup container;

    public todayActFragment() {
    }

    public static todayActFragment newInstance(String param1, String param2) {
        todayActFragment fragment = new todayActFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.inflater = inflater;
        this.container = container;
        View view = inflater.inflate(R.layout.fragment_today_act, container, false);

        count_calories_food = view.findViewById(R.id.count_calories_food);
        count_calories_sport = view.findViewById(R.id.count_calories_sport);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            String userID = currentUser.getUid();

            DatabaseReference userSport = FirebaseDatabase.getInstance().getReference().
                    child("user").child(userID).child("sport");
            DatabaseReference userDishes = FirebaseDatabase.getInstance().getReference().
                    child("user").child(userID).child("dishes");

            userSport.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int totalCalories = 0;

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        int calories = snapshot.getValue(Integer.class);
                        totalCalories += calories;
                    }
                    count_calories_sport.setText(String.valueOf(totalCalories));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

            userDishes.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int totalCaloriesFood = 0;
                    for (DataSnapshot dishSnapshot : dataSnapshot.getChildren()) {
                        for (DataSnapshot mealSnapshot : dishSnapshot.getChildren()) {
                            Integer calories = mealSnapshot.child("description").getValue(Integer.class);
                            if (calories != null) {
                                totalCaloriesFood += calories;
                            }
                        }
                    }
                    count_calories_food.setText(String.valueOf(totalCaloriesFood));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }

        return view;
    }
}