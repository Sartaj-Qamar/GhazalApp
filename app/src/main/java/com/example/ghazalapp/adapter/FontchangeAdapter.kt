package com.example.ghazalapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ghazalapp.R
import com.example.ghazalapp.adapter.FontchangeAdapter.ViewHolder
import com.example.ghazalapp.databinding.TextfontchangeBinding
import com.example.ghazalapp.interFace.BackgroundColorInterface
import com.example.ghazalapp.quotesData.AppFont

class FontchangeAdapter(
     val mfont: ArrayList<AppFont>,
    val context: Context,
    private val backgroundColorInterface: BackgroundColorInterface

): RecyclerView.Adapter<ViewHolder>() {
    private var selectedPosition: Int = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TextfontchangeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(
        holder: ViewHolder, position: Int) {
        val fontname = mfont[position].fontName
        val fontid = mfont[position].fontid
 holder.binding.fonttextChange.text = fontname.toString()
        val typeface = ResourcesCompat.getFont(context, mfont[position].fontid)
            holder.binding.fonttextChange.typeface = typeface
//        val selectedPosition = holder.adapterPosition
        // Reset the background for all items
        if (position == selectedPosition) {
            holder.binding.fontCv.setBackgroundResource(R.drawable.style_button_background)
            holder.binding.fonttextChange.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.white))
        } else {
            holder.binding.fontCv.setBackgroundResource(R.drawable.unselected_fontcolor) // Default background for unselected items
            holder.binding.fonttextChange.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.black))
        }

        // Set an OnClickListener to handle background color change when the item is clicked
        holder.itemView.setOnClickListener {
            // Update the selected position
            selectedPosition = position
            backgroundColorInterface.textfont(fontid)
            notifyDataSetChanged()
        }

    }



    override fun getItemCount(): Int {
    return mfont.size
    }
    inner class ViewHolder(val binding: TextfontchangeBinding) :
        RecyclerView.ViewHolder(binding.root)

}