package com.kfoszcz.makaoscore.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kfoszcz.makaoscore.R;
import com.kfoszcz.makaoscore.data.GameWithPlayers;
import com.kfoszcz.makaoscore.data.MakaoDatabase;
import com.kfoszcz.makaoscore.logic.GameListController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class GameListActivity extends AppCompatActivity implements GameViewInterface {

    private GameListController controller;
    private RecyclerView recyclerView;
    private LayoutInflater layoutInflater;
    private GameAdapter adapter;

    private List<GameWithPlayers> gameList;
    private boolean swipeToDelete;

    private static int recyclerViewPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);
        setTitle(R.string.game_list);

        recyclerView = findViewById(R.id.rec_game_list);
        layoutInflater = getLayoutInflater();

        controller = new GameListController(
                this,
                MakaoDatabase.getDatabase(getApplicationContext()).dao()
        );

        swipeToDelete = false;
        recyclerViewPosition = 0;
    }

    @Override
    protected void onPause() {
        super.onPause();
        recyclerViewPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                .findFirstCompletelyVisibleItemPosition();
    }

    @Override
    protected void onResume() {
        super.onResume();
        controller.getGameList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_game_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_game_list_delete:
                swipeToDelete = !swipeToDelete;
                if (swipeToDelete)
                    item.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_delete_red_48dp));
                else
                    item.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_delete_white_48dp));
                return true;

            case R.id.menu_game_list_add:
                Intent intent = new Intent(this, PlayerListActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);

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

        ItemTouchHelper touchHelper = new ItemTouchHelper(createTouchHelperCallback());
        touchHelper.attachToRecyclerView(recyclerView);

        if (recyclerViewPosition > gameList.size())
            recyclerViewPosition = gameList.size();
        recyclerView.scrollToPosition(recyclerViewPosition);
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
            private View root;

            public GameViewHolder(View itemView) {
                super(itemView);
                root = itemView;
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

    ItemTouchHelper.Callback createTouchHelperCallback() {
        return new ItemTouchHelper.SimpleCallback(
                0,
                0
        ) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                controller.deleteGame(gameList.get(position).game);

                gameList.remove(position);
                adapter.notifyItemRemoved(position);
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return swipeToDelete ? ItemTouchHelper.LEFT : 0;
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                    if (viewHolder instanceof GameAdapter.GameViewHolder) {
                        GameAdapter.GameViewHolder holder = (GameAdapter.GameViewHolder) viewHolder;
                        holder.root.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLightAlpha));
                    }
                }
                super.onSelectedChanged(viewHolder, actionState);
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                if (viewHolder instanceof GameAdapter.GameViewHolder) {
                    GameAdapter.GameViewHolder holder = (GameAdapter.GameViewHolder) viewHolder;
                    holder.root.setBackgroundColor(Color.WHITE);
                }
            }
        };
    }

}
