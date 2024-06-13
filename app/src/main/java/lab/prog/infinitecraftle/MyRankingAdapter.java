package lab.prog.infinitecraftle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import lab.prog.infinitecraftle.domain.RankingRow;
public class MyRankingAdapter extends RecyclerView.Adapter<MyRankingAdapter.RankingViewHolder> {
    private ArrayList<RankingRow> rankingList;

    public MyRankingAdapter(ArrayList<RankingRow> rankingList) {
        this.rankingList = rankingList;
    }

    @NonNull
    @Override
    public RankingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ranking_row_item, parent, false);
        return new RankingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RankingViewHolder holder, int position) {
        RankingRow rankingRow = rankingList.get(position);
        holder.usernameTextView.setText(rankingRow.getUsername());
        holder.scoreTextView.setText(String.valueOf(rankingRow.getScore()));
    }

    @Override
    public int getItemCount() {
        return rankingList.size();
    }

    public static class RankingViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        TextView scoreTextView;

        public RankingViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            scoreTextView = itemView.findViewById(R.id.scoreTextView);
        }
    }
}
