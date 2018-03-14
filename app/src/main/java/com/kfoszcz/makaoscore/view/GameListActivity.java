package com.kfoszcz.makaoscore.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kfoszcz.makaoscore.R;
import com.kfoszcz.makaoscore.data.Game;
import com.kfoszcz.makaoscore.data.GameWithPlayers;
import com.kfoszcz.makaoscore.data.MakaoDatabase;
import com.kfoszcz.makaoscore.logic.GameListController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class GameListActivity extends AppCompatActivity implements View.OnClickListener, GameViewInterface {

    private GameListController controller;
    private FloatingActionButton addGameButton;
    private RecyclerView recyclerView;
    private LayoutInflater layoutInflater;
    private GameAdapter adapter;

    private List<GameWithPlayers> gameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);

        addGameButton = findViewById(R.id.fab_game_list_add);
        addGameButton.setOnClickListener(this);

        recyclerView = findViewById(R.id.rec_game_list);
        layoutInflater = getLayoutInflater();

        controller = new GameListController(
                this,
                MakaoDatabase.getDatabase(getApplicationContext()).dao()
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        controller.getGameList();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == addGameButton.getId()) {
            Intent intent = new Intent(this, PlayerListActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void setUpGameList(List<GameWithPlayers> games) {
        gameList = games;

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new GameAdapter();
        recyclerView.setAdapter(adapter);

        if (recyclerView.getItemDecorationAt(0) == null) {
            DividerItemDecoration itemDecoration = new DividerItemDecoration(
                    recyclerView.getContext(),
                    layoutManager.getOrientation()
            );

            itemDecoration.setDrawable(
                    ContextCompat.getDrawable(
                            GameListActivity.this,
                            R.drawable.divider_horizontal
                    )
            );

            recyclerView.addItemDecoration(itemDecoration);
        }
    }

    @Override
    public void startScoreListActivity(int gameId) {
        Intent intent = new Intent(this, ScoreListActivity.class);
        intent.putExtra("gameId", gameId);
        startActivity(intent);
    }

    private class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {

        @Override
        public GameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = layoutInflater.inflate(R.layout.item_game_list, parent, false);
            return new GameViewHolder(v);
        }

        @Override
        public void onBindViewHolder(GameViewHolder holder, int position) {
            GameWithPlayers currentGame = gameList.get(position);
            DateFormat format = new SimpleDateFormat(
                    "EEE, MMM d, HH:mm",
                    getResources().getConfiguration().locale
            );
            holder.date.setText(format.format(currentGame.game.getStartDate()));
            holder.playerCount.setText(Integer.toString(currentGame.playerCount));
        }

        @Override
        public int getItemCount() {
            return gameList.size();
        }

        class GameViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private TextView date;
            private TextView playerCount;

            public GameViewHolder(View itemView) {
                super(itemView);
                date =  itemView.findViewById(R.id.txt_game_item_date);
                playerCount = itemView.findViewById(R.id.txt_game_item_players);

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                controller.gameRowClicked(
                        gameList.get(this.getAdapterPosition()).game
                );
            }
        }

    }

}
