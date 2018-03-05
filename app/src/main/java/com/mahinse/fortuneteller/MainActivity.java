package com.mahinse.fortuneteller;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.mahinse.fortunetellerview.Direction;
import com.mahinse.fortunetellerview.FortuneTellerListener;
import com.mahinse.fortunetellerview.FortuneTellerView;

public class MainActivity extends Activity implements FortuneTellerListener {
    private static final String TAG = "MainActivity";

    private FortuneTellerView fortuneTellerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fortuneTellerView = findViewById(R.id.fortune_view);
        fortuneTellerView.setFortuneTellerListener(this);
    }

    @Override
    public void onOptionChosen(Direction direction) {
        Log.d(TAG, "onOptionChosen: direction=" + direction);
        //ToDo: add logic on direction taken

        //If there is any submenus, set the new choices
        fortuneTellerView.setChoices("New 1", "New 2", "New 3", "New 4");
    }
}
