package com.example.ghazalapp

import android.graphics.Typeface
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ghazalapp.adapter.PdfAdapterr
import com.example.ghazalapp.databinding.ActivityPdfFolderBinding
import com.example.ghazalapp.quotesData.PdfModel
import java.io.File
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class PdfFolderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPdfFolderBinding
    private var pdfFilesArraylist: ArrayList<PdfModel> = ArrayList()
    private lateinit var pdfAdapterr: PdfAdapterr
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Hide status bar and make fullscreen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_FULLSCREEN
        )
        
        binding = ActivityPdfFolderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Apply Urdu Font to the "No PDF" message
        try {
            val typeface = ResourcesCompat.getFont(this, R.font.open_sans_medium)
            binding.noPdfTv.typeface = typeface
        } catch (e: Exception) {
            // Fallback to default if font not found
        }
        
        // Set up back arrow click listener
        binding.backArrow.setOnClickListener {
            onBackPressed()
        }
        
        getConvertedFiles()
    }

    fun getConvertedFiles() {
        pdfFilesArraylist.clear()
        val filePath = File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "").absolutePath + "/"
        val mfile = File(filePath)

        val listFile = mfile.listFiles()

        if (listFile != null) {
            for (file in listFile) {
                if (file.isDirectory) {
                    getConvertedFilesFromDir(file)
                } else {
                    addPdfFile(file)
                }
            }
        }

        updateEmptyView()

        binding.detailTittleList.layoutManager = LinearLayoutManager(this)
        pdfAdapterr = PdfAdapterr(pdfFilesArraylist, this) {
            updateEmptyView()
        }
        binding.detailTittleList.adapter = pdfAdapterr
    }

    private fun updateEmptyView() {
        if (pdfFilesArraylist.isEmpty()) {
            binding.noPdfTv.visibility = View.VISIBLE
            binding.detailTittleList.visibility = View.GONE
        } else {
            binding.noPdfTv.visibility = View.GONE
            binding.detailTittleList.visibility = View.VISIBLE
        }
    }

    private fun getConvertedFilesFromDir(directory: File) {
        val listFile = directory.listFiles()
        if (listFile != null) {
            for (file in listFile) {
                if (file.isDirectory) {
                    getConvertedFilesFromDir(file)
                } else {
                    addPdfFile(file)
                }
            }
        }
    }

    private fun addPdfFile(file: File) {
        if (file.name.endsWith(".pdf", ignoreCase = true)) {
            val path1 = file.absolutePath
            val name = file.name
            val date = file.lastModified()
            val size = file.length()

            val sizee = getStringSizeLengthFile(size)
            val datee = getDate(date)

            val modelClass = PdfModel(path1, name, datee, sizee)
            pdfFilesArraylist.add(modelClass)
        }
    }

    fun getStringSizeLengthFile(size: Long): String {
        val df = DecimalFormat("0.00")
        val sizeKb = 1024.0f
        val sizeMb = sizeKb * sizeKb
        val sizeGb = sizeMb * sizeKb
        val sizeTerra = sizeGb * sizeKb
        if (size < sizeMb) return df.format((size / sizeKb).toDouble()) + " Kb" else if (size < sizeGb) return df.format(
            (size / sizeMb).toDouble()
        ) + " Mb" else if (size < sizeTerra) return df.format((size / sizeGb).toDouble()) + " Gb"
        return ""
    }

    fun getDate(dateee: Long): String {
        val dateFormat = "dd-MM-yyyy"
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = dateee
        return formatter.format(calendar.time)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
