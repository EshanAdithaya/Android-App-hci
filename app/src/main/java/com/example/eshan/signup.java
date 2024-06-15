package com.example.eshan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class signup extends AppCompatActivity {

    private static final String TAG = "signup";
    private FirebaseAuth mAuth;
    private EditText emailEditText, passwordEditText, nameEditText;
    private Button signupButton, loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_signup);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Find views by ID
        emailEditText = findViewById(R.id.UserTextEmailAddress);
        passwordEditText = findViewById(R.id.UserTextTextPassword);
        nameEditText = findViewById(R.id.UserNameText);
        signupButton = findViewById(R.id.signupfinal);
        loginButton = findViewById(R.id.login);

        // Set an OnClickListener on the signup button
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String name = nameEditText.getText().toString().trim();

                // Validate input fields
                if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
                    Toast.makeText(signup.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create a new account
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(signup.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                } else {
                                    try {
                                        throw task.getException();
                                    } catch (FirebaseAuthUserCollisionException e) {
                                        // Email already exists
                                        Toast.makeText(signup.this, "This email address is already in use.", Toast.LENGTH_SHORT).show();
                                    } catch (FirebaseAuthInvalidCredentialsException e) {
                                        // Invalid email format
                                        Toast.makeText(signup.this, "The email address is badly formatted.", Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {
                                        // Other exceptions
                                        Log.e("SignupActivity", e.getMessage());
                                        Toast.makeText(signup.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    }
                                    Log.w(TAG, email+"--"+password);
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(signup.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }
                            }
                        });
            }
        });

        // Set an OnClickListener on the login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the login activity
                Intent intent = new Intent(signup.this, login.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            reload();
        }
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Toast.makeText(signup.this, "Registration successful!", Toast.LENGTH_SHORT).show();
            // You can redirect the user to another activity here if needed
            Intent intent = new Intent(signup.this, login.class); // Change to your target activity
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(signup.this, "Registration failed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void reload() {
        // Implement any specific logic for updating the UI
    }
}
