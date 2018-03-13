package com.kfoszcz.makaoscore.view;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kfoszcz.makaoscore.R;
import com.kfoszcz.makaoscore.data.MakaoDatabase;
import com.kfoszcz.makaoscore.data.Player;
import com.kfoszcz.makaoscore.data.Score;
import com.kfoszcz.makaoscore.logic.AddScoreController;

import java.util.List;

public class AddScoreActivity extends AppCompatActivity implements AddScoreInterface {

    private EditText dealNumber;
    private RecyclerView recyclerView;
    private LayoutInflater layoutInflater;
    private AddScoreController controller;
    private PlayerScoreAdapter adapter;
    private int gameId;
    private int dealId;

    private List<Player> playerList;

    private static int[] buttonColors = {
            R.color.btnDefault,
            R.color.btnFail,
            R.color.btnHalf,
            R.color.btnHalf,
            R.color.btnSuccess
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_score);

        layoutInflater = getLayoutInflater();
        dealNumber = findViewById(R.id.inp_add_score_deal);
        recyclerView = findViewById(R.id.lst_add_score_list);

        controller = new AddScoreController(
                this,
                MakaoDatabase.getDatabase(getApplicationContext()).dao()
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameId = getIntent().getIntExtra("gameId", 0);
        dealId = getIntent().getIntExtra("dealId", 0);

        controller.getPlayerList(gameId);
    }

    public void setUpInputList(List<Player> players) {
        playerList = players;

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new PlayerScoreAdapter();
        recyclerView.setAdapter(adapter);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(
                recyclerView.getContext(),
                layoutManager.getOrientation()
        );

        itemDecoration.setDrawable(
                ContextCompat.getDrawable(
                        AddScoreActivity.this,
                        R.drawable.divider_horizontal
                )
        );

        recyclerView.addItemDecoration(itemDecoration);

    }

    private class PlayerScoreAdapter
            extends RecyclerView.Adapter<PlayerScoreAdapter.PlayerScoreViewHolder> {

        @Override
        public PlayerScoreAdapter.PlayerScoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = layoutInflater.inflate(R.layout.item_add_score, parent, false);
            return new PlayerScoreViewHolder(v);
        }

        @Override
        public void onBindViewHolder(PlayerScoreAdapter.PlayerScoreViewHolder holder, int position) {
            Player currentPlayer = playerList.get(position);
            holder.initial.setText(currentPlayer.getInitial());
        }

        @Override
        public int getItemCount() {
            return playerList.size();
        }

        class PlayerScoreViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private ImageButton[] buttons;
            private TextView initial;
            private int scoreType = Score.SCORE_NONE;

            public PlayerScoreViewHolder(View itemView) {
                super(itemView);

                initial = itemView.findViewById(R.id.item_add_score_initial);
                buttons = new ImageButton[5];
                buttons[4] = itemView.findViewById(R.id.item_add_score_btn_success);
                buttons[3] = itemView.findViewById(R.id.item_add_score_btn_half_high);
                buttons[2] = itemView.findViewById(R.id.item_add_score_btn_half_low);
                buttons[1] = itemView.findViewById(R.id.item_add_score_btn_fail);

                for (int i = 1; i < 5; i++) {
                    buttons[i].setOnClickListener(this);
                }
            }

            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.item_add_score_btn_success:
                        toggleScoreType(Score.SCORE_SUCCESS);
                        break;

                    case R.id.item_add_score_btn_half_high:
                        toggleScoreType(Score.SCORE_HALF_HIGH);
                        break;

                    case R.id.item_add_score_btn_half_low:
                        toggleScoreType(Score.SCORE_HALF_LOW);
                        break;

                    case R.id.item_add_score_btn_fail:
                        toggleScoreType(Score.SCORE_FAIL);
                        break;
                }
            }

            public void toggleScoreType(int newType) {
                // set old score type button to default color
                if (scoreType != Score.SCORE_NONE)
                    buttons[scoreType].setBackgroundColor(getResources().getColor(buttonColors[0]));

                if (newType == scoreType) {
                    scoreType = Score.SCORE_NONE;
                }
                else {
                    buttons[newType].setBackgroundColor(getResources().getColor(buttonColors[newType]));
                    scoreType = newType;
                }
            }
        }
    }

}
