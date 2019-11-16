package com.example.kitchen;

import android.content.ClipData;
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

class ShoppingCartAdapter extends RecyclerView.Adapter {

    interface OnItemCheckListener {
        void onItemCheck(ShoppingCartItem item);
        void onItemUncheck(ShoppingCartItem item);
    }
    @NonNull
    private OnItemCheckListener onItemCheckListener;


    private List<ShoppingCartItem> shoppingCart;
    //private OnClickListener listener;


    public ShoppingCartAdapter(List<ShoppingCartItem> shoppingCart, @NonNull OnItemCheckListener listener) {
        super();
        this.shoppingCart = shoppingCart;
        onItemCheckListener = listener;
    }


    @Override
    public ShoppingCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_cart_list_item, parent, false);
        return new ShoppingCartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        //holder.bind(this.shoppingCart.get(position));
        if(holder instanceof ShoppingCartViewHolder){
            final ShoppingCartItem currentItem = shoppingCart.get(position);
            ((ShoppingCartViewHolder) holder).bind(this.shoppingCart.get(position));

            ((ShoppingCartViewHolder) holder).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ShoppingCartViewHolder) holder).ingredient.setChecked(
                            !((ShoppingCartViewHolder) holder).ingredient.isChecked());
                    if (((ShoppingCartViewHolder) holder).ingredient.isChecked()) {
                        onItemCheckListener.onItemCheck(currentItem);
                    } else {
                        onItemCheckListener.onItemUncheck(currentItem);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {

        try{
            return this.shoppingCart.size();
        }catch (NullPointerException e){
            return 0;
        }

    }

    static class ShoppingCartViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        CheckBox ingredient;
        TextView quantity;
        TextView unit;

        public ShoppingCartViewHolder(View view) {
            super(view);
            itemView = view;
            ingredient = itemView.findViewById(R.id.ingredient_check_box);
            quantity = itemView.findViewById(R.id.ingredient_quantity_text);
            unit = itemView.findViewById(R.id.ingredient_unit_text);
            ingredient.setClickable(false);
            //ingredient.setChecked(false);
        }

        public void bind(ShoppingCartItem shoppingCartItem) {
            ingredient.setText(shoppingCartItem.getName());
            quantity.setText(String.valueOf(shoppingCartItem.getQuantity()));
            unit.setText(shoppingCartItem.getUnit());
            ingredient.setChecked(false);


        }

        public void setOnClickListener(View.OnClickListener onClickListener){
            itemView.setOnClickListener(onClickListener);
        }


    }

    public interface OnClickListener {
        void onClick(int position);
    }
}
