package com.kfoszcz.makaoscore.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Spinner;

import com.kfoszcz.makaoscore.R;

public class StatsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        Spinner spinner = findViewById(R.id.stats_spinner);
    }

}
