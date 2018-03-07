package com.kfoszcz.makaoscore.view;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kfoszcz.makaoscore.R;
import com.kfoszcz.makaoscore.data.MakaoDatabase;
import com.kfoszcz.makaoscore.data.Player;
import com.kfoszcz.makaoscore.logic.ScoreListController;

public class ScoreListActivity extends AppCompatActivity implements ScoreViewInterface {

    private LayoutInflater layoutInflater;
    private RelativeLayout rootView;
    private RecyclerView recyclerView;
    private View verticalSeparator;
    private ScoreListController controller;
    private int gameId;

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

    }

    @Override
    protected void onResume() {
        super.onResume();
        gameId = getIntent().getIntExtra("gameId", 0);
        if (gameId > 0)
            controller.getScoreList(gameId);
    }

    @Override
    public void setUpScoreList(Player[] players) {

        ScoreRowView header = new ScoreRowView(
                this, players.length,
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

    public int dipToPixels(int dip) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dip,
                getResources().getDisplayMetrics()
        );
    }
}
