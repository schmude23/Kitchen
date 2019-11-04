package com.example.kitchen;

        import android.content.Intent;
        import android.os.Bundle;

        import androidx.appcompat.app.AppCompatActivity;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import java.util.ArrayList;
        import java.util.List;

/**
 * This code controls the Shopping Cart Activity
 */
public class ShoppingCartActivity extends AppCompatActivity {
    private DatabaseHelper database = new DatabaseHelper(this);
    private Recipe recipe;
    private List<ShoppingCartItem> ingredientsToBeAdded; // Recipe ingredients to be added to shopping cart
    private List<ShoppingCartItem> shoppingCart; // Current shopping cart
    private RecyclerView recyclerView;
    /**
     * This method is run when the activity is created
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        int recipeId = getIntent().getIntExtra("recipeId", -1);
        recipe = database.getRecipe(recipeId);
        getRecipeIngredients();

        recyclerView = findViewById(R.id.shopping_cart_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        ShoppingCartAdapter shoppingCartAdapter = new ShoppingCartAdapter(ingredientsToBeAdded, null);
        recyclerView.setAdapter(shoppingCartAdapter);
    }

    /**
     *
     */
    private void getRecipeIngredients(){
        List<RecipeIngredient> recipeIngredients = recipe.getIngredientList();
        ingredientsToBeAdded = new ArrayList<ShoppingCartItem>();
        for(int i = 0; i < recipeIngredients.size(); i++){
            String ingredient = database.getIngredient(recipeIngredients.get(i).getIngredientID()).getName();
            double quantity = recipeIngredients.get(i).getQuantity();
            String unit = recipeIngredients.get(i).getUnit();
            ingredientsToBeAdded.add(new ShoppingCartItem(ingredient, quantity, unit));
        }
    }

    /**
     *
     */
    private void getShoppingCart(){
        //shoppingCart = database.getShoppingCartIngredients();
    }
}
