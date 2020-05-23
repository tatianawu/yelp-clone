package edu.stanford.twu99.yelpclone

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val API_KEY =
    "jEE60QI7eLJGXd1ksgJxIKm35-f8VzsUYpeg76CBr8492ayLVuoM7HoxQ7W6gn6X_LtnPW8vJ1-o1_l24D3scWnRZxj5nf3uGndITtEgQ_ITsqHIQWW8xSmW13HAXnYx"
private const val BASE_URL = "https://api.yelp.com/v3/"
private const val INIT_TERM = "Avocado Toast"
private const val INIT_LOC = "New York"
private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Fill RecyclerView with some search results
        searchYelp(INIT_TERM, INIT_LOC)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.miSearch) {
            Log.i(TAG, "Tapped on search")

            showAlertDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * When user clicks on the Yelp icon (menu), open up a dialog box
     * to enter the search term and location.
     */
    private fun showAlertDialog() {
        val searchFormView = LayoutInflater.from(this).inflate(R.layout.dialog_search, null)
        val dialog = AlertDialog.Builder(this)
            .setTitle("Search Yelp")
            .setView(searchFormView)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("OK", null)
            .show()

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
            val term = searchFormView.findViewById<EditText>(R.id.etTerm).text.toString()
            val location = searchFormView.findViewById<EditText>(R.id.etLocation).text.toString()

            if (term.trim().isEmpty() || location.trim().isEmpty()) {
                Toast.makeText(
                    this,
                    "Must input both search description and location",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            searchYelp(term, location)
            dialog.dismiss()
        }
    }

    /**
     * Query the Yelp API with the provided search term and location.
     * If Yelp returns a valid response, update the RecyclerView.
     */
    private fun searchYelp(term: String, location: String) {
        val restaurants = mutableListOf<YelpRestaurant>()
        val adapter = RestaurantsAdapter(this, restaurants)
        rvRestaurants.adapter = adapter
        rvRestaurants.layoutManager = LinearLayoutManager(this)

        val retrofit =
            Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .build()
        val yelpService = retrofit.create(YelpService::class.java)
        yelpService.searchRestaurants("Bearer $API_KEY", term, location)
            .enqueue(object : Callback<YelpSearchResult> {
                override fun onResponse(
                    call: Call<YelpSearchResult>,
                    response: Response<YelpSearchResult>
                ) {
                    Log.i(TAG, "onResponse $response")

                    val body = response.body()
                    if (body == null) {
                        Log.w(TAG, "Did not receive valid response body from Yelp API... exiting")
                        return
                    }

                    restaurants.addAll(body.restaurants)
                    adapter.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<YelpSearchResult>, t: Throwable) {
                    Log.i(TAG, "onResponse $t")
                }
            })
    }
}
