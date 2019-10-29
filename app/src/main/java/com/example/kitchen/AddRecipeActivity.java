package com.example.kitchen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.view.View.OnClickListener;

import java.util.ArrayList;

public class AddRecipeActivity extends AppCompatActivity implements OnClickListener {

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
    }*/
    private Button btnNext;
    private EditText editTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        btnNext = (Button) findViewById(R.id.next_btn);
        btnNext.setOnClickListener(this);
        editTitle = (EditText) findViewById(R.id.title_edit);

    }

    public void onClick(View v) {
        String input = editTitle.getText().toString();
        if (input.length() > 0) {
            Intent intent = new Intent(this, EditIngredientActivity.class);
            startActivity(intent);
        }


    }
}
