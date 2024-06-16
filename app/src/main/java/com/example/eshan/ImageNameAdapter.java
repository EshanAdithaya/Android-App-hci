package com.example.eshan;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ImageNameAdapter extends RecyclerView.Adapter<ImageNameAdapter.ImageNameViewHolder> {

    private Context context;
    private ArrayList<Uri> imageUris;

    public ImageNameAdapter(Context context, ArrayList<Uri> imageUris) {
        this.context = context;
        this.imageUris = imageUris;
    }

    @NonNull
    @Override
    public ImageNameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_name_item, parent, false);
        return new ImageNameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageNameViewHolder holder, int position) {
        Uri imageUri = imageUris.get(position);
        String imageName = imageUri.getLastPathSegment();
        holder.imageNameTextView.setText(imageName);
    }

    @Override
    public int getItemCount() {
        return imageUris.size();
    }

    public static class ImageNameViewHolder extends RecyclerView.ViewHolder {
        TextView imageNameTextView;

        public ImageNameViewHolder(@NonNull View itemView) {
            super(itemView);
            imageNameTextView = itemView.findViewById(R.id.image_name_text_view);
        }
    }
}
