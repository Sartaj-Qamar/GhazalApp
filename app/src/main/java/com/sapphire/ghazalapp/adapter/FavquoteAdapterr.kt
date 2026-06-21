package com.sapphire.ghazalapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.sapphire.ghazalapp.DetailDataGhazalActivity
import com.sapphire.ghazalapp.adapter.FavquoteAdapterr.ViewHolder
import com.sapphire.ghazalapp.databinding.FavquoteItemviewBinding
import com.sapphire.ghazalapp.quotesData.Quote
import io.paperdb.Paper

class FavquoteAdapterr(
    private var quotes: List<Quote>, 
    val context: Context,
    private val onDelete: () -> Unit
) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FavquoteItemviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val quote = quotes[position]
        holder.binding.numberCount.text = "${quote.id}"
        holder.binding.title.text = "(${quote.author})"
        holder.binding.titleTv.text = quote.title

        // Remove from Favorites logic
        holder.binding.favouriteBottom.setOnClickListener {
            val currentFavs: MutableList<Quote> = Paper.book().read<MutableList<Quote>>("favorite") ?: mutableListOf()
            currentFavs.removeAll { it.id == quote.id }
            Paper.book().write("favorite", currentFavs)
            
            Toast.makeText(context, "Removed from Favorites", Toast.LENGTH_SHORT).show()
            onDelete() // Refresh activity
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailDataGhazalActivity::class.java).apply {
                putExtra("id", quote.id)
                putExtra("author", quote.author)
                putExtra("title", quote.title)
                putExtra("quote", quote.quote)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = quotes.size

    inner class ViewHolder(val binding: FavquoteItemviewBinding) : RecyclerView.ViewHolder(binding.root)
}
