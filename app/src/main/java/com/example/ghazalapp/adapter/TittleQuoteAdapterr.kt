package com.example.ghazalapp.adapter


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ScaleGestureDetector
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ghazalapp.QuoteViewActivity
import com.example.ghazalapp.adapter.TittleQuoteAdapterr.ViewHolder
import com.example.ghazalapp.databinding.TitleItemviewBinding
import com.example.ghazalapp.quotesData.Quote

class TittleQuoteAdapterr(private val quotes: List<Quote>, val context: Context,  val type: String ) : RecyclerView.Adapter<ViewHolder>() {

    private var scaleFactor = 1.0f // Default scale factor
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Using ViewBinding to inflate the layout
        val binding = TitleItemviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        // Return the ViewHolder with the binding
        return ViewHolder(binding)
    }




    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Bind data here
        // Bind data to views
        val quote = quotes[position]
        // Create a new ScaleGestureDetector for each item
        //        holder.binding.numberCount.text = ".${quote.id}" // Display id
        holder.binding.title.text = "${quote.quote}" // Display quote
        holder.binding.author.text = "${quote.author}"
        holder.binding.titleTv.text = "${quote.title}"

        // Set a touch listener on the container to handle scaling gestures


        holder.itemView.setOnClickListener() {
            val intent = Intent( context , QuoteViewActivity::class.java)
            intent.putExtra("id", quote.id)
            intent.putExtra("author", quote.author)
            intent.putExtra("title", quote.title)
            intent.putExtra("quote", quote.quote)
            context.startActivity(intent)

        }

    }

    override fun getItemCount(): Int {
        // Return the item count (currently returning 0)
        return quotes.size // Return the total number of quotes
    }

    inner class ViewHolder(val binding: TitleItemviewBinding) :
        RecyclerView.ViewHolder(binding.root)

}