package com.example.ghazalapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ghazalapp.adapter.ImageViewBgAdapter.ViewHolder
import com.example.ghazalapp.databinding.ImageViewScreenBinding
import com.example.ghazalapp.interFace.ImageInterface

class ImageViewBgAdapter(
    private val imageBg: List<Int>,
    val context: Context,
    private val imageInterface: ImageInterface

): RecyclerView.Adapter<ViewHolder>() {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ImageViewScreenBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val imagebg = imageBg[position]
        holder.binding.bgPic.setImageResource(imagebg)


        holder.itemView.setOnClickListener(){
            imageInterface.imageBackground(imagebg,true,position)
        }


    }

    override fun getItemCount(): Int {
    return  imageBg.size
    }
    inner class ViewHolder(val binding: ImageViewScreenBinding) :
        RecyclerView.ViewHolder(binding.root)

}