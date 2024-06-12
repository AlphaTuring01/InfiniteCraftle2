package lab.prog.infinitecraftle;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

import java.util.ArrayList;
import java.util.List;

public class WinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);

        TextView tvVictoryMessage = findViewById(R.id.tvVictoryMessage);
        TextView tvScore = findViewById(R.id.tvScore);
        TextView tvTime = findViewById(R.id.tvTime);
        Button buttonContinuePlaying = findViewById(R.id.buttonContinuePlaying);
        KonfettiView konfettiView = findViewById(R.id.konfettiView);
        RecyclerView rvScores = findViewById(R.id.rvScores);

        // Get the score and time from the intent
        Intent intent = getIntent();
        int score = intent.getIntExtra("SCORE", 0);
        String time = intent.getStringExtra("TIME");

        // Set the score and time
        tvScore.setText("Pontuação: " + score);
        tvTime.setText("Tempo: " + time);

        // Set up the continue playing button
        buttonContinuePlaying.setOnClickListener(v -> {
            Intent returnIntent = new Intent();
            setResult(RESULT_OK, returnIntent);
            finish();
        });

        // Start the confetti animation
        konfettiView.build()
                .addColors(ContextCompat.getColor(this, R.color.red), ContextCompat.getColor(this, R.color.blue), ContextCompat.getColor(this, R.color.yellow))
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(2000L)
                .addShapes(Shape.Square.INSTANCE, Shape.Circle.INSTANCE)
                .addSizes(new Size(12, 5f))
                .setPosition(-50f, konfettiView.getWidth() + 50f, -50f, -50f)
                .streamFor(300, 5000L);

        // Prepare the data for the scores
        List<Score> scoreList = new ArrayList<>();
        // Add sample scores, replace with real data as needed
        scoreList.add(new Score("2024-06-10", 120));
        scoreList.add(new Score("2024-06-11", 140));
        scoreList.add(new Score("2024-06-12", 160));

        // Set up the RecyclerView
        rvScores.setLayoutManager(new LinearLayoutManager(this));
        ScoreAdapter scoreAdapter = new ScoreAdapter(scoreList);
        rvScores.setAdapter(scoreAdapter);
    }
}
