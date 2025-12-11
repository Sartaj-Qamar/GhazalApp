package com.example.ghazalapp.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.ghazalapp.adapter.PdfAdapterr.ViewHolder
import com.example.ghazalapp.databinding.PdfFileviewBinding
import com.example.ghazalapp.quotesData.PdfModel

class PdfAdapterr(private val quotes: List<PdfModel>, val context: Context) : RecyclerView.Adapter<ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Using ViewBinding to inflate the layout
        val binding =PdfFileviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        // Return the ViewHolder with the binding
        return ViewHolder(binding)
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Bind data here
        // Bind data to views
        val quote = quotes[position]

        holder.binding.pdfSize.text = quote.size
        holder.binding.pdfName.text = quote.name

        holder.itemView.setOnClickListener {
            openPdf(quote.path1)
        Toast.makeText(context, "hygy", Toast.LENGTH_SHORT).show()
        }



    }

    override fun getItemCount(): Int {
        Log.d("MyApp", quotes.size.toString())
        return quotes.size // Return the total number of quotes
    }
//    private fun sharePdf(name: String) {
//        val file = File(Environment.getExternalStorageDirectory().absolutePath + "/example.pdf")
//        val intent = Intent(Intent.ACTION_VIEW).apply {
//            setDataAndType(Uri.fromFile(file), "application/pdf")
//            flags = Intent.FLAG_ACTIVITY_NO_HISTORY
//        }
//        context.startActivity(intent)
//
//    }
    fun openPdf(file: String) {


        val path: Uri = Uri.parse(file)

        val pdfOpenIntent = Intent(Intent.ACTION_VIEW).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            setDataAndType(path, "application/pdf")
        }
        context.startActivity(pdfOpenIntent)


    }
    inner class ViewHolder(val binding: PdfFileviewBinding) :
        RecyclerView.ViewHolder(binding.root)

}