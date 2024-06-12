package lab.prog.infinitecraftle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder> {

    private List<Score> scoreList;

    public ScoreAdapter(List<Score> scoreList) {
        this.scoreList = scoreList;
    }

    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_score, parent, false);
        return new ScoreViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreViewHolder holder, int position) {
        Score score = scoreList.get(position);
        holder.tvScoreDate.setText(score.getDate());
        holder.tvScoreValue.setText(String.valueOf(score.getScore()));
    }

    @Override
    public int getItemCount() {
        return scoreList.size();
    }

    public static class ScoreViewHolder extends RecyclerView.ViewHolder {
        public TextView tvScoreDate, tvScoreValue;

        public ScoreViewHolder(View view) {
            super(view);
            tvScoreDate = view.findViewById(R.id.tvScoreDate);
            tvScoreValue = view.findViewById(R.id.tvScoreValue);
        }
    }
}
