package com.example.eshan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class add_recipe extends AppCompatActivity {

    private static final int REQUEST_CODE_PICK_IMAGES = 1;
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 2;

    private ArrayList<Uri> imageUris = new ArrayList<>();
    private int currentImageIndex = 0;

    private ImageView foodImageView;
    private Button addPhotoButton, previousImageButton, nextImageButton, deletePhotoButton;
    private RecyclerView imageNameViewer;
    private ImageNameAdapter imageNameAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        foodImageView = findViewById(R.id.food_image);
        addPhotoButton = findViewById(R.id.add_photo_button);
        previousImageButton = findViewById(R.id.previousImagebutton);
        nextImageButton = findViewById(R.id.nextImagebutton);
        deletePhotoButton = findViewById(R.id.delete_photo_button2);
        imageNameViewer = findViewById(R.id.imagenameviewer);

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
        imageNameAdapter = new ImageNameAdapter(this, imageUris);
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
}
