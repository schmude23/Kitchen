package com.example.kitchen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    List<Recipe> recipes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mRecyclerView = findViewById(R.id.recipe_list_recycler);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        recipes = new ArrayList<>();
        for(int i = 0; i < 100; i++)
        {
            Recipe recipe = new Recipe();
            recipe.setRecipe("Recipe" + i);
            recipes.add(recipe);
        }
        mRecyclerView.setAdapter(new RecipeAdapter(recipes));
    }
    class RecipeAdapter extends RecyclerView.Adapter<RecipeViewHolder> {
        private List<Recipe> mHouse;
        public RecipeAdapter(List<Recipe> houses) {
            super();
            this.mHouse = houses;
        }

        @NonNull
        @Override
        public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new RecipeViewHolder(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
            holder.bind(this.mHouse.get(position));
        }

        @Override
        public int getItemCount() {
            return this.mHouse.size();
        }
    }
    class RecipeViewHolder extends RecyclerView.ViewHolder{
        private TextView recipe;
        private TextView servings;
        private TextView prep_time;
        private TextView total_time;

        public RecipeViewHolder(ViewGroup container){
            super(LayoutInflater.from(MainActivity.this).inflate(R.layout.recipe_list_item, container, false));
            recipe = (TextView) itemView.findViewById(R.id.text_recipe_name);
            //servings = (TextView) itemView.findViewById(R.id.recipe_servings);
            //prep_time = (TextView) itemView.findViewById(R.id.total_rooms_text);
        }
        public void bind(Recipe recipe){
            this.recipe.setText(recipe.getRecipe());
            //prep_time.setText(Integer.toString(recipe.getRooms()));
            //servings.setText(Integer.toString(recipe.getRoomsAvail()) );
        }
    }

class Recipe {
    private String recipe;
    private String servings;
    private String prep_time;
    private String total_time;

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public String getServings() {
        return servings;
    }

    public void setServings(String servings) {
        this.servings = servings;
    }

    public String getPrep_time() {
        return prep_time;
    }

    public void setPrep_time(String prep_time) {
        this.prep_time = prep_time;
    }

    public String getTotal_time() {
        return total_time;
    }

    public void setTotal_time(String total_time) {
        this.total_time = total_time;
    }
}
    // Toolbar functions
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.item1:
                return true;

            case R.id.item2:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
