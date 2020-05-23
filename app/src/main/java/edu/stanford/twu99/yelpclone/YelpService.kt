package edu.stanford.twu99.yelpclone

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

public interface YelpService {

    @GET("businesses/search")
    fun searchRestaurants(
        @Header("Authorization") authHeader: String,
        @Query("term") term: String,
        @Query("location") location: String
    ): Call<YelpSearchResult>

}