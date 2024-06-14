package lab.prog.infinitecraftle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import lab.prog.infinitecraftle.domain.RankingRow;


/**
 * Adapter class for the RecyclerView that displays the ranking.
 */
public class MyRankingAdapter extends RecyclerView.Adapter<MyRankingAdapter.RankingViewHolder> {
    private ArrayList<RankingRow> rankingList;

    /**
     * Constructor for the MyRankingAdapter.
     * @param rankingList The list of ranking rows to display.
     */
    public MyRankingAdapter(ArrayList<RankingRow> rankingList) {
        this.rankingList = rankingList;
    }

    /**
     * Method to create a new ViewHolder.
     * @param parent The parent view.
     * @param viewType The view type.
     * @return The new ViewHolder.
     */
    @NonNull
    @Override
    public RankingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ranking_row_item, parent, false);
        return new RankingViewHolder(view);
    }

    /**
     * Method to bind the ViewHolder to the data.
     * @param holder The ViewHolder.
     * @param position The position in the list.
     */
    @Override
    public void onBindViewHolder(@NonNull RankingViewHolder holder, int position) {
        RankingRow rankingRow = rankingList.get(position);
        holder.usernameTextView.setText(rankingRow.getUsername());
        holder.scoreTextView.setText(String.valueOf(rankingRow.getScore()));
    }

    /**
     * Method to get the number of items in the list.
     * @return The number of items in the list.
     */
    @Override
    public int getItemCount() {
        return rankingList.size();
    }

    /**
     * ViewHolder class for the ranking.
     */
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
