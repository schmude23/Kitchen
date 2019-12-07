package com.example.kitchen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class PasteRecipeActivity extends AppCompatActivity {
    DatabaseHelper database = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paste_recipe);

        // Display Toolbar
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

    }

    /**
     * This method is run when the options menu is being created and is where the menu is set.
     *
     * @param menu the menu that was created
     */
    // Toolbar functions
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * This method is run when an item from the options menu is selected
     *
     * @param item the item that was selected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_login:
                Intent login = new Intent(this, LoginActivity.class);
                startActivity(login);
                this.finish();
                return true;
            case R.id.advanced_search_item:
                Intent advancedSearch = new Intent(this, AdvancedSearchActivity.class);
                startActivity(advancedSearch);
                this.finish();
                return true;
            case R.id.action_add_recipe:
                Intent addRecipe = new Intent(this, EditRecipeActivity.class);
                addRecipe.putExtra("recipeId", -1); // New recipe
                addRecipe.putExtra("newRecipe", true); // New recipe
                startActivity(addRecipe);
                this.finish();
                return true;
            case R.id.action_view_cart:
                Intent viewCart = new Intent(this, ShoppingCartActivity.class);
                viewCart.putExtra("recipeId", -1);
                startActivity(viewCart);
                this.finish();
                return true;
            case R.id.action_home:
                Intent home = new Intent(this, MainActivity.class);
                startActivity(home);
                this.finish();
                return true;
            case R.id.action_ingredient_list:
                Intent intent = new Intent(this, IngredientListActivity.class);
                startActivity(intent);
                this.finish();
                return true;
            case R.id.action_retrieve_recipe:
                Intent retrieveRecipe = new Intent(this, ShareRecipeActivity.class);
                retrieveRecipe.putExtra("recipeId", -1);
                startActivity(retrieveRecipe);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onAddRecipeClicked(View v) {
        RecipeCopyPasteCreator paste = new RecipeCopyPasteCreator(this);
        EditText pastedText = findViewById(R.id.paste_edit_text);
        int recipeId = paste.main(pastedText.toString());
        if (recipeId == -1) {
            Toast.makeText(this, "Error Adding Recipe", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(this, DisplaySelectedRecipeActivity.class);
            intent.putExtra("recipeId", recipeId);
            startActivity(intent);
            this.finish();
        }
    }
}
