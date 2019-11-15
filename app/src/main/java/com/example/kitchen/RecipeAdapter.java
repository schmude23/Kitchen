package com.example.kitchen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is the Adapter used for displaying Recipes in a Recycler view
 */
class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private List<RecipeListItem> recipes;
    private OnClickListener listener;

    /**
     * This constructor sets up the recipe adapter with everything it needs.
     *
     * @param recipes The set of recipes for the adapter
     * @param listener The onClickListener to be used when a recipe is clicked on
     */
    public RecipeAdapter(List<RecipeListItem> recipes, OnClickListener listener) {
        super();
        this.recipes = recipes;
        this.listener = listener;
    }

    /**
     * This method is run when the viewHolder for each cell is created
     *
     * @param parent the parent view
     * @param viewType an int referencing the type of view
     * @return
     */
    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list_item, parent, false);
        return new RecipeViewHolder(view, listener);
    }

    /**
     * This method is run when the view holder is bound to the recycler view
     *
     * @param holder the view holder
     * @param position it's position in the dataset
     */
    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        holder.bind(this.recipes.get(position));
    }

    /**
     * This method gets the size of the dataset
     *
     * @return the size of the dataset
     */
    @Override
    public int getItemCount() {

        try{
          return this.recipes.size();
        }catch (NullPointerException e){
            return 0;
        }

    }
    public void setFilter(List<RecipeListItem> filteredRecipeList) {
        recipes = new ArrayList<>();

        recipes.addAll(filteredRecipeList);
        notifyDataSetChanged();
       notifyItemRangeChanged(0, recipes.size());
    }

    /**
     * This class is the controller for an individual viewHolder in the RecipeAdapter
     */
    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView recipe_name;
        private TextView servings;
        private TextView prep_time;
        private TextView total_time;
        OnClickListener listener;
        public ImageView image;
        private ImageView favorite;

        /**
         * This method is the constructor. Setup of the viewHolder takes place here.
         *
         * @param view
         * @param listener
         */
        public RecipeViewHolder(View view, OnClickListener listener) {
            super(view);
            recipe_name = (TextView) itemView.findViewById(R.id.text_recipe_name);
            this.listener = listener;
            view.setOnClickListener(this);
            servings = (TextView) itemView.findViewById(R.id.recipe_servings);
            prep_time = (TextView) itemView.findViewById(R.id.recipe_prep_time);
            total_time = (TextView) itemView.findViewById(R.id.recipe_total_time);
            image = itemView.findViewById(R.id.recipe_image);
            favorite = itemView.findViewById(R.id.recipe_favorite_image);
        }

        /**
         * This method fills the data of the viewholder based on the recipe. Used when binding the viewholder
         *
         * @param recipe
         */
        public void bind(RecipeListItem recipe) {
            recipe_name.setText(recipe.getRecipeName());
            servings.setText(recipe.getServings());
            prep_time.setText(recipe.getPrepTime());
            total_time.setText(recipe.getTotalTime());
            image.setImageBitmap(recipe.getImage());
            if(recipe.getFavorited()) {
                favorite.setImageResource(R.drawable.ic_favorite);
            }
            else{
                favorite.setImageResource(R.drawable.ic_favorite_outline);
            }

        }

        /**
         * This method runs when the adapter is clicked on.
         *
         * @param v the view clicked on
         */
        @Override
        public void onClick(View v) {
//            recipes.notifyAll();
            listener.onClick(getAdapterPosition());
        }
    }

    /**
     * This interface defines the type of onClickListener the recipeViewHolder needs.
     */
    public interface OnClickListener {
        void onClick(int position);
    }
}