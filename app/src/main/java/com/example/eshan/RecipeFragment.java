package com.example.eshan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class RecipeFragment extends Fragment {
    private FirebaseFirestore db;
    private GridView gridView;
    private recipeAdapter adapter;
    private List<recipes> recipeList;
    private FirebaseAuth mAuth;

    public RecipeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipes, container, false);

        // Initialize Firestore and FirebaseAuth
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Initialize GridView and recipe list
        gridView = view.findViewById(R.id.recipe_grid_view);
        recipeList = new ArrayList<>();

        // Initialize adapter and set to GridView
        adapter = new recipeAdapter(getContext(), recipeList);
        gridView.setAdapter(adapter);

        // Fetch recipes if user is logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            fetchRecipes();
        } else {
            // Show alert dialog if user is not logged in
            new AlertDialog.Builder(getContext())
                    .setTitle("Please Log In")
                    .setMessage("You need to log in first to view recipes.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                recipes selectedItem = adapter.getItem(position);
                if (selectedItem != null) {
                    Intent intent = new Intent(getContext(), recipeShowcase.class);
                    intent.putExtra("recipyName", selectedItem.getName());
                    intent.putExtra("recipyIngredients", selectedItem.getIngredients());
                    intent.putExtra("recipySteps", selectedItem.getSteps());
                    if (selectedItem.getImageUrls() != null && !selectedItem.getImageUrls().isEmpty()) {
                        intent.putExtra("imageUrl", selectedItem.getImageUrls().get(0));
                    }
                    startActivity(intent);
                }
            }
        });

        return view;
    }

    private String getCurrentUserId() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            return user.getUid();
        } else {
            return null;  // Or handle appropriately based on your app's requirements
        }
    }

    private void fetchRecipes() {
        db.collection("recipe") // Assuming "recipe" is the collection name in Firestore
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            recipeList.clear(); // Clear the list before adding new recipes
                            for (DocumentSnapshot document : task.getResult()) {
                                recipes recipe = document.toObject(recipes.class);
                                recipeList.add(recipe);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            // Handle the error here
                        }
                    }
                });
    }
}
