package com.example.eshan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class splashscreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if the splash screen has been shown before
        SharedPreferences preferences = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstTime = preferences.getBoolean("firstTime", true);

        if (!firstTime) {
            // If not the first time, go directly to MainActivity
            Intent intent = new Intent(splashscreen.this, signup.class);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_splashscreen);

        // Find the button by its ID
        Button splashButton = findViewById(R.id.splashButtongo);

        // Set an OnClickListener on the button
        splashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save that the splash screen has been shown
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("firstTime", false);
                editor.apply();

                // Create an Intent to start the MainActivity
                Intent intent = new Intent(splashscreen.this, MainActivity.class);
                startActivity(intent);
                // Finish the splash screen so the user cannot go back to it
                finish();
            }
        });
    }
}
