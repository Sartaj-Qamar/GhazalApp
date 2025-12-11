package com.example.ghazalapp

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ghazalapp.adapter.FavquoteAdapterr
import com.example.ghazalapp.databinding.ActivityFavoriteQuoteBinding
import com.example.ghazalapp.quotesData.Quote
import io.paperdb.Paper

class FavoriteQuoteActivity : AppCompatActivity() {
    lateinit var binding: ActivityFavoriteQuoteBinding
    private lateinit var quoteAdapterr1: FavquoteAdapterr // Or your custom adapter like QuoteAdapter
    private var addfavList: ArrayList<Quote> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteQuoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.favouriteList.layoutManager = LinearLayoutManager(this)
        Paper.init(this)



//// Read the favorite list from Paper DB
        val favList: List<Quote> = Paper.book().read("favorite") ?: emptyList()
//// If the list is null, you can use an empty list as a fallback
        Log.d("TAGhyfav", "onCreate: $favList")
        quoteAdapterr1 = FavquoteAdapterr(favList, this@FavoriteQuoteActivity)
        binding.favouriteList.adapter = quoteAdapterr1

        if (quoteAdapterr1.itemCount == 0) {
            Log.d("TAGhyfav", "onCreate: ${quoteAdapterr1.itemCount}")
            binding.emptyFavIv.visibility = View.VISIBLE
            binding.emptyFav.visibility = View.VISIBLE
        } else {
            binding.emptyFavIv.visibility = View.GONE
            binding.emptyFav.visibility = View.GONE
        }


    }
}