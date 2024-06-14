package lab.prog.infinitecraftle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


/**
 * Adapter class for the RecyclerView that displays the scores.
 */
public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder> {

    private List<Score> scoreList;

    /**
     * Constructor for the ScoreAdapter.
     * @param scoreList The list of scores to display.
     */
    public ScoreAdapter(List<Score> scoreList) {
        this.scoreList = scoreList;
    }

    /**
     * Method to create a new ViewHolder.
     * @param parent The parent view.
     * @param viewType The view type.
     * @return The new ViewHolder.
     */
    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_score, parent, false);
        return new ScoreViewHolder(itemView);
    }

    /**
     * Method to bind the ViewHolder to the data.
     * @param holder The ViewHolder.
     * @param position The position in the list.
     */
    @Override
    public void onBindViewHolder(@NonNull ScoreViewHolder holder, int position) {
        Score score = scoreList.get(position);
        holder.tvScoreDate.setText(score.getDate());
        holder.tvScoreValue.setText(String.valueOf(score.getScore()));
    }

    /**
     * Method to get the number of items in the list.
     * @return The number of items in the list.
     */
    @Override
    public int getItemCount() {
        return scoreList.size();
    }

    /**
     * ViewHolder class for the scores.
     */
    public static class ScoreViewHolder extends RecyclerView.ViewHolder {
        public TextView tvScoreDate, tvScoreValue;

        public ScoreViewHolder(View view) {
            super(view);
            tvScoreDate = view.findViewById(R.id.tvScoreDate);
            tvScoreValue = view.findViewById(R.id.tvScoreValue);
        }
    }
}
