package com.example.eshan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import com.example.eshan.R;



import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class add_recipe extends AppCompatActivity {

    private static final int REQUEST_CODE_PICK_IMAGES = 1;
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 2;
    private static final String TAG = "add_recipe";
    private static final String COUNTER_DOC = "counter";
    private static final String COUNTER_FIELD = "latestRecipeId";

    private ArrayList<Uri> imageUris = new ArrayList<>();
    private int currentImageIndex = 0;

    private ImageView foodImageView;
    private Button addPhotoButton, previousImageButton, nextImageButton, deletePhotoButton, addRecipeButton;
    private RecyclerView imageNameViewer;
    private ImageNameAdapter imageNameAdapter;

    private FirebaseFirestore db;
    private FirebaseStorage storage;

    private ProgressBar progressBar;

    private Spinner categorySpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
// ***********************************************************************************************************************//
         //Initialize Spinner
        categorySpinner = findViewById(R.id.spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_items, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        categorySpinner.setAdapter(adapter);
        progressBar = findViewById(R.id.progressBar);
//****************************************************************************************************************************//

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        foodImageView = findViewById(R.id.food_image);
        addPhotoButton = findViewById(R.id.add_photo_button);
        previousImageButton = findViewById(R.id.previousImagebutton);
        nextImageButton = findViewById(R.id.nextImagebutton);
        deletePhotoButton = findViewById(R.id.delete_photo_button2);
        addRecipeButton = findViewById(R.id.addRecipyTofirebaseButton);
        imageNameViewer = findViewById(R.id.imagenameviewer);

        // Initialize the Spinner
        categorySpinner = findViewById(R.id.spinner);

        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(add_recipe.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(add_recipe.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_READ_EXTERNAL_STORAGE);
                } else {
                    pickImages();
                }
            }
        });



        previousImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPreviousImage();
            }
        });

        nextImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNextImage();
            }
        });

        deletePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCurrentImage();
            }
        });

        addRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRecipeToFirebase();
            }
        });

        setupRecyclerView();
        updateImageView();
    }

    private void pickImages() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Select Pictures"), REQUEST_CODE_PICK_IMAGES);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGES && resultCode == RESULT_OK) {
            if (data != null) {
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count && imageUris.size() < 5; i++) {
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        imageUris.add(imageUri);
                    }
                } else if (data.getData() != null) {
                    if (imageUris.size() < 5) {
                        Uri imageUri = data.getData();
                        imageUris.add(imageUri);
                    }
                }
                currentImageIndex = 0;
                updateImageView();
                updateRecyclerView();
            }
        }
    }

    private void showPreviousImage() {
        if (imageUris.size() > 0) {
            currentImageIndex = (currentImageIndex - 1 + imageUris.size()) % imageUris.size();
            updateImageView();
        }
    }

    private void showNextImage() {
        if (imageUris.size() > 0) {
            currentImageIndex = (currentImageIndex + 1) % imageUris.size();
            updateImageView();
        }
    }

    private void deleteCurrentImage() {
        if (imageUris.size() > 0) {
            imageUris.remove(currentImageIndex);
            if (currentImageIndex >= imageUris.size()) {
                currentImageIndex = 0;
            }
            updateImageView();
            updateRecyclerView();
        }
    }

    private void updateImageView() {
        if (imageUris.size() > 0) {
            foodImageView.setImageURI(imageUris.get(currentImageIndex));
        } else {
            foodImageView.setImageResource(R.drawable.baseline_add_a_photo_24); // Default image
        }
    }

    private void setupRecyclerView() {
        imageNameAdapter = new ImageNameAdapter(this, imageUris, progressBar);
        imageNameViewer.setAdapter(imageNameAdapter);
        imageNameViewer.setLayoutManager(new LinearLayoutManager(this));
    }

    private void updateRecyclerView() {
        imageNameAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImages();
            } else {
                Toast.makeText(this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
            }
        }
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


    // from here to ****************************************************
    private void addRecipeToFirebase() {
        // Retrieve user ID (Assuming you have a way to authenticate users)
        String userId = getCurrentUserId(); // Implement this method to get current user ID

        // Collect recipe details from the user
        String recipeName = ((EditText) findViewById(R.id.recipyNameText)).getText().toString().trim();
        String ingredients = ((EditText) findViewById(R.id.ingredientText)).getText().toString().trim();
        String recipeSteps = ((EditText) findViewById(R.id.recipeText)).getText().toString().trim();
        String selectedCategory = categorySpinner.getSelectedItem().toString();

        if (recipeName.isEmpty() || ingredients.isEmpty() || recipeSteps.isEmpty()) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (imageUris.isEmpty()) {
            Toast.makeText(this, "Please add at least one photo", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show the ProgressBar
        progressBar.setVisibility(View.VISIBLE);

        // Create a map for the recipe data
        Map<String, Object> recipeData = new HashMap<>();
        recipeData.put("name", recipeName);
        recipeData.put("ingredients", ingredients);
        recipeData.put("steps", recipeSteps);
        recipeData.put("category", selectedCategory);  // Add the selected category
        recipeData.put("userId", userId);  // Add user ID to the recipe data

        // Upload images and get URLs
        uploadImagesAndSaveRecipe(recipeData);
    }



    private void uploadImagesAndSaveRecipe(Map<String, Object> recipeData) {
        ArrayList<String> imageUrls = new ArrayList<>();

        for (Uri imageUri : imageUris) {
            StorageReference imageRef = storage.getReference().child("images/" + imageUri.getLastPathSegment());

            imageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri downloadUri) {
                            imageUrls.add(downloadUri.toString());

                            // Check if all images have been uploaded
                            if (imageUrls.size() == imageUris.size()) {
                                recipeData.put("imageUrls", imageUrls);
                                saveRecipeToFirestore(recipeData);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "Failed to get download URL", e);
                            Toast.makeText(add_recipe.this, "Failed to upload images", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE); // Hide ProgressBar on failure
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "Failed to upload image", e);
                    Toast.makeText(add_recipe.this, "Failed to upload images", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE); // Hide ProgressBar on failure
                }
            });
        }
    }

    private void saveRecipeToFirestore(Map<String, Object> recipeData) {
      //   Get a new ID for the recipe
//        db.collection("recipes").document().add(recipeData)
//                                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void unused) {
//                                        Toast.makeText(add_recipe.this, "Recipe added successfully", Toast.LENGTH_SHORT).show();
//                                        progressBar.setVisibility(View.GONE); // Hide ProgressBar on success
//                                        finish();
//                                    }
//                                }).addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        Log.e(TAG, "Failed to add recipe", e);
//                                        Toast.makeText(add_recipe.this, "Failed to add recipe", Toast.LENGTH_SHORT).show();
//                                        progressBar.setVisibility(View.GONE); // Hide ProgressBar on failure
//                                    }
//                                });

//        db.runTransaction(new Transaction.Function<Long>() {
//            @Nullable
//            @Override
//            public Long apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
//                DocumentReference counterRef = db.collection("recipes").document(COUNTER_DOC);
//                Long currentId = transaction.get(counterRef).getLong(COUNTER_FIELD);
//                long newId = (currentId != null ? currentId : 0) + 1;
//                transaction.update(counterRef, COUNTER_FIELD, newId);
//                return newId;
//            }
//        }).addOnSuccessListener(new OnSuccessListener<Long>() {
//            @Override
//            public void onSuccess(Long newId) {
//                recipeData.put("id", newId);
//                Log.e(TAG, getCurrentUserId()+"---------"+String.valueOf(newId));
//                // Save recipe to Firestore with userId included
//
                db.collection("users").document(getCurrentUserId()).collection("recipes")
                        .add(recipeData)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                progressBar.setVisibility(View.GONE); // Hide ProgressBar on success
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.e(TAG, "Failed to get new recipe ID", e);
//                Toast.makeText(add_recipe.this, "Failed to add recipe", Toast.LENGTH_SHORT).show();
//                progressBar.setVisibility(View.GONE); // Hide ProgressBar on failure
//            }
//        });
    }


}
