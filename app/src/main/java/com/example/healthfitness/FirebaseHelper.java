package com.example.healthfitness;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHelper {
    private DatabaseReference databaseReference;

    public FirebaseHelper() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("dishes");
    }

    public void addDish(Dish dish) {
        String dishId = databaseReference.push().getKey();
        databaseReference.child(dishId).setValue(dish);
    }

    public DatabaseReference getDishesReference() {
        return databaseReference;
    }
}

