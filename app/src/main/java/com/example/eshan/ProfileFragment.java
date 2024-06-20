package com.example.eshan;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import android.widget.Toast;

public class ProfileFragment extends Fragment {

    private TextView userNameText, userEmailText, recipeCountText;
    private Button userProfileLoginButton, userProfileSignoutButton, notifiButton;
    private FirebaseFirestore db;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Find button by ID from the inflated view
        Button button5 = view.findViewById(R.id.button5);

        // Set an OnClickListener on the button5
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the add_recipe activity
                Intent intent = new Intent(getActivity(), add_recipe.class);
                startActivity(intent);
            }
        });

        // Find views by ID
        userNameText = view.findViewById(R.id.userNametext);
        userEmailText = view.findViewById(R.id.userEmlailText);
        userProfileLoginButton = view.findViewById(R.id.userProfileLoginButton);
        userProfileSignoutButton = view.findViewById(R.id.userProfileSignoutButton);
        notifiButton = view.findViewById(R.id.notifiButton);
        recipeCountText = view.findViewById(R.id.recipeCountText);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Get the current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            String name = user.getDisplayName();
            String email = user.getEmail();

            // Set the user name and email in the TextViews
            userNameText.setText(name != null ? name : "No Name");
            userEmailText.setText(email);

            // Fetch and display recipe count
            fetchRecipeCount(user.getUid());
        } else {
            // No user is signed in
            userNameText.setText("No user signed in");
            userEmailText.setText("No email available");
            recipeCountText.setText("0");
        }

        // Set OnClickListener for login button
        userProfileLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the login activity
                Intent intent = new Intent(getActivity(), login.class);
                startActivity(intent);
            }
        });

        // Set OnClickListener for signout button
        userProfileSignoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sign out the user
                FirebaseAuth.getInstance().signOut();
                // Update UI after sign out
                userNameText.setText("No user signed in");
                userEmailText.setText("No email available");
                recipeCountText.setText("0");
                // Optionally, navigate to the login activity
                Intent intent = new Intent(getActivity(), login.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        // Set OnClickListener for notification button
        notifiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseMessaging.getInstance().subscribeToTopic("new_recipes")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                String msg = "Subscribed to notifications!";
                                if (!task.isSuccessful()) {
                                    msg = "Subscription failed.";
                                }
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        return view;
    }

    private void fetchRecipeCount(String userId) {
        db.collection("users").document(userId).collection("recipes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count = 0;
                            for (DocumentSnapshot document : task.getResult()) {
                                count++;
                            }
                            recipeCountText.setText(String.valueOf(count));
                        } else {
                            Toast.makeText(getActivity(), "Failed to fetch recipe count.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
