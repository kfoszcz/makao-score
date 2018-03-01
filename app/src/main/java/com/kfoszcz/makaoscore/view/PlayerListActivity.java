package com.kfoszcz.makaoscore.view;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.kfoszcz.makaoscore.R;
import com.kfoszcz.makaoscore.data.Player;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Player> playerList;
    private LayoutInflater layoutInflater;
    private PlayerAdapter adapter;
    private ItemTouchHelper touchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_list);

        recyclerView = findViewById(R.id.rec_player_list);
        layoutInflater = getLayoutInflater();

        List<Player> testPlayers = new ArrayList<>();
        testPlayers.add(new Player("K", "Krzycho"));
        testPlayers.add(new Player("M", "Mama"));
        testPlayers.add(new Player("T", "Tata"));

        setUpRecyclerView(testPlayers);
    }

    public void setUpRecyclerView(List<Player> data) {
        playerList = data;

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new PlayerAdapter();
        recyclerView.setAdapter(adapter);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(
                recyclerView.getContext(),
                layoutManager.getOrientation()
        );

        itemDecoration.setDrawable(
                ContextCompat.getDrawable(
                        PlayerListActivity.this,
                        R.drawable.divider_horizontal
                )
        );

        recyclerView.addItemDecoration(itemDecoration);

        touchHelper = new ItemTouchHelper(createTouchHelperCallback());
        touchHelper.attachToRecyclerView(recyclerView);

    }

    private class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {

        @Override
        public PlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = layoutInflater.inflate(R.layout.item_player_list, parent, false);
            return new PlayerViewHolder(v);
        }

        @Override
        public void onBindViewHolder(PlayerViewHolder holder, int position) {
            Player currentPlayer = playerList.get(position);
            holder.initial.setText(currentPlayer.getInitial());
            holder.name.setText(currentPlayer.getName());
        }

        @Override
        public int getItemCount() {
            return playerList.size();
        }

        class PlayerViewHolder extends RecyclerView.ViewHolder {

            private CheckBox checkbox;
            private TextView initial;
            private TextView name;
            private ImageView drag;
            private View root;

            public PlayerViewHolder(View itemView) {
                super(itemView);

                root = itemView;
                checkbox = itemView.findViewById(R.id.chb_player_item);
                initial = itemView.findViewById(R.id.txt_player_item_initial);
                name = itemView.findViewById(R.id.txt_player_item_name);
                drag = itemView.findViewById(R.id.imv_player_item_drag);

                drag.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) {
                            touchHelper.startDrag(PlayerViewHolder.this);
                        }
                        return false;
                    }
                });
            }

        }

    }

    ItemTouchHelper.Callback createTouchHelperCallback() {
        return new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.DOWN | ItemTouchHelper.UP,
                0
        ) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();

                if (fromPosition == toPosition)
                    return false;

                Collections.swap(playerList, fromPosition, toPosition);
                adapter.notifyItemMoved(fromPosition, toPosition);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            }

            @Override
            public boolean isLongPressDragEnabled() {
                return false;
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                    if (viewHolder instanceof PlayerAdapter.PlayerViewHolder) {
                        PlayerAdapter.PlayerViewHolder holder = (PlayerAdapter.PlayerViewHolder) viewHolder;
                        holder.root.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLightAlpha));
                    }
                }
                super.onSelectedChanged(viewHolder, actionState);
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                if (viewHolder instanceof PlayerAdapter.PlayerViewHolder) {
                    PlayerAdapter.PlayerViewHolder holder = (PlayerAdapter.PlayerViewHolder) viewHolder;
                    holder.root.setBackgroundColor(Color.WHITE);
                }
            }
        };
    }
}
