package com.kfoszcz.makaoscore.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.Toast;

import com.kfoszcz.makaoscore.R;
import com.kfoszcz.makaoscore.data.MakaoDatabase;
import com.kfoszcz.makaoscore.data.Player;
import com.kfoszcz.makaoscore.data.Score;
import com.kfoszcz.makaoscore.data.ScoreRow;
import com.kfoszcz.makaoscore.logic.ScoreListController;

import org.w3c.dom.Text;

import java.util.List;

public class ScoreListActivity extends AppCompatActivity implements ScoreViewInterface {

    private LayoutInflater layoutInflater;
    private RelativeLayout rootView;
    private RecyclerView recyclerView;
    private View verticalSeparator;
    private ScoreListController controller;
    private ScoreRowAdapter adapter;

    private int gameId;
    private Player[] players;
    private List<ScoreRow> scoreList;

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

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (gameId > 0)
            controller.getScoreList(gameId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_score_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_score_list_add:
                controller.menuAddPressed();
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
            ((TextView) header.getScoreCellView(i + 1)).setText(players[i].getInitial());
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
                        ((TextView) cell.getChildAt(1))
                                .setText(Integer.toString(currentRow.getScores()[i].getTotalPoints()));
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
}
