package com.example.healthfitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

import java.lang.ref.Reference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterWindow extends AppCompatActivity {

    EditText reg_name, reg_age, reg_weight, reg_height, reg_email, reg_passwd;
    Button register;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    ProgressBar progressBar;
    private static final String USER = "user";
    private User user;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().
            getReferenceFromUrl("https://myapp-d86a1-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_window);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference(USER);

        reg_name = findViewById(R.id.ent_reg_name);
        reg_age = findViewById(R.id.ent_reg_age);
        reg_weight = findViewById(R.id.ent_reg_weight);
        reg_height = findViewById(R.id.ent_reg_height);
        reg_email = findViewById(R.id.ent_reg_email);
        reg_passwd = findViewById(R.id.ent_reg_passwd);

        register = findViewById(R.id.register);
        progressBar = findViewById(R.id.progressBar);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);

                String name = reg_name.getText().toString().trim();
                String age = reg_age.getText().toString().trim();
                String weight = reg_weight.getText().toString().trim();
                String height = reg_height.getText().toString().trim();
                String email = reg_email.getText().toString().trim();
                String passwd = reg_passwd.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(RegisterWindow.this, "Введіть email!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                } else {
                    String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{3}";
                    Pattern pattern = Pattern.compile(emailPattern);
                    Matcher matcher = pattern.matcher(email);

                    if (!matcher.matches()) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(RegisterWindow.this, "Некоректний формат email!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if (TextUtils.isEmpty(passwd)) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(RegisterWindow.this, "Введіть пароль!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (passwd.length() < 8) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(RegisterWindow.this, "Пароль повинен містити принаймні 8 символів", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(name)) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(RegisterWindow.this, "Введіть ім'я", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!name.matches("[a-zA-Zа-яА-ЯёЁіІїЇєЄ]+")) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(RegisterWindow.this, "Ім'я повинно містити лише букви", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(age)) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(RegisterWindow.this, "Введіть свій вік", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!age.matches("\\d+")) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(RegisterWindow.this, "Вік повинен містити лише цифри", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    int ageValue = Integer.parseInt(age);
                    if (ageValue < 1 || ageValue > 100) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(RegisterWindow.this, "Введіть свій справжній вік", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if (TextUtils.isEmpty(height)) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(RegisterWindow.this, "Введіть свій зріст", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!height.matches("\\d+")) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(RegisterWindow.this, "Зріст повинен містити лише цифри", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    int heightValue = Integer.parseInt(height);
                    if (heightValue < 120 || heightValue > 230) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(RegisterWindow.this, "Введіть свій справжній зріст", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if (TextUtils.isEmpty(weight)) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(RegisterWindow.this, "Введіть свою вагу", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!weight.matches("\\d+")) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(RegisterWindow.this, "Вага повинна містити лише цифри", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    int weightValue = Integer.parseInt(weight);
                    if (weightValue < 30 || weightValue > 200) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(RegisterWindow.this, "Введіть свою справжню вагу", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                user = new User(name, Integer.parseInt(age), Integer.parseInt(weight), Integer.parseInt(height), email, passwd);
                RegisterUser(email, passwd);
            }
        });
    }

    public void RegisterUser(String email, String passwd) {
        mAuth.createUserWithEmailAndPassword(email, passwd)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);

                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            Toast.makeText(RegisterWindow.this, "Акаунт створенно!",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterWindow.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(RegisterWindow.this, "Не вдала реєстрація!",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    public void updateUI(FirebaseUser currentUser) {
        String userID = mAuth.getCurrentUser().getUid();
        mDatabase.child(userID).setValue(user);
        Intent intent = new Intent(this, MainWindow.class);
        startActivity(intent);
    }
}