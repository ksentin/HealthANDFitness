package com.example.healthfitness;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DishListAdapter extends RecyclerView.Adapter<DishListAdapter.ViewHolder> {
    private List<Dish> dishes;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView dishNameTextView;
        public TextView dishDescriptionTextView;

        public ViewHolder(View view) {
            super(view);
            dishNameTextView = view.findViewById(R.id.dish_name);
            dishDescriptionTextView = view.findViewById(R.id.dish_description);
        }
    }

    public DishListAdapter(List<Dish> dishes) {
        this.dishes = dishes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredient_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Dish dish = dishes.get(position);
        holder.dishNameTextView.setText(dish.getName());
        holder.dishDescriptionTextView.setText(String.valueOf(dish.getDescription()));
    }

    @Override
    public int getItemCount() {
        return dishes.size();
    }
}

