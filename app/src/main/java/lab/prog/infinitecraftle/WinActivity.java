package lab.prog.infinitecraftle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import lab.prog.infinitecraftle.MyRankingAdapter;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lab.prog.infinitecraftle.domain.RankingRow;
import nl.dionsegijn.konfetti.core.Party;
import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.core.models.Shape;
import nl.dionsegijn.konfetti.core.models.Size;
import nl.dionsegijn.konfetti.xml.KonfettiView;

public class WinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);

        TextView tvScore = findViewById(R.id.tvScore);
        TextView tvTime = findViewById(R.id.tvTime);
        Button buttonContinuePlaying = findViewById(R.id.buttonContinuePlaying);
        // Get the score and time from the intent
        Intent intent = getIntent();
        int score = intent.getIntExtra("SCORE", 0);
        String time = intent.getStringExtra("TIME");
        ArrayList<RankingRow> ranking = (ArrayList<RankingRow>) intent.getSerializableExtra("RANKING");
        // Set the score and time
        tvScore.setText("Pontuação: " + score);
        tvTime.setText("Tempo: " + time);

        // Set up the continue playing button
        buttonContinuePlaying.setOnClickListener(v -> {
            Intent newIntent = new Intent(WinActivity.this, LoginActivity.class);
            startActivity(newIntent);
            finish();
        });

        int red = ContextCompat.getColor(this, R.color.red);
        int blue = ContextCompat.getColor(this, R.color.blue);
        int yellow = ContextCompat.getColor(this, R.color.yellow);

        KonfettiView konfettiView = findViewById(R.id.konfettiView);
        EmitterConfig emitterConfig = new Emitter(5L, TimeUnit.SECONDS).perSecond(50);
        Party party =
                new PartyFactory(emitterConfig)
                        .angle(270)
                        .spread(90)
                        .setSpeedBetween(1f, 5f)
                        .timeToLive(2000L)
                        .shapes(Arrays.asList(Shape.Square.INSTANCE, Shape.Circle.INSTANCE))
                        .sizes(new Size(12, 5f, 0.2f))
                        .position(0.0, 0.0, 1.0, 0.0)
                        .colors(Arrays.asList(red, blue, yellow))

                        .build();
        konfettiView.start(party);

        if (ranking != null) {
            RecyclerView recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            MyRankingAdapter adapter = new MyRankingAdapter(ranking);
            recyclerView.setAdapter(adapter);
        }
    }
}
