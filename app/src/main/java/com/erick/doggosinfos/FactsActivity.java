package com.erick.doggosinfos;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import androidx.core.content.ContextCompat;
import android.graphics.Color;
import android.graphics.Typeface;
import android.content.Intent;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call; // Add this import
import retrofit2.Callback; // Add this import
import retrofit2.Response; // Add this import
public class FactsActivity extends AppCompatActivity {

    private TextView breedInfo;
    private Spinner breedSpinner;
    private List<DogInfo> dogInfoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facts);

        breedInfo = findViewById(R.id.breedInfo);
        breedSpinner = findViewById(R.id.breedSpinner);

        // Load dog breeds and information from JSON file in assets folder
        loadDogBreedsFromApi();

        // Extract dog breed names
        List<String> dogBreeds = new ArrayList<>();
        for (DogInfo dogInfo : dogInfoList) {
            dogBreeds.add(dogInfo.getName());
        }

        // Set up spinner with dog breeds
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, dogBreeds);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        breedSpinner.setAdapter(adapter);

        // Set item selected listener for the breedSpinner
        breedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                DogInfo selectedDog = dogInfoList.get(position);
                int breedId = selectedDog.getId();
                showDogInfo(breedId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
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

        // Set "Facts" button as initially selected
        setNavButtonSelected(factsButton);
        setNavButtonUnselected(voteButton);

    }


    private void loadMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void loadFactsActivity() {
        // You are already in the FactsActivity, so no need to navigate to it again.
        // If you want to refresh the activity or update data, you can implement that logic here.
    }

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


    // Method to load dog information from JSON
//    private void loadDogBreedsFromApi() {
//        ApiClient.getClient().getDogBreeds().enqueue(new Callback<List<DogInfo>>() {
//            @Override
//            public void onResponse(Call<List<DogInfo>> call, Response<List<DogInfo>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    dogInfoList = response.body();
//                    List<String> dogBreeds = new ArrayList<>();
//                    for (DogInfo dogInfo : dogInfoList) {
//                        dogBreeds.add(dogInfo.getName());
//                    }
//                    ArrayAdapter<String> adapter = new ArrayAdapter<>(FactsActivity.this,
//                            android.R.layout.simple_spinner_item, dogBreeds);
//                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    breedSpinner.setAdapter(adapter);
//                } else {
//                    // Handle API error
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<DogInfo>> call, Throwable t) {
//                // Handle API failure
//            }
//        });
//    }

    private void loadDogBreedsFromApi() {
        ApiClient.getClient().getDogBreeds().enqueue(new Callback<List<DogInfo>>() {
            @Override
            public void onResponse(Call<List<DogInfo>> call, Response<List<DogInfo>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    dogInfoList = response.body();
                    List<String> dogBreeds = new ArrayList<>();
                    for (DogInfo dogInfo : dogInfoList) {
                        dogBreeds.add(dogInfo.getName());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(FactsActivity.this,
                            android.R.layout.simple_spinner_item, dogBreeds);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    breedSpinner.setAdapter(adapter);
                    Log.d("DogBreeds", "Dog breeds loaded successfully.");

                    // Call showDogInfo only after dogInfoList is populated
                    if (!dogInfoList.isEmpty()) {
                        showDogInfo(dogInfoList.get(0).getId()); // or any other breedId
                    }
                } else {
                    Log.d("DogBreeds", "API call failed or response body is null.");
                    // Handle API error
                    dogInfoList = new ArrayList<>(); // Initialize to an empty list on error
                }
            }

            @Override
            public void onFailure(Call<List<DogInfo>> call, Throwable t) {
                Log.d("DogBreeds", "API call failed: " + t.getMessage());
                // Handle API failure
                dogInfoList = new ArrayList<>(); // Initialize to an empty list on failure
            }
        });
    }





    private void showDogInfo(int breedId) {
        ApiClient.getClient().getDogInfo(breedId).enqueue(new Callback<DogInfo>() {
            @Override
            public void onResponse(Call<DogInfo> call, Response<DogInfo> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DogInfo dogInfo = response.body();
                    displayDogInfo(dogInfo);

                    //Fetch breed images
                    fetchBreedImages(breedId);
                } else {
                    // Handle API error
                }
            }

            @Override
            public void onFailure(Call<DogInfo> call, Throwable t) {
                // Handle API failure
            }
        });
    }

    private void fetchBreedImages(int breedId) {
        ApiClient.getClient().getDogImages(breedId).enqueue(new Callback<List<DogImage>>() {
            @Override
            public void onResponse(Call<List<DogImage>> call, Response<List<DogImage>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    List<DogImage> dogImages = response.body();
                    // Display the first image (you can adjust this according to your UI design)
                    displayDogImage(dogImages.get(0).getUrl());
                } else {
                    // Handle API error or empty response
                }
            }

            @Override
            public void onFailure(Call<List<DogImage>> call, Throwable t) {
                // Handle API failure
            }
        });
    }

    private void displayDogImage(String imageUrl) {
        ImageView dogImageView = findViewById(R.id.dogImage);
        Picasso.get().load(imageUrl).into(dogImageView);
    }


    private void displayDogInfo(DogInfo dogInfo) {
        // Use the dogInfo object to display the information on the UI
        StringBuilder infoBuilder = new StringBuilder();
        infoBuilder.append("Name: ").append(dogInfo.getName()).append("\n");
        infoBuilder.append("Weight: ").append(dogInfo.getWeight().getMetric()).append(" kg").append("\n");
        infoBuilder.append("Height: ").append(dogInfo.getHeight().getMetric()).append(" cm").append("\n");
        infoBuilder.append("Life Span: ").append(dogInfo.getLifeSpan()).append("\n");
        infoBuilder.append("Bred For: ").append(dogInfo.getBredFor()).append("\n");
        infoBuilder.append("Breed Group: ").append(dogInfo.getBreedGroup()).append("\n");

        breedInfo.setText(infoBuilder.toString());
    }

}


