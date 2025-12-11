package com.example.ghazalapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ghazalapp.DetailDataGhazalActivity
import com.example.ghazalapp.adapter.QuoteAdapterr.ViewHolder
import com.example.ghazalapp.databinding.ItemViewScreenBinding
import com.example.ghazalapp.quotesData.Quote

class QuoteAdapterr(private val quotes: List<Quote>, val context: Context,val type: String) : RecyclerView.Adapter<ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Using ViewBinding to inflate the layout
        val binding = ItemViewScreenBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        // Return the ViewHolder with the binding
        return ViewHolder(binding)
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Bind data here
        // Bind data to views
        val quote = quotes[position]
        holder.binding.numberCount.text = ".${quote.id}" // Display id
//        holder.binding.title.text = "\"${quote.quote}\"" // Display quote
//        holder.binding.author.text = "- ${quote.author}"
        holder.binding.titleTv.text = "${quote.title}"

        // Display author
        holder.itemView.setOnClickListener() {
            val intent = Intent( context , DetailDataGhazalActivity::class.java)
            intent.putExtra("mainObject", type)
            Log.d("MyputApp", type)
            intent.putExtra("keyObject",quote.title)

            intent.putExtra("id", quote.id)

            context.startActivity(intent)

        }



    }

    override fun getItemCount(): Int {
        // Return the item count (currently returning 0)
        Log.d("MyApp", quotes.size.toString())
        return quotes.size // Return the total number of quotes
    }

    inner class ViewHolder(val binding: ItemViewScreenBinding) :
        RecyclerView.ViewHolder(binding.root)

}