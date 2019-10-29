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

public class EditTimerActivity extends AppCompatActivity implements OnClickListener {


    private Button btnAddTimer, btnNext;
    private EditText editTimer;
    private ListView timerListView;
    ArrayList<String> timerList = new ArrayList<String>();
    ArrayAdapter<String> ingredientAdapter, directionAdapter, timerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_timer);

        btnAddTimer = (Button)findViewById(R.id.add_timer_btn);
        btnAddTimer.setOnClickListener(this);
        btnNext = (Button)findViewById(R.id.next_btn);
        btnNext.setOnClickListener(this);

        editTimer = (EditText)findViewById(R.id.edit_text_timer);
        timerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, timerList);

        // set the ingredientListView variable to your ingredientList in the xml
        timerListView =(ListView)findViewById(R.id.timer_list);
        timerListView.setAdapter(timerAdapter);
    }
    public void onClick(View v)
    {
        String input;
        switch (v.getId()) {

            case R.id.add_timer_btn:
                input = editTimer.getText().toString();
                if(input.length() > 0)
                {
                    // add string to the ingredientAdapter, not the listview
                    timerAdapter.add(input);
                    // no need to call ingredientAdapter.notifyDataSetChanged(); as it is done by the ingredientAdapter.add() method
                }
                break;
            case R.id.next_btn:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }

    }
}
