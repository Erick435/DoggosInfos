package com.erick.doggosinfos;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiClient {
    @GET("images/search")
    Call<List<DogImage>> getRandomDogImage();

    @GET("breeds/{breed_id}")
    Call<DogInfo> getDogInfo(@Path("breed_id") int breedId);

    @GET("breeds")
    Call<List<DogInfo>> getDogBreeds(); // Add this method


    @GET("images/search")
    Call<List<DogImage>> getDogImages(@Query("breed_ids") int breedId);

    // Add this static method to get the Retrofit instance
    static ApiClient getClient() {
        return RetrofitClient.getClient().create(ApiClient.class);
    }
}
