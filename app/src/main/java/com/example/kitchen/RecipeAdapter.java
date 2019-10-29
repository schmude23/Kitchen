package com.example.kitchen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private List<Recipe> recipes;
    private OnClickListener listener;
    public RecipeAdapter(List<Recipe> houses, OnClickListener listener) {
        super();
        this.recipes = houses;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list_item, parent, false);
        return new RecipeViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        holder.bind(this.recipes.get(position));
    }

    @Override
    public int getItemCount() {
        return this.recipes.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView recipe;
        private TextView servings;
        private TextView prep_time;
        private TextView total_time;
        OnClickListener listener;

        public RecipeViewHolder(View view, OnClickListener listener) {
            //super(LayoutInflater.from(container.getContext()).inflate(R.layout.recipe_list_item, container, false));
            super(view);
            recipe = (TextView) itemView.findViewById(R.id.text_recipe_name);
            this.listener = listener;
            view.setOnClickListener(this);
            //servings = (TextView) itemView.findViewById(R.id.recipe_servings);
            //prep_time = (TextView) itemView.findViewById(R.id.total_rooms_text);
        }

        public void bind(Recipe recipe) {
            this.recipe.setText(recipe.getTitle());
            //prep_time.setText(Integer.toString(recipe.getRooms()));
            //servings.setText(Integer.toString(recipe.getRoomsAvail()) );
        }

        @Override
        public void onClick(View v) {
            listener.onClick(getAdapterPosition());
        }
    }

    public interface OnClickListener {
        void onClick(int position);
    }
}