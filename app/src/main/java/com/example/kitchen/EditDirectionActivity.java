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

public class EditDirectionActivity extends AppCompatActivity implements OnClickListener {

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
    }*/
    private Button btnAddDirection, btnNext;
    private EditText editDirection;
    private ListView directionListView;
    ArrayList<String> directionList = new ArrayList<String>();
    ArrayAdapter<String> directionAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_direction);


        btnAddDirection = (Button)findViewById(R.id.add_direction_btn);
        btnAddDirection.setOnClickListener(this);
        btnNext = (Button)findViewById(R.id.next_btn);
        btnNext.setOnClickListener(this);
        editDirection = (EditText)findViewById(R.id.edit_text_direction);
        directionAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, directionList);

        // set the ingredientListView variable to your ingredientList in the xml
        directionListView =(ListView)findViewById(R.id.direction_list);
        directionListView.setAdapter(directionAdapter);

    }
    public void onClick(View v)
    {
        String input;
        switch (v.getId()) {
            case R.id.add_direction_btn:
                input = editDirection.getText().toString();
                if(input.length() > 0)
                {
                    // add string to the ingredientAdapter, not the listview
                    directionAdapter.add(input);
                    // no need to call ingredientAdapter.notifyDataSetChanged(); as it is done by the ingredientAdapter.add() method
                }
                break;
            case R.id.next_btn:
                Intent intent = new Intent(this, EditTimerActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }

    }
}
