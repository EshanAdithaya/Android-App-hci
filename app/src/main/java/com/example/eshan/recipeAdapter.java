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
    private List<recipes> recipes;

    public recipeAdapter(@NonNull Context context, List<recipes> recipes) {
        super(context, 0, recipes);
        this.context = context;
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.recipe_item, parent, false);
        }

        recipes recipe = recipes.get(position);

        TextView nameTextView = convertView.findViewById(R.id.recipe_name);
        ImageView imageView = convertView.findViewById(R.id.recipe_image);

        nameTextView.setText(recipe.getName());
        Glide.with(context).load(recipe.getImageUrls().get(0)).into(imageView);
        return convertView;
    }




}