package com.example.ghazalapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ghazalapp.DetailDataGhazalActivity
import com.example.ghazalapp.adapter.FavquoteAdapterr.ViewHolder
import com.example.ghazalapp.databinding.FavquoteItemviewBinding
import com.example.ghazalapp.quotesData.Quote

class FavquoteAdapterr(private val quotes: List<Quote>, val context: Context) :
    RecyclerView.Adapter<ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Using ViewBinding to inflate the layout
        val binding =
            FavquoteItemviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        // Return the ViewHolder with the binding
        return ViewHolder(binding)
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Bind data here
        // Bind data to views
        val quote = quotes[position]
        holder.binding.numberCount.text = "${quote.id}" // Display id
        holder.binding.title.text = "(${quote.author})" // Display quote
//        holder.binding.author.text = "- ${quote.author}"
        holder.binding.titleTv.text = "${quote.title}"
        holder.binding.favouriteBottom.setOnClickListener(){

        }
        // Display author
        holder.itemView.setOnClickListener() {
            val intent = Intent(context, DetailDataGhazalActivity::class.java)
            intent.putExtra("id", quote.id)
            intent.putExtra("author", quote.author)
            intent.putExtra("title", quote.title)
            intent.putExtra("quote", quote.quote)
            context.startActivity(intent)

        }


    }

    override fun getItemCount(): Int {
        // Return the item count (currently returning 0)
        Log.d("MyApp", quotes.size.toString())
        return quotes.size // Return the total number of quotes
    }

    inner class ViewHolder(val binding: FavquoteItemviewBinding) :
        RecyclerView.ViewHolder(binding.root)

}