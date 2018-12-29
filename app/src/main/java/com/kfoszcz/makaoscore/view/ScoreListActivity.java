package com.kfoszcz.makaoscore.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kfoszcz.makaoscore.R;
import com.kfoszcz.makaoscore.data.MakaoDatabase;
import com.kfoszcz.makaoscore.data.Player;
import com.kfoszcz.makaoscore.data.Score;
import com.kfoszcz.makaoscore.data.ScoreRow;
import com.kfoszcz.makaoscore.logic.ScoreListController;

import java.util.List;

public class ScoreListActivity extends AppCompatActivity implements ScoreViewInterface {

    private LayoutInflater layoutInflater;
    private RelativeLayout rootView;
    private RecyclerView recyclerView;
    private View verticalSeparator;
    private ScoreListController controller;
    private ScoreRowAdapter adapter;
    private SharedPreferences prefs;

    private int gameId;
    private Player[] players;
    private List<ScoreRow> scoreList;
    private int relativeToPlayer;

    private boolean showScoreColors;
    private boolean swipeToDelete;

    private static final String SHOW_SCORE_COLORS = "SHOW_SCORE_COLORS";

    public static int[] backgroundColors = {
            0,
            R.color.bgFail,
            R.color.bgHalf,
            R.color.bgHalf,
            R.color.bgSuccess
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_list);

        layoutInflater = getLayoutInflater();
        rootView = findViewById(R.id.root_score_list);
        recyclerView = findViewById(R.id.rec_score_list);
        verticalSeparator = findViewById(R.id.sep_score_list);

        controller = new ScoreListController(
                this,
                MakaoDatabase.getDatabase(getApplicationContext()).dao()
        );

        gameId = getIntent().getIntExtra("gameId", 0);
        relativeToPlayer = -1;
        prefs = getPreferences(Context.MODE_PRIVATE);
        showScoreColors = prefs.getBoolean(SHOW_SCORE_COLORS, false);
        swipeToDelete = false;

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (gameId > 0)
            controller.getScoreList(gameId);

    }

    private void updateShowScoreItem(MenuItem item) {
        if (showScoreColors)
            item.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_invert_colors_red_48dp));
        else
            item.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_invert_colors_white_48dp));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_score_list, menu);
        updateShowScoreItem(menu.findItem(R.id.menu_score_list_colors));
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_score_list_delete:
                swipeToDelete = !swipeToDelete;
                if (swipeToDelete)
                    item.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_delete_red_48dp));
                else
                    item.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_delete_white_48dp));
                return true;

            case R.id.menu_score_list_colors:
                showScoreColors = !showScoreColors;
                prefs.edit().putBoolean(SHOW_SCORE_COLORS, showScoreColors).apply();
                updateShowScoreItem(item);
                adapter.notifyDataSetChanged();
                return true;

            case R.id.menu_score_list_add:
                controller.menuAddPressed(gameId);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void setUpScoreListHeader(Player[] players) {

        this.players = players;

        ScoreRowView header = new ScoreRowView(
                this,
                players.length,
                R.layout.header_first_score_list,
                R.layout.header_cell_score_list,
                R.layout.separator_score_list
        );

        header.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dipToPixels(48)
        ));
        header.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
        header.inflateChildren(layoutInflater);
        if (Build.VERSION.SDK_INT >= 17)
            header.setId(View.generateViewId());
        else
            header.setId(R.id.headerId);
        rootView.addView(header, 0);

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) verticalSeparator.getLayoutParams();
        lp.addRule(RelativeLayout.BELOW, header.getId());

        for (int i = 0; i < players.length; i++) {
            TextView playerText = (TextView) header.getScoreCellView(i + 1);
            playerText.setText(players[i].getInitial());
            final int j = i;
            playerText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    relativeToPlayer = (relativeToPlayer != j) ? j : -1;
                    adapter.notifyDataSetChanged();
                }
            });
        }

    }

    @Override
    public void setUpScoreList(List<ScoreRow> scores) {
        scoreList = scores;

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ScoreRowAdapter();
        recyclerView.setAdapter(adapter);

        if (recyclerView.getItemDecorationAt(0) == null) {
            DividerItemDecoration itemDecoration = new DividerItemDecoration(
                    recyclerView.getContext(),
                    layoutManager.getOrientation()
            );

            itemDecoration.setDrawable(
                    ContextCompat.getDrawable(
                            ScoreListActivity.this,
                            R.drawable.divider_score_list
                    )
            );
            recyclerView.addItemDecoration(itemDecoration);
        }

        ItemTouchHelper touchHelper = new ItemTouchHelper(createTouchHelperCallback());
        touchHelper.attachToRecyclerView(recyclerView);

        recyclerView.scrollToPosition(scoreList.size() - 1);
    }

    @Override
    public void startAddScoreActivity(int dealId) {
        Intent intent = new Intent(this, AddScoreActivity.class);
        intent.putExtra("gameId", gameId);
        intent.putExtra("dealId", dealId);
        startActivity(intent);
    }

    public int dipToPixels(int dip) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dip,
                getResources().getDisplayMetrics()
        );
    }

    private class ScoreRowAdapter extends RecyclerView.Adapter<ScoreRowAdapter.ScoreRowViewHolder> {

        @Override
        public ScoreRowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ScoreRowView v = new ScoreRowView(
                    ScoreListActivity.this,
                    players.length,
                    R.layout.row_first_score_list,
                    R.layout.row_cell_score_list,
                    R.layout.separator_score_list
            );

            v.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    dipToPixels(48)
            ));

            v.setGravity(Gravity.CENTER);
            v.inflateChildren(layoutInflater);
            return new ScoreRowViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ScoreRowViewHolder holder, int position) {
            ScoreRow currentRow = scoreList.get(position);
            ((TextView) holder.root.getHeaderCellView())
                    .setText(Integer.toString(currentRow.getDealId()));

            for (int i = 0; i < players.length; i++) {
                ViewGroup cell = (ViewGroup) holder.root.getScoreCellView(i + 1);
                if (currentRow.getScores()[i].getDeclared() != -1) {
                    ((TextView) cell.getChildAt(0))
                            .setText(Integer.toString(currentRow.getScores()[i].getDeclared()));

                    if (currentRow.getScores()[i].getScoreType() != Score.SCORE_NONE) {
                        TextView totalText = (TextView) cell.getChildAt(1);
                        totalText.setTextColor(getResources().getColor(R.color.textPrimary));

                        if (relativeToPlayer > -1) {
                            int diff = currentRow.getScores()[i].getTotalPoints()
                                     - currentRow.getScores()[relativeToPlayer].getTotalPoints();

                            if (relativeToPlayer == i) {
                                totalText.setText("*");
                            }

                            else if (diff > 0) {
                                totalText.setTextColor(getResources().getColor(R.color.btnFail));
                                totalText.setText(Integer.toString(diff));
                            }

                            else if (diff < 0) {
                                totalText.setTextColor(getResources().getColor(R.color.btnSuccess));
                                totalText.setText(Integer.toString(-diff));
                            }

                            else {
                                totalText.setText("0");
                                totalText.setTextColor(getResources().getColor(R.color.scoreDraw));
                            }

                        }
                        else
                            totalText.setText(Integer.toString(currentRow.getScores()[i].getTotalPoints()));

                        if (showScoreColors) {
                            cell.setBackgroundColor(
                                    getResources().getColor(backgroundColors[
                                            currentRow.getScores()[i].getScoreType()
                                    ])
                            );
                        }
                        else
                            cell.setBackgroundColor(Color.TRANSPARENT);
                    }
                }
            }
        }

        @Override
        public int getItemCount() {
            return scoreList.size();
        }

        class ScoreRowViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private ScoreRowView root;

            public ScoreRowViewHolder(View itemView) {
                super(itemView);
                root = (ScoreRowView) itemView;
                root.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                controller.scoreRowClicked(
                        scoreList.get(ScoreRowViewHolder.this.getAdapterPosition())
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
                controller.deleteScoreRow(scoreList.get(position));

                scoreList.remove(position);
                adapter.notifyItemRemoved(position);

                if (position < scoreList.size())
                    controller.getScoreList(gameId);
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return swipeToDelete ? ItemTouchHelper.LEFT : 0;
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                    if (viewHolder instanceof ScoreRowAdapter.ScoreRowViewHolder) {
                        ScoreRowAdapter.ScoreRowViewHolder holder = (ScoreRowAdapter.ScoreRowViewHolder) viewHolder;
                        holder.root.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLightAlpha));
                    }
                }
                super.onSelectedChanged(viewHolder, actionState);
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                if (viewHolder instanceof ScoreRowAdapter.ScoreRowViewHolder) {
                    ScoreRowAdapter.ScoreRowViewHolder holder = (ScoreRowAdapter.ScoreRowViewHolder) viewHolder;
                    holder.root.setBackgroundColor(Color.WHITE);
                }
            }
        };
    }
}
