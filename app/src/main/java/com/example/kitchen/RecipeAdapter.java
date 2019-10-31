package com.example.kitchen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private List<Recipe> recipes;
    private OnClickListener listener;
    private Context context;
    public RecipeAdapter(List<Recipe> recipes, OnClickListener listener, Context context) {
        super();
        this.recipes = recipes;
        this.listener = listener;
        this.context = context;
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

        try{
          return  this.recipes.size();
        }catch (NullPointerException e){
            return 0;
        }

    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView recipe;
        private TextView servings;
        private TextView prep_time;
        private TextView total_time;
        OnClickListener listener;
        private ImageView image;

        public RecipeViewHolder(View view, OnClickListener listener) {
            super(view);
            recipe = (TextView) itemView.findViewById(R.id.text_recipe_name);
            this.listener = listener;
            view.setOnClickListener(this);
            servings = (TextView) itemView.findViewById(R.id.recipe_servings);
            prep_time = (TextView) itemView.findViewById(R.id.recipe_prep_time);
            total_time = (TextView) itemView.findViewById(R.id.recipe_total_time);
            image = itemView.findViewById(R.id.recipe_image);
        }

        public void bind(Recipe recipe) {
            this.recipe.setText(recipe.getTitle());
            servings.setText(String.valueOf(recipe.getServings()));
            String text = recipe.getPrep_time() + " min";
            prep_time.setText(text);
            text = recipe.getTotal_time() + " min";
            total_time.setText(text);
            image.setImageBitmap(recipe.getImage(context));

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