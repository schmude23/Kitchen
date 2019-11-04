package com.example.kitchen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * This class is the Adapter used for displaying Recipes in a Recycler view
 */
class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.ShoppingCartViewHolder> {
    private List<ShoppingCartItem> shoppingCart;
    private OnClickListener listener;

    /**
     * This constructor sets up the recipe adapter with everything it needs.
     *
     * @param shoppingCart The set of recipes for the adapter
     * @param listener The onClickListener to be used when a recipe is clicked on
     */
    public ShoppingCartAdapter(List<ShoppingCartItem> shoppingCart, OnClickListener listener) {
        super();
        this.shoppingCart = shoppingCart;
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
    public ShoppingCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_cart_list_item, parent, false);
        return new ShoppingCartViewHolder(view, listener);
    }

    /**
     * This method is run when the view holder is bound to the recycler view
     *
     * @param holder the view holder
     * @param position it's position in the dataset
     */
    @Override
    public void onBindViewHolder(@NonNull ShoppingCartViewHolder holder, int position) {
        holder.bind(this.shoppingCart.get(position));
    }

    /**
     * This method gets the size of the dataset
     *
     * @return the size of the dataset
     */
    @Override
    public int getItemCount() {

        try{
            return this.shoppingCart.size();
        }catch (NullPointerException e){
            return 0;
        }

    }

    /**
     * This class is the controller for an individual viewHolder in the RecipeAdapter
     */
    public class ShoppingCartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CheckBox ingredient;
        TextView quantity;
        TextView unit;
        /**
         * This method is the constructor. Setup of the viewHolder takes place here.
         *
         * @param view
         * @param listener
         */
        public ShoppingCartViewHolder(View view, OnClickListener listener) {
            super(view);
            ingredient = itemView.findViewById(R.id.ingredient_check_box);
            quantity = itemView.findViewById(R.id.ingredient_quantity_text);
            unit = itemView.findViewById(R.id.ingredient_unit_text);
            view.setOnClickListener(this);
        }

        /**
         * This method fills the data of the viewholder based on the recipe. Used when binding the viewholder
         *
         * @param shoppingCartItem
         */
        public void bind(ShoppingCartItem shoppingCartItem) {
            ingredient.setText(shoppingCartItem.getName());
            quantity.setText(String.valueOf(shoppingCartItem.getQuantity()));
            unit.setText(shoppingCartItem.getUnit());

        }

        /**
         * This method runs when the adapter is clicked on.
         *
         * @param v the view clicked on
         */
       @Override
        public void onClick(View v) {
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