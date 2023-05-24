package com.example.healthfitness;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Update_param extends AppCompatActivity {

    EditText update_name, update_age, update_weight, update_height;
    Button update;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_param);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        update_name = findViewById(R.id.update_name);
        update_age = findViewById(R.id.update_age);
        update_weight = findViewById(R.id.update_weight);
        update_height = findViewById(R.id.update_height);
        update = findViewById(R.id.update);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(update_name.getText().toString())) {
                    Toast.makeText(Update_param.this, "Введіть ім'я", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!update_name.getText().toString().matches("[a-zA-Zа-яА-ЯёЁіІїЇєЄ]+")) {
                    Toast.makeText(Update_param.this, "Ім'я повинно містити лише букви", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(update_age.getText().toString())) {
                    Toast.makeText(Update_param.this, "Введіть свій вік", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!update_age.getText().toString().matches("\\d+")) {
                    Toast.makeText(Update_param.this, "Вік повинен містити лише цифри", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    int ageValue = Integer.parseInt(update_age.getText().toString());
                    if (ageValue < 1 || ageValue > 100) {
                        Toast.makeText(Update_param.this, "Введіть свій справжній вік", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if (TextUtils.isEmpty(update_height.getText().toString())) {
                    Toast.makeText(Update_param.this, "Введіть свій зріст", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!update_height.getText().toString().matches("\\d+")) {
                    Toast.makeText(Update_param.this, "Зріст повинен містити лише цифри", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    int heightValue = Integer.parseInt(update_height.getText().toString());
                    if (heightValue < 120 || heightValue > 230) {
                        Toast.makeText(Update_param.this, "Введіть свій справжній зріст", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if (TextUtils.isEmpty(update_height.getText().toString())) {
                    Toast.makeText(Update_param.this, "Введіть свою вагу", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!update_height.getText().toString().matches("\\d+")) {
                    Toast.makeText(Update_param.this, "Вага повинна містити лише цифри", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    int weightValue = Integer.parseInt(update_height.getText().toString());
                    if (weightValue < 30 || weightValue > 200) {
                        Toast.makeText(Update_param.this, "Введіть свою справжню вагу", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                String name = update_name.getText().toString();
                Integer age = Integer.parseInt(update_age.getText().toString());
                Integer weight = Integer.parseInt(update_weight.getText().toString());
                Integer height = Integer.parseInt(update_height.getText().toString());

                updateUserData(name, age, weight, height);

                finish();
            }
        });

    }

    private void updateUserData(String name, Integer age, Integer weight, Integer height) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String userID = currentUser.getUid();
            DatabaseReference userRef = databaseReference.child("user").child(userID);
            userRef.child("name").setValue(name);
            userRef.child("age").setValue(age);
            userRef.child("weight").setValue(weight);
            userRef.child("height").setValue(height);
        } else {
        }
    }
}