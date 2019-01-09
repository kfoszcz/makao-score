package com.kfoszcz.makaoscore.view;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.kfoszcz.makaoscore.R;
import com.kfoszcz.makaoscore.data.MakaoDatabase;
import com.kfoszcz.makaoscore.data.PlayerGroup;
import com.kfoszcz.makaoscore.data.PlayerWithStats;
import com.kfoszcz.makaoscore.logic.StatsController;

import java.util.List;

public class StatsActivity extends AppCompatActivity implements
    StatsViewInterface, AdapterView.OnItemSelectedListener {

    private StatsController controller;
    private Spinner spinner;
    private StatsAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        controller = new StatsController(
            this,
            MakaoDatabase.getDatabase(getApplicationContext()).dao()
        );
        spinner = findViewById(R.id.stats_spinner);
        spinner.setOnItemSelectedListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.stats_rec);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(
            recyclerView.getContext(),
            layoutManager.getOrientation()
        );

        itemDecoration.setDrawable(
            ContextCompat.getDrawable(
                StatsActivity.this,
                R.drawable.divider_score_list
            )
        );

        recyclerView.addItemDecoration(itemDecoration);

        adapter = new StatsAdapter(this);
        recyclerView.setAdapter(adapter);

        controller.loadPlayerGroups();
    }

    @Override
    public void setupPlayerGroupSpinner(List<PlayerGroup> groups) {
        ArrayAdapter<PlayerGroup> adapter = new ArrayAdapter<>(
            this, android.R.layout.simple_spinner_item, groups
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    @Override
    public void setupStatsList(List<PlayerWithStats> stats) {
        adapter.setStats(stats);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        PlayerGroup group = (PlayerGroup) adapterView.getItemAtPosition(i);
        String ids = group.getIds();
        controller.loadStats(ids);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Toast.makeText(this, "Nothing selected", Toast.LENGTH_LONG).show();
    }
}
