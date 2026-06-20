package com.example.ghazalapp

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ghazalapp.adapter.FavquoteAdapterr
import com.example.ghazalapp.databinding.ActivityFavoriteQuoteBinding
import com.example.ghazalapp.quotesData.Quote
import io.paperdb.Paper

class FavoriteQuoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteQuoteBinding
    private lateinit var quoteAdapterr1: FavquoteAdapterr
    private var favList: ArrayList<Quote> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_FULLSCREEN
        )
        
        binding = ActivityFavoriteQuoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        binding.favouriteList.layoutManager = LinearLayoutManager(this)
        Paper.init(this)

        binding.backArrow.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        loadFavorites()
    }

    private fun loadFavorites() {
        // Read the favorite list from Paper DB
        val list: List<Quote> = Paper.book().read("favorite") ?: emptyList()
        favList.clear()
        favList.addAll(list)
        
        quoteAdapterr1 = FavquoteAdapterr(favList, this@FavoriteQuoteActivity) {
            // Callback when an item is removed
            loadFavorites()
        }
        binding.favouriteList.adapter = quoteAdapterr1

        // Toggle visibility of the entire empty state container
        if (favList.isEmpty()) {
            binding.emptyLayout.visibility = View.VISIBLE
            binding.favouriteList.visibility = View.GONE
        } else {
            binding.emptyLayout.visibility = View.GONE
            binding.favouriteList.visibility = View.VISIBLE
        }
    }
}
