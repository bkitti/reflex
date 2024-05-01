package com.example.reflex_test;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.example.reflex_test.game.Game;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class GameActivity extends Activity {

    private static final String LOG_TAG = GameActivity.class.getName();
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new Game(this));

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.d(LOG_TAG, "User is logged in");
        } else {
            Log.d(LOG_TAG, "Unauthenticated user");
            finish();
        }
    }
}