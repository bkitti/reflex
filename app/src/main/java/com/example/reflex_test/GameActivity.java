package com.example.reflex_test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.reflex_test.game.Game;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("user_scores").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    startGame(docRef, document);
                } else {
                    Log.d(LOG_TAG, "Failed to get user score", task.getException());
                }
            }
        });
    }

    public void startGame (DocumentReference docRef, DocumentSnapshot document ) {
        Context gameActivityContext = this;
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
                Intent intent = new Intent(gameActivityContext, MainActivity.class);
                gameActivityContext.startActivity(intent);

                Map<String, Object> scoreMap = new HashMap<>();
                scoreMap.put("lastScore", score);
                scoreMap.put("highScore", Math.max(score, finalActualHighScore));
                docRef.set(scoreMap);
            }
        });
        setContentView(game);
    }
}