package com.kfoszcz.makaoscore.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.kfoszcz.makaoscore.R;

public class GameListActivity extends AppCompatActivity implements View.OnClickListener {

    private Button playerListButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);

        playerListButton = findViewById(R.id.btn_game_list_players);
        playerListButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == playerListButton.getId()) {
            Intent intent = new Intent(this, PlayerListActivity.class);
            startActivity(intent);
        }
    }
}
