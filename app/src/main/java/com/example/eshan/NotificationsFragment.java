package com.example.eshan;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {

    private List<NotificationItem> notificationList;
    private NotificationAdapter notificationAdapter;
    private RecyclerView notificationRecyclerView;
    private FirebaseFirestore db;
    public NotificationsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        db = FirebaseFirestore.getInstance();
        // Initialize RecyclerView and LayoutManager
        notificationRecyclerView = view.findViewById(R.id.notificationRecyclerView);
        notificationRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        // Initialize notification list
        notificationList = new ArrayList<>();
        notificationList.add(new NotificationItem("Title 1", "Message 1", "https://example.com/image1.jpg"));
        notificationList.add(new NotificationItem("Title 2", "Message 2", "https://example.com/image2.jpg"));
        final ArrayList<NotificationItem> NotificationList = new ArrayList<>();

        // Get the query snapshot
       db.collection("notification")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "DocumentSnapshot added with ID: ");
                            for (DocumentSnapshot document : task.getResult()) {
                            //    Log.d("TAG", document.getString("message"));
                              //  NotificationItem notification = document.toObject(NotificationItem.class);
                                NotificationList.add(new NotificationItem( "https://example.com/image1.jpg",document.getString("title"), document.getString("message")));
                             //   NotificationList.add(notification);
                            }
                            notificationAdapter = new NotificationAdapter(NotificationList);
                            notificationRecyclerView.setAdapter(notificationAdapter);
                         //   adapter.notifyDataSetChanged();
                        } else {
                            // Handle the error here
                        }
                    }
                });
        // Initialize adapter


        return view;
    }
}
