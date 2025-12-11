package com.example.ghazalapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
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

    // Define RecyclerView and Adapter
    var jsonString: String = ""

    private lateinit var quoteAdapterr1: QuoteAdapterr // Or your custom adapter like QuoteAdapter
    lateinit var binding: ActivityNazamBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNazamBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.detailTittleList.layoutManager = LinearLayoutManager(this)
        if (intent.hasExtra("mainObject")) {
            jsonString = intent.getStringExtra("mainObject").toString()
        }
        binding.backArrow.setOnClickListener() {
            onBackPressed()
        }


        val filejsonString = readFile("jsonformatter.json")
//        Log.d("JSON", jsonString)
//
        //conversion from string to JSON object
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch() {
            val job = launch {


//
                fetchdata(filejsonString, jsonString)
                Log.d("size", "" + quotes.size)
            }
            job.invokeOnCompletion {
                launch {
                    CompleteCallBack()
                }
            }

        }


    }


    @SuppressLint("NotifyDataSetChanged")
    suspend fun CompleteCallBack() {
        withContext(Dispatchers.Main) {

            quoteAdapterr1 = QuoteAdapterr(
                quotes,
                this@FetchDataCategoryActivity, jsonString
            ) // Pass the data to your adapter
            binding.detailTittleList.adapter = quoteAdapterr1

            quoteAdapterr1.notifyDataSetChanged()
            Log.d("adapter ", quoteAdapterr1.itemCount.toString())
            Log.d("listsize ", quotes.size.toString())
        }
    }


    //first you need to read the json file from assets
    @Throws(IOException::class)
    fun readFile(fileName: String): String {
        val reader: BufferedReader =
            BufferedReader(InputStreamReader(assets.open(fileName), "UTF-8"))
        val content = StringBuilder()
        var line: String?

        while (reader.readLine().also { line = it } != null) {
            content.append(line)
        }

        return content.toString()
    }

    fun fetchdata(filetext: String, type: String) {
        Log.d("tag22", type)
        val obj = JSONObject(filetext)
        val mquotes = obj.getJSONObject(type)
        val qatatkey = mquotes.keys()
        while (qatatkey.hasNext()) {
            val key = qatatkey.next()

            val quoteObject = mquotes.getJSONObject(key)
//            Log.d("tag23",)
            //Extract the title for each quote object (assuming the key exists)
            val title = quoteObject.getString("title")
            binding.textNazam.text = type
            val id = quoteObject.getString("id")
//            // Add the Quote object (with an ID, and empty string for other fields) to the list
            quotes.add(Quote(id, "", "", title))
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }


}

