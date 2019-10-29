package com.example.kitchen;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

public class EditIngredientActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Button btnAddIngredient, btnNext;
    private EditText editIngredient;
    private ListView ingredientListView;
    ArrayList<String> ingredientList = new ArrayList<String>();
    ArrayAdapter<String> ingredientAdapter;
    Dialog myDialog;
    String ingredient_quantity, ingredient_unit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_ingredient);
        myDialog = new Dialog(this);

        btnAddIngredient = (Button) findViewById(R.id.add_ingredient_btn);
        btnAddIngredient.setOnClickListener(this);
        btnNext = (Button) findViewById(R.id.next_btn);
        btnNext.setOnClickListener(this);

        editIngredient = (EditText) findViewById(R.id.edit_text_ingredient);
        ingredientAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, ingredientList);

        // set the ingredientListView variable to your ingredientList in the xml
        ingredientListView = (ListView) findViewById(R.id.ingredient_list);
        ingredientListView.setAdapter(ingredientAdapter);

    }

    public void onClick(View v) {
        String input;
        switch (v.getId()) {

            case R.id.add_ingredient_btn:
                input = editIngredient.getText().toString();
                if (input.length() > 0) {
                    ShowPopup(v);
                }
                break;

            case R.id.next_btn:
                Intent intent = new Intent(this, EditDirectionActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }

    }

    public void ShowPopup (View v) {
        final EditText edit_name, edit_quantity ;
        Button btnOkay;

        myDialog.setContentView(R.layout.edit_ingredient_popup);
        edit_name = myDialog.findViewById(R.id.ingredient_text);
        edit_name.setText(editIngredient.getText().toString());
        edit_quantity = myDialog.findViewById(R.id.ingredient_quantity_text);

        btnOkay = myDialog.findViewById(R.id.button_okay);



        Spinner spinner = myDialog.findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.units_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        btnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingredient_quantity = edit_quantity.getText().toString();
                // add string to the ingredientAdapter, not the listview

                ingredientAdapter.add(edit_name.getText().toString() + " (" + ingredient_quantity + " " + ingredient_unit + ")" );
                // no need to call ingredientAdapter.notifyDataSetChanged(); as it is done by the ingredientAdapter.add() method
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    // Spinner Methods
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // An item was selected. You can retrieve the selected item using
        ingredient_unit = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
