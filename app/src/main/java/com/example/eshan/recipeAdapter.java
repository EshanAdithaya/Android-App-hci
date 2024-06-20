package com.example.eshan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.List;

public class recipeAdapter extends ArrayAdapter<recipes> {
    private Context context;
    private List<recipes> recipesList;

    public recipeAdapter(@NonNull Context context, List<recipes> recipesList) {
        super(context, 0, recipesList);
        this.context = context;
        this.recipesList = recipesList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.recipe_item, parent, false);
        }

        recipes recipe = recipesList.get(position);

        TextView nameTextView = convertView.findViewById(R.id.recipe_name);
        ImageView imageView = convertView.findViewById(R.id.recipe_image);

        nameTextView.setText(recipe.getName());
        // Load image using Glide or any other image loading library
        if (recipe.getImageUrls() != null && !recipe.getImageUrls().isEmpty()) {
            Glide.with(context).load(recipe.getImageUrls().get(0)).into(imageView);
        } else {
            // Handle case where there are no images
            imageView.setImageDrawable(null);
        }

        return convertView;
    }
}
