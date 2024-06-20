package com.example.eshan;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private FirebaseAuth mAuth;

    public NotificationsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Initialize RecyclerView and LayoutManager
        notificationRecyclerView = view.findViewById(R.id.notificationRecyclerView);
        notificationRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        // Initialize notification list
        notificationList = new ArrayList<>();
        notificationList.add(new NotificationItem("Title 1", "Message 1", "https://example.com/image1.jpg"));
        notificationList.add(new NotificationItem("Title 2", "Message 2", "https://example.com/image2.jpg"));
        final ArrayList<NotificationItem> NotificationList = new ArrayList<>();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            // User is logged in, proceed with fetching notifications
            db.collection("notification")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                Log.d("TAG", "DocumentSnapshot added with ID: ");
                                for (DocumentSnapshot document : task.getResult()) {
                                    NotificationList.add(new NotificationItem(
                                            "https://example.com/image1.jpg",
                                            document.getString("title"),
                                            document.getString("message")));
                                }
                                notificationAdapter = new NotificationAdapter(NotificationList);
                                notificationRecyclerView.setAdapter(notificationAdapter);
                            } else {
                                // Handle the error here
                                Log.w("TAG", "Error getting documents.", task.getException());
                            }
                        }
                    });
        } else {
            // User is not logged in, show alert dialog
            new AlertDialog.Builder(getContext())
                    .setTitle("Please Log In")
                    .setMessage("You need to log in first to view notifications.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }

        return view;
    }
}
