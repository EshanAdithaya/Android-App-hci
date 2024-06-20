package com.example.eshan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class recipeShowcase extends AppCompatActivity {

    private static final String TAG = "recipeShowcase";
    private Context context;
    private ImageView foodImageView;
    private EditText recipy_name, recipy_ingredients, recipy_instructions;
    private FirebaseFirestore db;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_showcase);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize UI elements
        recipy_ingredients = findViewById(R.id.IngredientsText);
        recipy_instructions = findViewById(R.id.recipeStepsText);
        recipy_name = findViewById(R.id.Recipename);
        foodImageView = findViewById(R.id.imageViewRecipe);
        progressBar = findViewById(R.id.progressBar);

        // Retrieve data from the intent
        Intent intent = getIntent();
        recipy_name.setText(intent.getStringExtra("recipyName"));
        recipy_ingredients.setText(intent.getStringExtra("recipyIngredients"));
        recipy_instructions.setText(intent.getStringExtra("recipySteps"));
        String imageUrl = intent.getStringExtra("imageUrl");

        // Load image using Glide
        Glide.with(this).load(imageUrl).into(foodImageView);

        // Optional: Fetch additional data if needed
        // fetchRecipyData();
    }

    private String getCurrentUserId() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getUid();
        } else {
            // Handle case where user is not authenticated
            return null;  // Or handle appropriately based on your app's requirements
        }
    }

    private void fetchRecipyData() {
        String userId = getCurrentUserId();
        if (userId == null) {
            Log.e(TAG, "User is not authenticated");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        db.collection("users").document(userId).collection("recipes").document("YvR7kndrqM96AVy8QeVR").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                progressBar.setVisibility(View.GONE);

                if (documentSnapshot.exists()) {
                    String name = documentSnapshot.getString("name");
                    String ingredients = documentSnapshot.getString("ingredients");
                    String steps = documentSnapshot.getString("steps");
                    ArrayList<String> imageUrls = (ArrayList<String>) documentSnapshot.get("imageUrls");

                    recipy_name.setText(name);
                    recipy_ingredients.setText(ingredients);
                    recipy_instructions.setText(steps);
                    if (imageUrls != null && !imageUrls.isEmpty()) {
                        Glide.with(context).load(imageUrls.get(0)).into(foodImageView);
                    }
                } else {
                    Log.e(TAG, "No such document");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "Error fetching data: " + e.getMessage());
            }
        });
    }
}
