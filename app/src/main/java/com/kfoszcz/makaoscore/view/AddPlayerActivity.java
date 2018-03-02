package com.kfoszcz.makaoscore.view;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kfoszcz.makaoscore.R;
import com.kfoszcz.makaoscore.data.MakaoDatabase;
import com.kfoszcz.makaoscore.data.Player;
import com.kfoszcz.makaoscore.logic.AddPlayerController;

public class AddPlayerActivity extends AppCompatActivity
        implements AddPlayerInterface, View.OnClickListener {

    private TextInputEditText playerInitial;
    private TextInputEditText playerName;
    private Button confirmButton;
    private Player editedPlayer;

    private AddPlayerController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_player);

        playerInitial = findViewById(R.id.inp_add_player_initial);
        playerName = findViewById(R.id.inp_add_player_name);
        confirmButton = findViewById(R.id.btn_add_player_confirm);

        confirmButton.setOnClickListener(this);

        controller = new AddPlayerController(
                this,
                MakaoDatabase.getDatabase(getApplicationContext()).dao()
        );

        int playerId = getIntent().getIntExtra("playerId", 0);
        if (playerId != 0) {
            controller.getPlayerById(playerId);
            confirmButton.setText(R.string.add_player_save);
            setTitle(R.string.add_player_edit);
        }
    }

    @Override
    public void setFormFields(Player player) {
        editedPlayer = player;

        playerInitial.setText(player.getInitial());
        playerName.setText(player.getName());
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public void showErrorToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == confirmButton.getId()) {
            if (editedPlayer == null) {
                editedPlayer = new Player(
                        playerInitial.getText().toString(),
                        playerName.getText().toString()
                );
            }
            else {
                editedPlayer.setInitial(playerInitial.getText().toString());
                editedPlayer.setName(playerName.getText().toString());
            }
            controller.insertPlayer(editedPlayer);
        }
    }
}
