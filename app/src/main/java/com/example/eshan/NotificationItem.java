package com.example.eshan;

public class NotificationItem {
    private String imageResId;
    private String title;
    private String message;
    public NotificationItem() {
        // No-argument constructor
    }
    public NotificationItem(String imageResId, String title, String message) {
        this.imageResId = imageResId;
        this.title = title;
        this.message = message;
    }



    public String getImageResId() {
        return imageResId;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }


}
