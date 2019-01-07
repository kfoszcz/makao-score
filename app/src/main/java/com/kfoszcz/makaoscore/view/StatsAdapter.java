package com.kfoszcz.makaoscore.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kfoszcz.makaoscore.R;
import com.kfoszcz.makaoscore.data.PlayerWithStats;

import java.util.List;
import java.util.Locale;

public class StatsAdapter extends RecyclerView.Adapter<StatsAdapter.StatsViewHolder> {

    private final LayoutInflater inflater;
    private final Locale locale;
    private List<PlayerWithStats> stats;

    StatsAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        locale = context.getResources().getConfiguration().locale;
    }

    @Override
    public StatsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_stats, parent, false);
        return new StatsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StatsViewHolder holder, int position) {
        if (stats != null) {
            PlayerWithStats current = stats.get(position);
            holder.initial.setText(current.getPlayer().getInitial());
            holder.wins.setText(FormatHelper.integer(locale, current.getWins()));
            holder.successRate.setText(FormatHelper.percent(locale, current.getSuccessRate()));
            holder.halfRate.setText(FormatHelper.percent(locale, current.getHalfRate()));
            holder.failRate.setText(FormatHelper.percent(locale, current.getFailRate()));
        }
    }

    @Override
    public int getItemCount() {
        return (stats != null) ? stats.size() : 0;
    }

    public void setStats(List<PlayerWithStats> stats) {
        this.stats = stats;
        notifyDataSetChanged();
    }

    class StatsViewHolder extends RecyclerView.ViewHolder {
        private final TextView initial;
        private final TextView wins;
        private final TextView successRate;
        private final TextView halfRate;
        private final TextView failRate;

        private StatsViewHolder(View itemView) {
            super(itemView);
            initial = itemView.findViewById(R.id.stats_item_initial);
            wins = itemView.findViewById(R.id.stats_item_wins);
            successRate = itemView.findViewById(R.id.stats_item_ok);
            halfRate = itemView.findViewById(R.id.stats_item_half);
            failRate = itemView.findViewById(R.id.stats_item_fail);
        }
    }

}
