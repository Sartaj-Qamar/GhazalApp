package com.example.ghazalapp

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ghazalapp.adapter.TittleQuoteAdapterr
import com.example.ghazalapp.databinding.ActivityDetailDataGhazalBinding
import com.example.ghazalapp.quotesData.Quote
import com.itextpdf.text.Document
import com.itextpdf.text.DocumentException
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import io.paperdb.Paper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import kotlin.random.Random

class DetailDataGhazalActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailDataGhazalBinding
    lateinit var quoteAdapterr: TittleQuoteAdapterr
    private var quotes: ArrayList<Quote> = ArrayList()
    private var addfavList: ArrayList<Quote> = ArrayList()
    private var removefavList: ArrayList<Quote> = ArrayList()
    var quote: String = ""
    var id: String = ""
    var author: String = ""
    var titlequote: String = ""
    var jsonString: String = ""
    var jsonkeyString: String = ""
    var isFavorite = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailDataGhazalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.detailghzallist.layoutManager = LinearLayoutManager(this)
        Paper.init(this)

        val filejsonString = readFile("jsonformatter.json")


        if (intent.hasExtra("mainObject")) {
            jsonString = intent.getStringExtra("mainObject").toString()
        }
        if (intent.hasExtra("keyObject")) {
            jsonkeyString = intent.getStringExtra("keyObject").toString()
        }

        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch() {
            val job = launch {


                fetchdata(filejsonString, jsonString)
            }
            job.invokeOnCompletion {
                launch {
                    CompleteCallBack()
                }
            }

        }
        binding.detailghzallist.setOnClickListener() {
            val intent = Intent(this, QuoteViewActivity::class.java)
            startActivity(intent)
        }
        binding.share.setOnClickListener {
            val shareText = Intent(Intent.ACTION_SEND)
            shareText.type = "text/plain"
            val dataToShare = quote
            shareText.putExtra(Intent.EXTRA_TEXT, dataToShare)
            startActivity(Intent.createChooser(shareText, "Share Via"))
        }
        binding.copy.setOnClickListener() {

            // Get system clipboard service
            val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val getstring = ClipData.newPlainText("", quote)
            clipboard.setPrimaryClip(getstring)
            Toast.makeText(this, "Text Copied", Toast.LENGTH_SHORT).show()
        }
        binding.backArrow.setOnClickListener() {
            onBackPressed()
        }
        binding.pdfIv.setOnClickListener {
//            val file = File(this.getExternalFilesDir(null), "/PdfFiles/Abc.pdf")
             val destPath = File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "").absolutePath + "/" + "${Random.nextInt(0, 9 + 1).toString()+jsonkeyString}.pdf"
            createPdf(quote,destPath)

            Toast.makeText(this, "PDF created successfully", Toast.LENGTH_SHORT).show()
            // Share the created PDF file

        }
        binding.favourite.setOnClickListener {
            // New Quote that you want to add
            val newQuote = Quote(id, quote, author, titlequote)

            // Check if the new quote already exists based on 'id'
            if (!addfavList.any { it.id == id }){
                addfavList.add(newQuote)

                binding.favouriteBottom.setColorFilter(ContextCompat.getColor(this, R.color.yellow))
                binding.favouriteVisible.visibility = View.VISIBLE
                binding.favouriteVisible.setColorFilter(ContextCompat.getColor(this, R.color.yellow))
                Toast.makeText(this, "Added to Favorite", Toast.LENGTH_SHORT).show()

            }else{
                addfavList.remove(newQuote)
                binding.favouriteBottom.setColorFilter(ContextCompat.getColor(this, R.color.white))
                binding.favouriteVisible.visibility = View.GONE
                binding.favouriteVisible.setColorFilter(ContextCompat.getColor(this, R.color.white))
                Toast.makeText(this, "Removed from Favorite", Toast.LENGTH_SHORT).show()
            }
            Paper.book().write("favorite", addfavList)

        }
        binding.detailghzallist.layoutManager = LinearLayoutManager(this)

    }

    @SuppressLint("NotifyDataSetChanged")
    suspend fun CompleteCallBack() {
        withContext(Dispatchers.Main) {
            quoteAdapterr = TittleQuoteAdapterr(
                quotes,
                this@DetailDataGhazalActivity, jsonString,
            ) // Pass the data to your adapter
            binding.detailghzallist.adapter = quoteAdapterr
            quoteAdapterr.notifyDataSetChanged()
        }
    }


    //first you need to read the json file from assets
    @Throws(IOException::class)
    fun readFile(fileName: String): String {
        val reader: BufferedReader =
            BufferedReader(InputStreamReader(assets.open(fileName), "UTF-8"))
        val content = StringBuilder()
        var line: String?

        while (reader.readLine().also { line = it } != null) {
            content.append(line)
        }

        return content.toString()
    }

    fun fetchdata(filetext: String, type: String) {
        Log.d("tag22", type)
        try {
            val obj = JSONObject(filetext)
            val mquotes = obj.getJSONObject(type)
            val quoteObject = mquotes.getJSONObject(jsonkeyString)
            quote = quoteObject.getString("quote")
            titlequote = quoteObject.getString("title")
            binding.toolbarText.text = titlequote

            author = quoteObject.getString("author")
            id = quoteObject.getString("id")
            quotes.add(Quote(id, quote, author, titlequote))
            val favList: List<Quote> = Paper.book().read("favorite") ?: emptyList()
            addfavList.addAll(favList)
            if (addfavList.any { it.id == id }) {
                binding.favouriteBottom.setColorFilter(ContextCompat.getColor(this, R.color.yellow))
                binding.favouriteVisible.visibility = View.VISIBLE
                binding.favouriteVisible.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        R.color.yellow
                    )
                )

            } else {
                binding.favouriteBottom.setColorFilter(ContextCompat.getColor(this, R.color.white))
                binding.favouriteVisible.visibility = View.GONE
                binding.favouriteVisible.setColorFilter(ContextCompat.getColor(this, R.color.white))

            }

        } catch (e: Exception) {

            if (intent.hasExtra("quote")) {
                quote = intent.getStringExtra("quote").toString()
            }
            if (intent.hasExtra("author")) {
                author = intent.getStringExtra("author").toString()
            }
            if (intent.hasExtra("title")) {
                titlequote = intent.getStringExtra("title").toString()
            }
            if (intent.hasExtra("id")) {
                id = intent.getStringExtra("id").toString()
            }
            binding.toolbarText.text = titlequote
            quotes.add(Quote(id, quote, author, titlequote))
            e.printStackTrace()

            quotes.add(Quote(id, quote, author, titlequote))
            val favList: List<Quote> = Paper.book().read("favorite") ?: emptyList()
            addfavList.addAll(favList)
            if (addfavList.any { it.id == id }) {
                binding.favouriteBottom.setColorFilter(ContextCompat.getColor(this, R.color.yellow))
                binding.favouriteVisible.visibility = View.VISIBLE
                binding.favouriteVisible.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        R.color.yellow
                    )
                )

            } else {
                binding.favouriteBottom.setColorFilter(ContextCompat.getColor(this, R.color.white))
                binding.favouriteVisible.visibility = View.GONE
                binding.favouriteVisible.setColorFilter(ContextCompat.getColor(this, R.color.white))

            }
        }
    }
    fun createPdf(text: String, mDestFile: String) {
        val doc = Document()
        val outputfile: File?
        try {
            try {
                outputfile = File(mDestFile)
                val fOut = FileOutputStream(outputfile)
                PdfWriter.getInstance(doc, fOut)
                doc.open()
                val p1 = Paragraph(quote)
                p1.alignment = Paragraph.ALIGN_CENTER
                doc.add(p1)
            } catch (e: OutOfMemoryError) {
                e.printStackTrace()
            }
        } catch (de: DocumentException) {
            Log.e("PDFCreator", "DocumentException:$de")
        } catch (e: IOException) {
            Log.e("PDFCreator", "ioException:$e")
        } finally {
            try {
                doc.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            Log.e("PDFCreator", "ioException:$")
        }
    }
        override fun onBackPressed() {
            super.onBackPressed()
        }


    }



