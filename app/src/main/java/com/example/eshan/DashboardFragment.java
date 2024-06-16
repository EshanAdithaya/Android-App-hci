package com.example.eshan;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class DashboardFragment extends Fragment {
    private FirebaseFirestore db;
    private ListView listView;
    private recipeAdapter adapter;
    private List<recipes> recipeList;

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize ListView and recipe list
        listView = view.findViewById(R.id.recipe_list_view);
        recipeList = new ArrayList<>();

        // Initialize adapter and set to ListView
        adapter = new recipeAdapter(getContext(), recipeList);
        listView.setAdapter(adapter);

        // Fetch recipes from Firestore
        fetchRecipes();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             //   String selectedItem = (String) parent.getItemAtPosition(position);
                Intent intent = new Intent(getContext(), recipeShowcase.class);
                intent.putExtra("selectedItem", "selectedItem");
                startActivity(intent);
            }
        });

        return view;


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
    private void fetchRecipes() {
        db.collection("users").document(getCurrentUserId()).collection("recipes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
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
