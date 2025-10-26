package com.example.musicapp

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.R // FIX 1: Use a simple import for R
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.emptyList

class MainActivity : AppCompatActivity() {

    lateinit var myRecyclerView: RecyclerView
    lateinit var myAdapter: MyAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // FIX 1: Reference R resources correctly
        setContentView(R.layout.activity_main)

        // FIX 1: Reference R resources correctly
        myRecyclerView= findViewById(R.id.recyclerView)
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://deezerdevs-deezer.p.rapidapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getData("eminem")

        retrofitData.enqueue(object : Callback<MyData?> {
            override fun onResponse(call: Call<MyData?>, response: Response<MyData?>) {

                // FIX 2: Handle null response safely (dataList must be non-nullable List<Track>)
                // If response.body() or data is null, use an empty list instead.
                val dataList = response.body()?.data ?: emptyList()

                // If MyAdapter expects List<Track>, we must tell the compiler the type is List<Track>
                // This is needed because response.body()?.data returns List<Track>?
                val tracks: List<Track> = dataList as List<Track>

                myAdapter=MyAdapter(this@MainActivity, tracks)
                myRecyclerView.adapter= myAdapter
                myRecyclerView.layoutManager= LinearLayoutManager(this@MainActivity)

                Log.d("TAG: onResponse", "onResponse: " + response.body())
            }

            override fun onFailure(call: Call<MyData?>, t: Throwable) {
                // if the API call is a failure then this method is executed
                Log.d( "onFailure", "onFailure: " + t.message)
            }
        })

        // FIX 3: Change R.id.main to R.id.root_layout (or the actual root ID from activity_main.xml)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.root_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}