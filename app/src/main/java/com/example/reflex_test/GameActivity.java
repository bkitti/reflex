package com.example.reflex_test;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.reflex_test.game.Game;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class GameActivity extends Activity {

    private static final String LOG_TAG = GameActivity.class.getName();
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.d(LOG_TAG, "User is logged in");
        } else {
            Log.d(LOG_TAG, "Unauthenticated user");
            finish();
            return;
        }

        setContentView(R.layout.activity_game);
        renderScoreboard(this);
    }

    public void startGame (DocumentReference docRef, DocumentSnapshot document ) {
        Object highScore = document.get("highScore");
        int actualHighScore = 0;
        if (highScore != null) {
            actualHighScore = ((Long) highScore).intValue();
        }
        Game game = new Game(this);
        game.setActualHighScore(actualHighScore);
        int finalActualHighScore = actualHighScore;
        game.addOnGameEventListener(new Game.OnGameEventListener() {
            @Override
            public void onGameEnd(int score) {
                Map<String, Object> scoreMap = new HashMap<>();
                scoreMap.put("lastScore", score);
                scoreMap.put("highScore", Math.max(score, finalActualHighScore));
                docRef.set(scoreMap);
                setContentView(R.layout.activity_game);
                renderScoreboard(GameActivity.this);
            }
        });
        setContentView(game);
    }

    public void renderScoreboard(Context gameActivityContext) {
        LinearLayout scoreboard = findViewById(R.id.scoreLayout);
        // scoreboard.removeAllViews();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query = db.collection("user_scores").orderBy("highScore", Query.Direction.DESCENDING).limit(10);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    String userName = document.getId().substring(0, 6).concat("...");
                    Object highScore = document.get("highScore");
                    int actualHighScore = 0;
                    if (highScore != null) {
                        actualHighScore = ((Long) highScore).intValue();
                    }
                    Log.d(LOG_TAG, "User: " + userName + " High Score: " + actualHighScore);
                    TextView textView = new TextView(gameActivityContext);
                    textView.setText(userName + " High Score: " + actualHighScore);
                    textView.setTextSize(20);

                    if (user.getUid().equals(document.getId())) {
                        textView.setTextColor(0xFF000000);
                        textView.setBackgroundColor(0xFFFFFF00);
                    }

                    scoreboard.addView(textView);
                }
            } else {
                Log.d(LOG_TAG, "Failed to get user scores", task.getException());
            }
        });
    }

    public void onClick(View view) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("user_scores").document(user.getUid());
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                startGame(docRef, document);
            } else {
                Log.d(LOG_TAG, "Failed to get user score", task.getException());
            }
        });
    }
}