package com.example.kitchen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ViewProfileActivity extends AppCompatActivity {

    String username;
    int userID;
    Button mButtonDeleteProfile;
    Button mButtonEditProfile;
    DatabaseHelper database;
    private SearchView searchView;
    private RecipeAdapter recipeAdapter;
    Boolean filter = false;
    List<RecipeListItem> filteredList;
    private List<RecipeListItem> recipeListItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity();


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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
        mainActivity();
    }

    public void mainActivity() {
        setContentView(R.layout.activity_view_profile);

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        database = new DatabaseHelper(this);

        username = getIntent().getStringExtra("username");
        userID = getIntent().getIntExtra("userID", -1);

        mButtonEditProfile = findViewById(R.id.button_edit_profile);
        mButtonDeleteProfile = findViewById(R.id.button_delete_profile);

        TextView profile_name = findViewById(R.id.profile_name_view);
        profile_name.setText(username);

        mButtonDeleteProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.deleteUser(userID);
                Toast toast = Toast.makeText(ViewProfileActivity.this, "Profile Deleted", Toast.LENGTH_SHORT);
                toast.setGravity(0, 0, 500);
                toast.show();
                Intent i = new Intent(ViewProfileActivity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        mButtonEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(ViewProfileActivity.this,EditProfileActivity.class);
                startActivity(registerIntent);
                finish();
            }
        });



    }

}