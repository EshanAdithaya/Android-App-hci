package com.example.eshan;

import static androidx.fragment.app.FragmentManager.TAG;
import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class recipeShowcase extends AppCompatActivity {
    private Context context;

    private ImageView foodImageView;

    private EditText recipy_name, recipy_ingredients, recipy_instructions;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_showcase);

        recipy_ingredients = findViewById(R.id.IngredientsText);

        recipy_instructions = findViewById(R.id.recipeStepsText);

        recipy_name = findViewById(R.id.Recipename);



        foodImageView = findViewById(R.id.imageViewRecipe);
        Intent intent = getIntent();
        recipy_name.setText(intent.getStringExtra("recipyName"));
        recipy_ingredients.setText(intent.getStringExtra("recipyIngredients"));
        recipy_instructions.setText(intent.getStringExtra("recipySteps"));
      //  recipy_instructions.setText(intent.getStringExtra("imageUrl"));
     //   fetchRecipyData() ;
//        if (isAdded() && getContext() != null) {
//            Glide.with(getContext()).load(imageUrl).into(imageView);
//        }
        Glide.with(this).load(intent.getStringExtra("imageUrl")).into(foodImageView);
    }

    private String getCurrentUserId() {
        // Implement this method to get the current user ID from your authentication system
        // Example using Firebase Authentication:
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getUid();
        } else {
            // Handle case where user is not authenticated
            return null;  // Or handle appropriately based on your app's requirements
        }
    }
    private void fetchRecipyData() {
//        db.collection("users").document(getCurrentUserId()).collection("recipes").document("YvR7kndrqM96AVy8QeVR").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                //for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
//                    String name = documentSnapshot.getString("name");
//                    String ingredients = documentSnapshot.getString("ingredients");
//                    String steps = documentSnapshot.getString("steps");
//                    ArrayList<String> imageUrls = (ArrayList<String>) documentSnapshot.get("imageUrls");
//
//                    // Assuming you have TextViews named textView1, textView2 and an ImageView named imageView in your layout
//                    recipy_name.setText(name);
//                    recipy_instructions.setText(ingredients);
//                    recipy_instructions.setText(steps);
//                   // Glide.with(context).load(imageUrls.get(0)).into(foodImageView);
//              //  }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.e("TAG", "Error fetching data: " + e.getMessage());
//            }
//        });
    }
}