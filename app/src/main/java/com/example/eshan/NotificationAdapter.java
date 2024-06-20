package com.example.eshan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private List<NotificationItem> items;

    private Context context;

    public NotificationAdapter(List<NotificationItem> items) {
        this.context=context;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NotificationItem item = items.get(position);
        holder.title.setText(item.getTitle());
        holder.content.setText(item.getMessage());

        if (holder.imageView != null) {
            // Assuming 'imageUrl' is your image URL variable
//            Glide.with(context).load(item.getImageResId()).into(holder.imageView);
        } else {
            // Handle the case where the ImageView is null (e.g., log an error)
        }
       // Glide.with(this.context).load(item.getImageResId()).into(holder.imageView);
      //  holder.descriptionTextView.setText(item.getDescription());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

//        private TextView titleTextView;
//        private TextView descriptionTextView;
TextView title ;

TextView content ;

        ImageView imageView ;
        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.notificationTitle);
            content = itemView.findViewById(R.id.notificationMessage);
            imageView = itemView.findViewById(R.id.notificationImage);
        }
    }
}