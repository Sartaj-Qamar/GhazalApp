package com.example.ghazalapp.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.ghazalapp.adapter.PdfAdapterr.ViewHolder
import com.example.ghazalapp.databinding.PdfFileviewBinding
import com.example.ghazalapp.quotesData.PdfModel
import java.io.File

class PdfAdapterr(
    private val quotes: MutableList<PdfModel>,
    val context: Context,
    private val onDelete: () -> Unit
) : RecyclerView.Adapter<ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Using ViewBinding to inflate the layout
        val binding = PdfFileviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        // Return the ViewHolder with the binding
        return ViewHolder(binding)
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Bind data here
        // Bind data to views
        val quote = quotes[position]

        holder.binding.pdfSize.text = quote.size
        holder.binding.pdfFolderName.text = quote.folderName

        // Open PDF on item click
        holder.itemView.setOnClickListener {
            openPdf(quote.path1)
        }

        // Delete PDF on delete button click
        holder.binding.deletePdf.setOnClickListener {
            deletePdf(quote.path1, position)
        }
    }

    override fun getItemCount(): Int {
        Log.d("MyApp", quotes.size.toString())
        return quotes.size // Return the total number of quotes
    }

    private fun openPdf(filePath: String) {
        try {
            val file = File(filePath)
            if (!file.exists()) {
                Toast.makeText(context, "PDF file not found", Toast.LENGTH_SHORT).show()
                return
            }

            // Use FileProvider for proper URI generation
            val uri: Uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            val pdfOpenIntent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_GRANT_READ_URI_PERMISSION
            }
            context.startActivity(pdfOpenIntent)
        } catch (e: Exception) {
            Log.e("PdfAdapter", "Error opening PDF: ${e.message}")
            Toast.makeText(context, "Unable to open PDF: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deletePdf(filePath: String, position: Int) {
        try {
            val file = File(filePath)
            if (file.exists()) {
                val deleted = file.delete()
                if (deleted) {
                    quotes.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, quotes.size)
                    Toast.makeText(context, "PDF deleted successfully", Toast.LENGTH_SHORT).show()
                    onDelete() // Notify activity to check if list is empty
                } else {
                    Toast.makeText(context, "Failed to delete PDF", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "PDF file not found", Toast.LENGTH_SHORT).show()
                quotes.removeAt(position)
                notifyItemRemoved(position)
                onDelete()
            }
        } catch (e: Exception) {
            Log.e("PdfAdapter", "Error deleting PDF: ${e.message}")
            Toast.makeText(context, "Error deleting PDF: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    inner class ViewHolder(val binding: PdfFileviewBinding) :
        RecyclerView.ViewHolder(binding.root)

}