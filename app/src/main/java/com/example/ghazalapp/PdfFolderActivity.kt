package com.example.ghazalapp

import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
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
private lateinit var tittleQuoteAdapterr: PdfAdapterr
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfFolderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getConvertedFiles()
    }

    fun getConvertedFiles() {
        val filePath = File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "").absolutePath + "/"
val mfile = File(filePath)


        val listFile = mfile.listFiles()

        for (file in listFile!!) {
            if (file.isDirectory) {
                getConvertedFiles()
            } else {
                val path1 = file.absolutePath
                val name = file.name
                val date = file.lastModified()
                val size = file.length()

                val sizee = getStringSizeLengthFile(size)

                val datee = getDate(date)

                val modelClass = PdfModel(path1, name, sizee, datee)

                pdfFilesArraylist.add(modelClass)
//                binding.detailTittleList.layoutManager = GridLayoutManager(this, 3)
                binding.detailTittleList.layoutManager = LinearLayoutManager(this)
                tittleQuoteAdapterr = PdfAdapterr(pdfFilesArraylist, this)
                binding.detailTittleList.adapter = tittleQuoteAdapterr

                binding.backArrow.setOnClickListener() {
                    onBackPressed()
                }
            }
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
        // Create a DateFormatter object for displaying date in specified format.
        val dateFormat = "dd-MM-yyyy"
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = dateee
        return formatter.format(calendar.time)
    }
    override fun onBackPressed() {
        super.onBackPressed()
    }

}
