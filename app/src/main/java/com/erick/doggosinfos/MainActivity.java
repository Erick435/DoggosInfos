package com.erick.doggosinfos;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import android.graphics.Color;
import android.graphics.Typeface;
import android.content.Intent;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;

import android.view.Gravity;
import android.view.LayoutInflater;

import android.widget.PopupWindow;
import android.widget.TextView;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ImageView dogImage;
    private Button dislikeButton, loveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dogImage = findViewById(R.id.dogImage);
        dislikeButton = findViewById(R.id.dislikeButton);
        loveButton = findViewById(R.id.loveButton);

        // Load random dog image on app start
        loadRandomDogImage();

        // Set click listener for Dislike button
        dislikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show the popup menu
                showPopupMenu(view);
            }
        });

        // Set click listener for Love button
        loveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadRandomDogImage();
            }
        });

        // Find the navigation buttons
        TextView voteButton = findViewById(R.id.voteButton);
        TextView factsButton = findViewById(R.id.factsButton);

        // Set click listeners for the navigation buttons
        voteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Load the MainActivity
                loadMainActivity();
            }
        });

        factsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Load the FactsActivity
                loadFactsActivity();
            }
        });

        // Set "Vote" button as initially selected
        setNavButtonSelected(voteButton);
        setNavButtonUnselected(factsButton);

    }

    //Changing and affecting how one is clicked for the navbar
    private void setNavButtonSelected(TextView button) {
        button.setBackgroundColor(ContextCompat.getColor(this, R.color.grey));
        button.setTextColor(Color.WHITE);
        button.setTypeface(null, Typeface.BOLD);
    }

    private void setNavButtonUnselected(TextView button) {
        button.setBackgroundColor(Color.TRANSPARENT);
        button.setTextColor(Color.WHITE);
        button.setTypeface(null, Typeface.NORMAL);
    }

    private void loadMainActivity() {
        // Implement the logic to navigate to the main activity here
        // For example, you can use an Intent to start the activity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void loadFactsActivity() {
        // Implement the logic to navigate to the facts activity here
        // For example, you can use an Intent to start the activity
        Intent intent = new Intent(this, FactsActivity.class);
        startActivity(intent);
    }



    private void showPopupMenu(View view) {
        // Inflate the popup_menu layout
        View popupView = LayoutInflater.from(this).inflate(R.layout.popup_menu, null);

        // Create a PopupWindow
        int width = getResources().getDimensionPixelSize(R.dimen.popup_menu_width);
        int height = getResources().getDimensionPixelSize(R.dimen.popup_menu_height);
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

        // Set the position of the PopupWindow
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // Set the text for the message
        TextView messageText = popupView.findViewById(R.id.messageText);
//        messageText.setText("Sorry, this button isn't working, how about you try the other button and love dogs instead ;)");

        // Set the click listener for the "I understand and I'm sorry" button

        Button closeButton = popupView.findViewById(R.id.closeButton);
//        closeButton.setText("I understand");
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dismiss the popup window when the button is clicked
                popupWindow.dismiss();
            }
        });
    }


    private void loadRandomDogImage() {
        ApiClient.getClient().getRandomDogImage().enqueue(new Callback<List<DogImage>>() {
            @Override
            public void onResponse(Call<List<DogImage>> call, Response<List<DogImage>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().size() > 0) {
                    String imageUrl = response.body().get(0).getUrl();
                    // Load the random dog image using Glide library
                    Glide.with(MainActivity.this)
                            .load(imageUrl)
                            .into(new DrawableImageViewTarget(dogImage) {
                                @Override
                                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                    super.onLoadFailed(errorDrawable);
                                    Toast.makeText(MainActivity.this, "Failed to load dog image.", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                    super.onLoadCleared(placeholder);
                                    Toast.makeText(MainActivity.this, "Cleared image.", Toast.LENGTH_SHORT).show();
                                }


                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    super.onResourceReady(resource, transition);
                                    // Stop loading animation if needed
                                }
                            });
                } else {
                    // Handle API error
                }
            }

            @Override
            public void onFailure(Call<List<DogImage>> call, Throwable t) {
                // Handle API failure
            }
        });
    }
}
