package com.kfoszcz.makaoscore.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Krzysztof on 2018-03-06.
 */

public class ScoreRowView extends LinearLayout {

    private int headerCell;
    private int separator;
    private int scoreCell;
    private int scoreCount;

    public ScoreRowView(Context context) {
        super(context);
    }

    public ScoreRowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ScoreRowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ScoreRowView(Context context, int scoreCount, int headerCell, int scoreCell, int separator) {
        super(context);
        this.headerCell = headerCell;
        this.scoreCell = scoreCell;
        this.separator = separator;
        this.scoreCount = scoreCount;
    }

    public void inflateChildren(LayoutInflater inflater) {

        inflater.inflate(headerCell, this, true);
        for (int i = 0; i < scoreCount; i++) {
            inflater.inflate(separator, this, true);
            inflater.inflate(scoreCell, this, true);
        }

    }

    public View getHeaderCellView() {
        return this.getChildAt(0);
    }

    public View getScoreCellView(int index) {
        if (index > scoreCount)
            return null;

        return this.getChildAt(2 * index);
    }
}
