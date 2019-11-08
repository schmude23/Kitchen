package com.example.kitchen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class ShareRecipeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_recipe);

        //TODO: Register as a wifip2p listener


    }

    //TODO: add some kind of list to the activity with all devices that are currently broadcasting
    //TODO: On click for one of these, use the wifip2p connect method to connect to the device
    //              In method you will need to get a response and do some fancy stuff.
    //              See: https://developer.android.com/guide/topics/connectivity/wifip2p

    public void onShareButtonPressed(View view) {

        //TODO: Figure out how to get the user's selected recipes that they want to share
        //TODO: Broadcast that you want to share stuff
        //          You will need to set up a listener while doing this which will see when stuff
        //          is connected and will transmit the data.
        //          You will also need to convert all the recipes into a text format and back.

    }


}
