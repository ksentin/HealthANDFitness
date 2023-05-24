package com.example.healthfitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    EditText email_field, psswrd_field;
    Button login_btn, gotoreg_btn;
    FirebaseAuth mAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        email_field = findViewById(R.id.ent_email);
        psswrd_field = findViewById(R.id.ent_passwd);
        login_btn = findViewById(R.id.login_butt);
        gotoreg_btn = findViewById(R.id.registr_btn);
        progressBar = findViewById(R.id.progressBar);


        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);

                String email = email_field.getText().toString().trim();
                String pass = psswrd_field.getText().toString().trim();
                if (!email.isEmpty()) {
                    if (!pass.isEmpty()) {
                        mAuth.signInWithEmailAndPassword(email, pass)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        progressBar.setVisibility(View.GONE);
                                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("user");
                                        userRef.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    String name = dataSnapshot.child("name").getValue(String.class);
                                                    String age = dataSnapshot.child("age").getValue(Integer.class).toString().trim();

                                                    Toast.makeText(MainActivity.this, "Вітаю!", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(MainActivity.this, MainWindow.class);
                                                    intent.putExtra("name", name);
                                                    intent.putExtra("age", age);
                                                    startActivity(intent);
                                                } else {
                                                    progressBar.setVisibility(View.GONE);
                                                    Toast.makeText(MainActivity.this, "Акаунту не існує!", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(MainActivity.this, "Помилка входу!" + error.getMessage(), Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(MainActivity.this, "Помилка входу!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "Ви не ввели пароль!", Toast.LENGTH_SHORT).show();
                    }
                } else if (email.isEmpty()) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "Ви не ввели email!", Toast.LENGTH_SHORT).show();
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "Не коректний email!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        gotoreg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterWindow.class);
                startActivity(intent);
            }
        });

    }
}