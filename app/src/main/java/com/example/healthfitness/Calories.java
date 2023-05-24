package com.example.healthfitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.ArrayList;
import java.util.List;

public class Calories extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calories);
        RecyclerView dishListRecyclerView = findViewById(R.id.ingredient_list);

        List<Dish> dishesBreakfast = new ArrayList<>();
        DishListAdapter adapterBreakfast = new DishListAdapter(dishesBreakfast);
        dishListRecyclerView.setAdapter(adapterBreakfast);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        dishListRecyclerView.setLayoutManager(layoutManager);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL);
        dishListRecyclerView.addItemDecoration(itemDecoration);

        Button addIngredientButton = findViewById(R.id.add_ingredient_button);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            String userID = currentUser.getUid();
            DatabaseReference userDishesRefBreakfast = FirebaseDatabase.getInstance().
                    getReference().child("user").
                    child(userID).child("dishes").child("breakfast");

            addIngredientButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(Calories.this);
                    builder.setTitle("Нова страва");

                    LinearLayout layout = new LinearLayout(Calories.this);
                    layout.setOrientation(LinearLayout.VERTICAL);

                    EditText dishNameEditText = new EditText(Calories.this);
                    dishNameEditText.setHint("Назва страви");
                    layout.addView(dishNameEditText);

                    EditText dishDescriptionEditText = new EditText(Calories.this);
                    dishDescriptionEditText.setHint("Кількість калорій");
                    layout.addView(dishDescriptionEditText);

                    builder.setView(layout);

                    builder.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            if (TextUtils.isEmpty(dishNameEditText.getText().toString())) {
                                Toast.makeText(Calories.this, "Введіть назву страви!",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            } else if (!dishNameEditText.getText().toString().
                                    replace(" ", "").
                                    matches("[a-zA-Zа-яА-ЯёЁіІїЇєЄ]+")) {
                                Toast.makeText(Calories.this,
                                        "Назва страви повинна містити лише букви!",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }

                            if (TextUtils.isEmpty(String.valueOf(dishDescriptionEditText.getText().
                                    toString()))) {
                                Toast.makeText(Calories.this,
                                        "Введіть калорійність страви!", Toast.LENGTH_SHORT).show();
                                return;
                            } else if (!String.valueOf(dishDescriptionEditText.getText().
                                    toString()).matches("\\d+")) {
                                Toast.makeText(Calories.this,
                                        "Калорійність повинна містити лише цифри",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }

                            String dishName_ent = dishNameEditText.getText().toString();
                            Integer dishDescription_ent = Integer.parseInt(dishDescriptionEditText.getText().toString());

                            Dish dish = new Dish(dishName_ent, dishDescription_ent);

                            userDishesRefBreakfast.push().setValue(dish);
                        }
                    });
                    builder.setNegativeButton("Відмінити", null);

                    builder.show();

                    adapterBreakfast.notifyDataSetChanged();

                }
            });

            userDishesRefBreakfast.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    dishesBreakfast.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Dish dish = snapshot.getValue(Dish.class);
                        dishesBreakfast.add(dish);
                    }

                    adapterBreakfast.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        } else {
        }

        RecyclerView dishListRecyclerView_snack = findViewById(R.id.ingredient_list_snack);
        List<Dish> dishes_snack = new ArrayList<>();
        DishListAdapter adapter_snack = new DishListAdapter(dishes_snack);
        dishListRecyclerView_snack.setAdapter(adapter_snack);
        RecyclerView.LayoutManager layoutManager_snack = new LinearLayoutManager(this);
        dishListRecyclerView_snack.setLayoutManager(layoutManager_snack);

        RecyclerView.ItemDecoration itemDecoration_snack = new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL);
        dishListRecyclerView_snack.addItemDecoration(itemDecoration_snack);

        Button addIngredientButton_snack = findViewById(R.id.add_ingredient_button_snack);

        if (currentUser != null) {
            String userID = currentUser.getUid();

            DatabaseReference userDishesRefSnack = FirebaseDatabase.getInstance().getReference().child("user").
                    child(userID).child("dishes").child("snack1");

            addIngredientButton_snack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(Calories.this);
                    builder.setTitle("Нова страва");

                    LinearLayout layout = new LinearLayout(Calories.this);
                    layout.setOrientation(LinearLayout.VERTICAL);

                    EditText dishNameEditText = new EditText(Calories.this);
                    dishNameEditText.setHint("Назва страви");
                    layout.addView(dishNameEditText);

                    EditText dishDescriptionEditText = new EditText(Calories.this);
                    dishDescriptionEditText.setHint("Кількість калорій");
                    layout.addView(dishDescriptionEditText);

                    builder.setView(layout);

                    builder.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            if (TextUtils.isEmpty(dishNameEditText.getText().toString())) {
                                Toast.makeText(Calories.this, "Введіть назву страви!",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            } else if (!dishNameEditText.getText().toString().replace(" ",
                                    "").matches("[a-zA-Zа-яА-ЯёЁіІїЇєЄ]+")) {
                                Toast.makeText(Calories.this,
                                        "Назва страви повинна містити лише букви!", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            if (TextUtils.isEmpty(String.valueOf(dishDescriptionEditText.getText().toString()))) {
                                Toast.makeText(Calories.this, "Введіть калорійність страви!", Toast.LENGTH_SHORT).show();
                                return;
                            } else if (!String.valueOf(dishDescriptionEditText.getText().toString()).matches("\\d+")) {
                                Toast.makeText(Calories.this, "Калорійність повинна містити лише цифри", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            String dishName_ent = dishNameEditText.getText().toString();
                            Integer dishDescription_ent = Integer.parseInt(dishDescriptionEditText.getText().toString());

                            Dish dish = new Dish(dishName_ent, dishDescription_ent);

                            userDishesRefSnack.push().setValue(dish);

                        }
                    });
                    builder.setNegativeButton("Відмінити", null);
                    builder.show();
                    adapter_snack.notifyDataSetChanged();
                }
            });

            userDishesRefSnack.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    dishes_snack.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Dish dish = snapshot.getValue(Dish.class);
                        dishes_snack.add(dish);
                    }

                    adapter_snack.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {

        }

        RecyclerView dishListRecyclerView_lunch = findViewById(R.id.ingredient_list_lunch);
        List<Dish> dishes_lunch = new ArrayList<>();
        DishListAdapter adapter_lunch = new DishListAdapter(dishes_lunch);
        dishListRecyclerView_lunch.setAdapter(adapter_lunch);
        RecyclerView.LayoutManager layoutManager_lunch = new LinearLayoutManager(this);
        dishListRecyclerView_lunch.setLayoutManager(layoutManager_lunch);

        RecyclerView.ItemDecoration itemDecoration_lunch = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dishListRecyclerView_lunch.addItemDecoration(itemDecoration_lunch);

        Button addIngredientButton_lunch = findViewById(R.id.add_ingredient_button_lunch);

        if (currentUser != null) {
            String userID = currentUser.getUid();

            DatabaseReference userDishesRefLunch = FirebaseDatabase.getInstance().getReference().child("user").
                    child(userID).child("dishes").child("lunch");


            addIngredientButton_lunch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(Calories.this);
                    builder.setTitle("Нова страва");

                    LinearLayout layout = new LinearLayout(Calories.this);
                    layout.setOrientation(LinearLayout.VERTICAL);

                    EditText dishNameEditText = new EditText(Calories.this);
                    dishNameEditText.setHint("Назва страви");
                    layout.addView(dishNameEditText);

                    EditText dishDescriptionEditText = new EditText(Calories.this);
                    dishDescriptionEditText.setHint("Кількість калорій");
                    layout.addView(dishDescriptionEditText);

                    builder.setView(layout);

                    builder.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            if (TextUtils.isEmpty(dishNameEditText.getText().toString())) {
                                Toast.makeText(Calories.this, "Введіть назву страви!", Toast.LENGTH_SHORT).show();
                                return;
                            } else if (!dishNameEditText.getText().toString().replace(" ", "").matches("[a-zA-Zа-яА-ЯёЁіІїЇєЄ]+")) {
                                Toast.makeText(Calories.this, "Назва страви повинна містити лише букви!", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            if (TextUtils.isEmpty(String.valueOf(dishDescriptionEditText.getText().toString()))) {
                                Toast.makeText(Calories.this, "Введіть калорійність страви!", Toast.LENGTH_SHORT).show();
                                return;
                            } else if (!String.valueOf(dishDescriptionEditText.getText().toString()).matches("\\d+")) {
                                Toast.makeText(Calories.this, "Калорійність повинна містити лише цифри", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            String dishName_ent = dishNameEditText.getText().toString();
                            Integer dishDescription_ent = Integer.parseInt(dishDescriptionEditText.getText().toString());

                            Dish dish = new Dish(dishName_ent, dishDescription_ent);

                            userDishesRefLunch.push().setValue(dish);

                        }
                    });
                    builder.setNegativeButton("Відмінити", null);

                    builder.show();
                    adapter_lunch.notifyDataSetChanged();
                }
            });

            userDishesRefLunch.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    dishes_lunch.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Dish dish = snapshot.getValue(Dish.class);
                        dishes_lunch.add(dish);
                    }

                    adapter_lunch.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
        }

        RecyclerView dishListRecyclerView_snack2 = findViewById(R.id.ingredient_list_snack2);
        List<Dish> dishes_snack2 = new ArrayList<>();
        DishListAdapter adapter_snack2 = new DishListAdapter(dishes_snack2);
        dishListRecyclerView_snack2.setAdapter(adapter_snack2);
        RecyclerView.LayoutManager layoutManager_snack2 = new LinearLayoutManager(this);
        dishListRecyclerView_snack2.setLayoutManager(layoutManager_snack2);

        RecyclerView.ItemDecoration itemDecoration_snack2 = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dishListRecyclerView_snack2.addItemDecoration(itemDecoration_snack2);

        Button addIngredientButton_snack2 = findViewById(R.id.add_ingredient_button_snack2);

        if (currentUser != null) {
            String userID = currentUser.getUid();

            DatabaseReference userDishesRefSnack2 = FirebaseDatabase.getInstance().getReference().child("user").
                    child(userID).child("dishes").child("snack2");

            addIngredientButton_snack2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(Calories.this);
                    builder.setTitle("Нова страва");

                    LinearLayout layout = new LinearLayout(Calories.this);
                    layout.setOrientation(LinearLayout.VERTICAL);

                    EditText dishNameEditText = new EditText(Calories.this);
                    dishNameEditText.setHint("Назва страви");
                    layout.addView(dishNameEditText);

                    EditText dishDescriptionEditText = new EditText(Calories.this);
                    dishDescriptionEditText.setHint("Кількість калорій");
                    layout.addView(dishDescriptionEditText);

                    builder.setView(layout);

                    builder.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            if (TextUtils.isEmpty(dishNameEditText.getText().toString())) {
                                Toast.makeText(Calories.this, "Введіть назву страви!", Toast.LENGTH_SHORT).show();
                                return;
                            } else if (!dishNameEditText.getText().toString().replace(" ", "").matches("[a-zA-Zа-яА-ЯёЁіІїЇєЄ]+")) {
                                Toast.makeText(Calories.this, "Назва страви повинна містити лише букви!", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            if (TextUtils.isEmpty(String.valueOf(dishDescriptionEditText.getText().toString()))) {
                                Toast.makeText(Calories.this, "Введіть калорійність страви!", Toast.LENGTH_SHORT).show();
                                return;
                            } else if (!String.valueOf(dishDescriptionEditText.getText().toString()).matches("\\d+")) {
                                Toast.makeText(Calories.this, "Калорійність повинна містити лише цифри", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            String dishName_ent = dishNameEditText.getText().toString();
                            Integer dishDescription_ent = Integer.parseInt(dishDescriptionEditText.getText().toString());

                            Dish dish = new Dish(dishName_ent, dishDescription_ent);

                            userDishesRefSnack2.push().setValue(dish);
                        }
                    });
                    builder.setNegativeButton("Відмінити", null);

                    builder.show();

                    adapter_snack2.notifyDataSetChanged();
                }
            });
            userDishesRefSnack2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    dishes_snack2.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Dish dish = snapshot.getValue(Dish.class);
                        dishes_snack2.add(dish);
                    }

                    adapter_snack2.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        } else {

        }

        RecyclerView dishListRecyclerView_dinner = findViewById(R.id.ingredient_list_dinner);
        List<Dish> dishes_dinner = new ArrayList<>();
        DishListAdapter adapter_dinner = new DishListAdapter(dishes_dinner);
        dishListRecyclerView_dinner.setAdapter(adapter_dinner);
        RecyclerView.LayoutManager layoutManager_dinner = new LinearLayoutManager(this);
        dishListRecyclerView_dinner.setLayoutManager(layoutManager_dinner);

        RecyclerView.ItemDecoration itemDecoration_dinner = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dishListRecyclerView_dinner.addItemDecoration(itemDecoration_dinner);

        Button addIngredientButton_dinner = findViewById(R.id.add_ingredient_button_dinner);

        if (currentUser != null) {
            String userID = currentUser.getUid();

            DatabaseReference userDishesRefDinner = FirebaseDatabase.getInstance().getReference().child("user").
                    child(userID).child("dishes").child("dinner");

            addIngredientButton_dinner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(Calories.this);
                    builder.setTitle("Нова страва");

                    LinearLayout layout = new LinearLayout(Calories.this);
                    layout.setOrientation(LinearLayout.VERTICAL);

                    EditText dishNameEditText = new EditText(Calories.this);
                    dishNameEditText.setHint("Назва страви");
                    layout.addView(dishNameEditText);

                    EditText dishDescriptionEditText = new EditText(Calories.this);
                    dishDescriptionEditText.setHint("Кількість калорій");
                    layout.addView(dishDescriptionEditText);

                    builder.setView(layout);

                    builder.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            if (TextUtils.isEmpty(dishNameEditText.getText().toString())) {
                                Toast.makeText(Calories.this, "Введіть назву страви!", Toast.LENGTH_SHORT).show();
                                return;
                            } else if (!dishNameEditText.getText().toString().replace(" ", "").matches("[a-zA-Zа-яА-ЯёЁіІїЇєЄ]+")) {
                                Toast.makeText(Calories.this, "Назва страви повинна містити лише букви!", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            if (TextUtils.isEmpty(String.valueOf(dishDescriptionEditText.getText().toString()))) {
                                Toast.makeText(Calories.this, "Введіть калорійність страви!", Toast.LENGTH_SHORT).show();
                                return;
                            } else if (!String.valueOf(dishDescriptionEditText.getText().toString()).matches("\\d+")) {
                                Toast.makeText(Calories.this, "Калорійність повинна містити лише цифри", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            String dishName_ent = dishNameEditText.getText().toString();
                            Integer dishDescription_ent = Integer.parseInt(dishDescriptionEditText.getText().toString());

                            Dish dish = new Dish(dishName_ent, dishDescription_ent);

                            userDishesRefDinner.push().setValue(dish);
                        }
                    });
                    builder.setNegativeButton("Відмінити", null);

                    builder.show();
                    adapter_dinner.notifyDataSetChanged();
                }
            });

            userDishesRefDinner.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    dishes_dinner.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Dish dish = snapshot.getValue(Dish.class);
                        dishes_dinner.add(dish);
                    }

                    adapter_dinner.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        } else {

        }
    }
}