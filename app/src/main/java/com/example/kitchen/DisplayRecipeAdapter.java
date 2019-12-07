package com.example.kitchen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DisplayRecipeAdapter extends RecyclerView.Adapter<DisplayRecipeAdapter.DisplayRecipeViewHolder> {

    public class DisplayRecipeViewHolder extends RecyclerView.ViewHolder {

        TextView text;

        DisplayRecipeViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.list_item_string);
        }
    }

    private ArrayList<String> items;

    public DisplayRecipeAdapter(ArrayList<String> arrayList) {
        items = arrayList;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public DisplayRecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.display_recipe_list_items, parent, false);
        return new DisplayRecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DisplayRecipeViewHolder holder, int position) {
        String object = items.get(position);

        holder.text.setText(object);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}