package com.example.ghazalapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ghazalapp.adapter.QuoteAdapterr
import com.example.ghazalapp.databinding.ActivityNazamBinding
import com.example.ghazalapp.quotesData.Quote
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class FetchDataCategoryActivity : AppCompatActivity() {

    private var quotes: ArrayList<Quote> = ArrayList()
    private var jsonString: String = ""
    private lateinit var quoteAdapterr1: QuoteAdapterr
    private lateinit var binding: ActivityNazamBinding

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

        binding = ActivityNazamBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        binding.detailTittleList.layoutManager = LinearLayoutManager(this)
        
        jsonString = intent.getStringExtra("mainObject") ?: ""
        binding.textNazam.text = jsonString

        binding.backArrow.setOnClickListener {
            onBackPressed()
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val filejsonString = readFile("jsonformatter.json")
                fetchdata(filejsonString, jsonString)
                withContext(Dispatchers.Main) {
                    completeCallBack()
                }
            } catch (e: Exception) {
                Log.e("FetchData", "Error loading data", e)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private suspend fun completeCallBack() {
        quoteAdapterr1 = QuoteAdapterr(
            quotes,
            this@FetchDataCategoryActivity, jsonString
        )
        binding.detailTittleList.adapter = quoteAdapterr1
        quoteAdapterr1.notifyDataSetChanged()
    }

    private fun readFile(fileName: String): String {
        return try {
            assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (e: IOException) {
            ""
        }
    }

    private fun fetchdata(filetext: String, type: String) {
        if (filetext.isEmpty() || type.isEmpty()) return
        
        try {
            val obj = JSONObject(filetext)
            if (!obj.has(type)) return
            
            val mquotes = obj.getJSONObject(type)
            val keys = mquotes.keys()
            
            val tempQuotes = ArrayList<Quote>()
            while (keys.hasNext()) {
                val key = keys.next()
                val quoteObject = mquotes.getJSONObject(key)
                
                val title = quoteObject.optString("title", "")
                val id = quoteObject.optString("id", "")
                
                tempQuotes.add(Quote(id, "", "", title))
            }
            quotes.clear()
            quotes.addAll(tempQuotes)
        } catch (e: Exception) {
            Log.e("FetchData", "JSON Parsing error", e)
        }
    }
}
