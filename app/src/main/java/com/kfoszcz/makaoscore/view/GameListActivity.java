package com.kfoszcz.makaoscore.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.kfoszcz.makaoscore.R;

public class GameListActivity extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton addGameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);

        addGameButton = findViewById(R.id.fab_game_list_add);
        addGameButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == addGameButton.getId()) {
            Intent intent = new Intent(this, PlayerListActivity.class);
            startActivity(intent);
        }
    }
}
