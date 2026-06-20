package com.example.ghazalapp

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ghazalapp.adapter.TittleQuoteAdapterr
import com.example.ghazalapp.databinding.ActivityDetailDataGhazalBinding
import com.example.ghazalapp.quotesData.Quote
import io.paperdb.Paper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream

class DetailDataGhazalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailDataGhazalBinding
    private lateinit var quoteAdapterr: TittleQuoteAdapterr
    private var quotes: ArrayList<Quote> = ArrayList()
    private var addfavList: ArrayList<Quote> = ArrayList()
    
    private var currentQuoteText: String = ""
    private var currentId: String = ""
    private var currentAuthor: String = ""
    private var currentTitle: String = ""
    
    private var jsonCategory: String = ""
    private var jsonKey: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN)
        
        binding = ActivityDetailDataGhazalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        binding.detailghzallist.layoutManager = LinearLayoutManager(this)
        Paper.init(this)

        jsonCategory = intent.getStringExtra("mainObject") ?: ""
        jsonKey = intent.getStringExtra("keyObject") ?: ""

        lifecycleScope.launch(Dispatchers.IO) {
            val fileContent = try {
                assets.open("jsonformatter.json").bufferedReader().use { it.readText() }
            } catch (e: Exception) { "" }
            
            fetchData(fileContent)
            withContext(Dispatchers.Main) {
                setupAdapter()
            }
        }

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.share.setOnClickListener {
            if (currentQuoteText.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, currentQuoteText)
                }
                startActivity(Intent.createChooser(intent, "Share Via"))
            }
        }

        binding.copy.setOnClickListener {
            if (currentQuoteText.isNotEmpty()) {
                val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                clipboard.setPrimaryClip(ClipData.newPlainText("Quote", currentQuoteText))
                Toast.makeText(this, "کاپی کر لیا گیا", Toast.LENGTH_SHORT).show()
            }
        }

        binding.backArrow.setOnClickListener { onBackPressed() }

        binding.pdfIv.setOnClickListener {
            if (currentTitle.isNotEmpty()) {
                val sanitizedTitle = currentTitle.replace(Regex("[/\\\\:*?\"<>|]"), "_")
                val folder = File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), sanitizedTitle)
                if (!folder.exists()) folder.mkdirs()
                
                val file = File(folder, "${sanitizedTitle}_${System.currentTimeMillis()}.pdf")
                generateUrduPdf(currentQuoteText, file.absolutePath)
            }
        }

        binding.favourite.setOnClickListener {
            val q = Quote(currentId, currentQuoteText, currentAuthor, currentTitle)
            if (!addfavList.any { it.id == currentId }) {
                addfavList.add(q)
                updateFavUI(true)
                Toast.makeText(this, "پسندیدہ میں شامل کر لیا گیا", Toast.LENGTH_SHORT).show()
            } else {
                addfavList.removeAll { it.id == currentId }
                updateFavUI(false)
                Toast.makeText(this, "پسندیدہ سے نکال دیا گیا", Toast.LENGTH_SHORT).show()
            }
            Paper.book().write("favorite", addfavList)
        }
    }

    private fun updateFavUI(isFav: Boolean) {
        val color = if (isFav) R.color.yellow else R.color.white
        binding.favouriteBottom.setColorFilter(ContextCompat.getColor(this, color))
        binding.favouriteVisible.visibility = if (isFav) View.VISIBLE else View.GONE
        binding.favouriteVisible.setColorFilter(ContextCompat.getColor(this, color))
    }

    private fun setupAdapter() {
        quoteAdapterr = TittleQuoteAdapterr(quotes, this, jsonCategory)
        binding.detailghzallist.adapter = quoteAdapterr
    }

    private fun fetchData(json: String) {
        if (json.isEmpty()) return
        try {
            val root = JSONObject(json)
            val category = root.getJSONObject(jsonCategory)
            val data = category.getJSONObject(jsonKey)
            
            currentQuoteText = data.optString("quote", "")
            currentTitle = data.optString("title", "")
            currentAuthor = data.optString("author", "")
            currentId = data.optString("id", "")

            runOnUiThread { binding.toolbarText.text = currentTitle }
            
            quotes.clear()
            quotes.add(Quote(currentId, currentQuoteText, currentAuthor, currentTitle))
            
            val favorites: List<Quote> = Paper.book().read("favorite") ?: emptyList()
            addfavList.clear()
            addfavList.addAll(favorites)
            
            runOnUiThread { updateFavUI(addfavList.any { it.id == currentId }) }
        } catch (e: Exception) {
            currentQuoteText = intent.getStringExtra("quote") ?: ""
            currentAuthor = intent.getStringExtra("author") ?: ""
            currentTitle = intent.getStringExtra("title") ?: ""
            currentId = intent.getStringExtra("id") ?: ""
            
            runOnUiThread { binding.toolbarText.text = currentTitle }
            quotes.clear()
            quotes.add(Quote(currentId, currentQuoteText, currentAuthor, currentTitle))
        }
    }

    private fun generateUrduPdf(content: String, path: String) {
        val pdfDocument = PdfDocument()
        val paint = TextPaint().apply {
            color = Color.BLACK
            textSize = 16f
            typeface = try {
                ResourcesCompat.getFont(this@DetailDataGhazalActivity, R.font.urdu_type)
            } catch (e: Exception) { Typeface.DEFAULT }
        }

        val pageWidth = 595
        val pageHeight = 842
        val margin = 50f
        val width = pageWidth - (margin * 2).toInt()

        val titlePaint = TextPaint(paint).apply {
            textSize = 24f
            isFakeBoldText = true
        }

        val titleLayout = StaticLayout.Builder.obtain(currentTitle, 0, currentTitle.length, titlePaint, width)
            .setAlignment(Layout.Alignment.ALIGN_CENTER).build()

        val contentLayout = StaticLayout.Builder.obtain(content, 0, content.length, paint, width)
            .setAlignment(Layout.Alignment.ALIGN_OPPOSITE).setLineSpacing(0f, 1.4f).build()

        var currentLine = 0
        var pageNumber = 1
        val totalLines = contentLayout.lineCount
        val footerPaint = TextPaint().apply {
            textSize = 10f
            color = Color.LTGRAY
            textAlign = Paint.Align.CENTER
        }

        while (currentLine < totalLines) {
            val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
            val page = pdfDocument.startPage(pageInfo)
            val canvas = page.canvas
            
            var yOffset = margin
            
            if (pageNumber == 1) {
                canvas.save()
                canvas.translate(margin, yOffset)
                titleLayout.draw(canvas)
                canvas.restore()
                yOffset += titleLayout.height + 30f
            }
            
            val remainingHeight = pageHeight - margin - yOffset - 40f
            var linesOnThisPage = 0
            var heightOnThisPage = 0f
            
            while (currentLine + linesOnThisPage < totalLines) {
                val lineHeight = contentLayout.getLineBottom(currentLine + linesOnThisPage) - 
                                 contentLayout.getLineTop(currentLine + linesOnThisPage)
                if (heightOnThisPage + lineHeight > remainingHeight) break
                heightOnThisPage += lineHeight
                linesOnThisPage++
            }
            
            if (linesOnThisPage > 0) {
                canvas.save()
                canvas.translate(margin, yOffset)
                val topOffset = contentLayout.getLineTop(currentLine).toFloat()
                canvas.clipRect(0f, 0f, width.toFloat(), heightOnThisPage + 5f)
                canvas.translate(0f, -topOffset)
                contentLayout.draw(canvas)
                canvas.restore()
                currentLine += linesOnThisPage
            }

            canvas.drawText("- $pageNumber -", (pageWidth / 2f), pageHeight - 30f, footerPaint)
            pdfDocument.finishPage(page)
            pageNumber++
        }

        try {
            pdfDocument.writeTo(FileOutputStream(File(path)))
            Toast.makeText(this, "پی ڈی ایف محفوظ ہو گئی", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "محفوظ کرنے میں خرابی", Toast.LENGTH_SHORT).show()
        } finally {
            pdfDocument.close()
        }
    }
}
