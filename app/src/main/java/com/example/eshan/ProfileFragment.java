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

public class ProfileFragment extends Fragment {

    private TextView userNameText, userEmailText;
    private Button userProfileLoginButton, userProfileSignoutButton;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Find views by ID
        userNameText = view.findViewById(R.id.userNametext);
        userEmailText = view.findViewById(R.id.userEmlailText);
        userProfileLoginButton = view.findViewById(R.id.userProfileLoginButton);
        userProfileSignoutButton = view.findViewById(R.id.userProfileSignoutButton);

        // Get the current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            String name = user.getDisplayName();
            String email = user.getEmail();

            // Set the user name and email in the TextViews
            userNameText.setText(name != null ? name : "No Name");
            userEmailText.setText(email);
        } else {
            // No user is signed in
            userNameText.setText("No user signed in");
            userEmailText.setText("No email available");
        }

        // Set an OnClickListener on the login button
        userProfileLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the login activity
                Intent intent = new Intent(getActivity(), login.class);
                startActivity(intent);
            }
        });

        // Set an OnClickListener on the signout button
        userProfileSignoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sign out the user
                FirebaseAuth.getInstance().signOut();
                // Update UI after sign out
                userNameText.setText("No user signed in");
                userEmailText.setText("No email available");
                // Optionally, navigate to the login activity
                Intent intent = new Intent(getActivity(), login.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }
}
