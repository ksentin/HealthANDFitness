package com.example.healthfitness;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class StatisticsFragment extends Fragment {

    TextView statistic_age, statistic_weight, statistic_height, ideal_weight, imt, calories_norm,
            proteins, fats, carbohydrates;
    DatabaseReference userRef;

    private LayoutInflater inflater;
    private ViewGroup container;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public StatisticsFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static StatisticsFragment newInstance(String param1, String param2) {
        StatisticsFragment fragment = new StatisticsFragment();
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
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        statistic_age = view.findViewById(R.id.ageTextView);
        statistic_weight = view.findViewById(R.id.weightTextView);
        statistic_height = view.findViewById(R.id.heightTextView);

        ideal_weight = view.findViewById(R.id.ideal_weight);
        imt = view.findViewById(R.id.imt);
        calories_norm = view.findViewById(R.id.calories_norm);
        proteins = view.findViewById(R.id.proteins);
        fats = view.findViewById(R.id.fats);
        carbohydrates = view.findViewById(R.id.carbohydrates);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("user").child(uid);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int age = dataSnapshot.child("age").getValue(Integer.class);
                    int weight = dataSnapshot.child("weight").getValue(Integer.class);
                    int height = dataSnapshot.child("height").getValue(Integer.class);

                    statistic_age.setText("вік: " + String.valueOf(age));
                    statistic_weight.setText("вага: " + String.valueOf(weight));
                    statistic_height.setText("зріст: " + String.valueOf(height));

                    ideal_weight.setText((Ideal_weight(height).get(0)) + " - " + (Ideal_weight(height).get(1)));
                    imt.setText(IMT(height, weight));
                    calories_norm.setText(Calories_Norm(height, weight, age).get(0) + " - " + Calories_Norm(height, weight, age).get(1));
                    proteins.setText(Count_proteins(height, weight, age).get(0) + " - " + Count_proteins(height, weight, age).get(1));
                    fats.setText(Count_fats(height, weight, age).get(0) + " - " + Count_fats(height, weight, age).get(1));
                    carbohydrates.setText(Count_carbohydrates(height, weight, age).get(0) + " - " + Count_carbohydrates(height, weight, age).get(1));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Failed to read user data", Toast.LENGTH_SHORT).show();
            }
        };

        userRef.addValueEventListener(valueEventListener);

        return view;
    }

    public List<String> Ideal_weight(int heigt) {
        List<String> weightList = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        double weightStart = ((heigt - 110) * 1.15) - 8;
        double weightEnd = ((heigt - 110) * 1.15) + 8;
        String roundedWeightStart = decimalFormat.format(weightStart);
        String roundedEnd = decimalFormat.format(weightEnd);
        weightList.add(roundedWeightStart);
        weightList.add(roundedEnd);

        return weightList;
    }

    public String IMT(int heigt, int weight) {
        double imt = (double) weight / (Math.pow((heigt / 100.0), 2));
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String st_imt = decimalFormat.format(imt);
        return st_imt;
    }

    public List<String> Calories_Norm(int heigt, int weight, int age) {
        List<String> caloriesList = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        double BNR_start = 10 * weight + 6.25 * heigt - 5 * age + 200;
        double BNR_end = 10 * weight + 6.25 * heigt - 5 * age + 1000;

        String BNR_start_st = decimalFormat.format(BNR_start);
        String BNR_end_st = decimalFormat.format(BNR_end);
        caloriesList.add(BNR_start_st);
        caloriesList.add(BNR_end_st);

        return caloriesList;
    }

    public List<String> Count_proteins(int heigt, int weight, int age) {
        double BNR_start = 10 * weight + 6.25 * heigt - 5 * age + 200;
        double BNR_end = 10 * weight + 6.25 * heigt - 5 * age + 1000;

        double proteins_start = (0.3 * BNR_start) / 4;
        double proteins_end = (0.3 * BNR_end) / 4;

        List<String> caloriesList = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        String proteins_start_st = decimalFormat.format(proteins_start);
        String proteins_end_st = decimalFormat.format(proteins_end);
        caloriesList.add(proteins_start_st);
        caloriesList.add(proteins_end_st);

        return caloriesList;
    }

    public List<String> Count_fats(int heigt, int weight, int age) {
        double BNR_start = 10 * weight + 6.25 * heigt - 5 * age + 200;
        double BNR_end = 10 * weight + 6.25 * heigt - 5 * age + 1000;

        double fats_start = (0.3 * BNR_start) / 9;
        double fats_end = (0.3 * BNR_end) / 9;

        List<String> fatsList = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        String fats_start_st = decimalFormat.format(fats_start);
        String fats_end_st = decimalFormat.format(fats_end);
        fatsList.add(fats_start_st);
        fatsList.add(fats_end_st);

        return fatsList;
    }

    public List<String> Count_carbohydrates(int heigt, int weight, int age) {
        double BNR_start = 10 * weight + 6.25 * heigt - 5 * age + 200;
        double BNR_end = 10 * weight + 6.25 * heigt - 5 * age + 1000;

        double carbohydrates_start = (0.4 * BNR_start) / 4;
        double carbohydrates_end = (0.4 * BNR_end) / 4;

        List<String> carbohydratesList = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        String carbohydrates_start_st = decimalFormat.format(carbohydrates_start);
        String carbohydrates_end_st = decimalFormat.format(carbohydrates_end);
        carbohydratesList.add(carbohydrates_start_st);
        carbohydratesList.add(carbohydrates_end_st);

        return carbohydratesList;
    }
}