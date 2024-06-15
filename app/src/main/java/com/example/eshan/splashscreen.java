package com.example.eshan;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class splashscreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        // Find the button by its ID
        Button splashButton = findViewById(R.id.splashButtongo);

        // Set an OnClickListener on the button
        splashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the MainActivity
                Intent intent = new Intent(splashscreen.this, MainActivity.class);
                startActivity(intent);
                // Finish the splash screen so the user cannot go back to it
                finish();
            }
        });
    }
}
