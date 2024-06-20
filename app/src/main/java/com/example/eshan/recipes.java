package com.example.eshan;

import android.widget.ProgressBar;

import java.util.List;

public class recipes {

    private String name;
    private List<String> imageUrls;

    private String ingredients;
    private String steps;

//    private ProgressBar progressBar;
    public recipes() {
        // No-argument constructor
    }

    public recipes(String name, List<String> imageUrls, String ingredients, String steps) {
        this.name = name;
        this.imageUrls = imageUrls;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    public String getName() {
        return name;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getSteps() {
        return steps;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }


}
