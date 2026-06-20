package com.example.ghazalapp

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.drawToBitmap
import codes.side.andcolorpicker.converter.toColorInt
import codes.side.andcolorpicker.model.IntegerHSLColor
import com.example.ghazalapp.R
import com.example.ghazalapp.bottomSheet.BottomSheetEditDialogFragment
import com.example.ghazalapp.databinding.ActivityQuoteViewBinding
import com.example.ghazalapp.interFace.BackgroundColorInterface
import com.example.ghazalapp.quotesData.Quote
import io.paperdb.Paper

class QuoteViewActivity : AppCompatActivity(), BackgroundColorInterface {
    lateinit var binding: ActivityQuoteViewBinding
    private var xCoOrdinate = 0f
    private var yCoOrdinate = 0f
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private var scaleFactor = 1f

    @SuppressLint("ClickableViewAccessibility", "ServiceCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_FULLSCREEN
        )
        
        binding = ActivityQuoteViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val quote = intent.getStringExtra("quote") ?: ""
        val title = intent.getStringExtra("title") ?: ""
        
        // Fix: Use setText() instead of .text = for EditText to avoid type mismatch with Editable
        binding.titleTv.setText(quote)
        binding.textTitle.text = title
        
        Paper.init(this)
        scaleGestureDetector = ScaleGestureDetector(this, ScaleListener())

        val typeface = ResourcesCompat.getFont(this, R.font.croissant_one)
        binding.titleTv.typeface = typeface

        binding.backArrow.setOnClickListener {
            onBackPressed()
        }

        binding.textbgLayout.background?.alpha = 10
        binding.textbgLayout.setOnTouchListener { v, event ->
            scaleGestureDetector.onTouchEvent(event)
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    xCoOrdinate = event.rawX - v.x
                    yCoOrdinate = event.rawY - v.y
                }
                MotionEvent.ACTION_MOVE -> {
                    v.animate()
                        .x(event.rawX - xCoOrdinate)
                        .y(event.rawY - yCoOrdinate)
                        .setDuration(0)
                        .start()
                }
            }
            true
        }

        binding.themeLayout.setOnClickListener {
            val intent = Intent(this, BackgroundActivity::class.java)
            startActivityForResult(intent, 1)
        }

        binding.editIv.setOnClickListener {
            val bottomSheetEditDialogFragment = BottomSheetEditDialogFragment(binding.titleTv.textSize)
            bottomSheetEditDialogFragment.show(supportFragmentManager, "EditDialog")
        }

        binding.share.setOnClickListener {
            try {
                hideKeyboard()
                val bitmap = binding.textScroll.drawToBitmap()
                val imageUri = shareImageToGallery(bitmap)
                if (imageUri != null) {
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "image/*"
                        putExtra(Intent.EXTRA_STREAM, imageUri)
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    startActivity(Intent.createChooser(shareIntent, "Share Via"))
                }
            } catch (e: Exception) {
                Toast.makeText(this, "Error sharing image", Toast.LENGTH_SHORT).show()
            }
        }

        binding.copy.setOnClickListener {
            val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Quote", binding.titleTv.text.toString())
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "Text Copied", Toast.LENGTH_SHORT).show()
        }

        binding.downloadimg.setOnClickListener {
            try {
                hideKeyboard()
                binding.titleTv.clearFocus()
                val bitmap = binding.textScroll.drawToBitmap()
                saveImageToGallery(bitmap)
                Toast.makeText(this, "Image saved to gallery", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this, "Error saving image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm?.hideSoftInputFromWindow(binding.titleTv.windowToken, 0)
    }

    private fun shareImageToGallery(bitmap: Bitmap): Uri? {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "shared_image_${System.currentTimeMillis()}.png")
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }

        val contentResolver: ContentResolver = applicationContext.contentResolver
        val imageUri: Uri? = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        imageUri?.let { uri ->
            contentResolver.openOutputStream(uri)?.use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            }
        }
        return imageUri
    }

    private fun saveImageToGallery(bitmap: Bitmap) {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "quote_${System.currentTimeMillis()}.png")
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }

        val imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        imageUri?.let { uri ->
            contentResolver.openOutputStream(uri)?.use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            if (data.hasExtra("editTextValue")) {
                val imageBg = data.getIntExtra("editTextValue", 0)
                val isResource = data.getBooleanExtra("boolean", false)
                if (isResource) {
                    binding.textScroll.setBackgroundResource(imageBg)
                } else {
                    binding.textScroll.setBackgroundColor(imageBg)
                }
            } else if (data.hasExtra("galleryimage")) {
                val imageString = data.getStringExtra("galleryimage")
                imageString?.let {
                    try {
                        val uri = Uri.parse(it)
                        contentResolver.openInputStream(uri)?.use { stream ->
                            val selectedImage = BitmapFactory.decodeStream(stream)
                            binding.textScroll.background = selectedImage.toDrawable(resources)
                        }
                    } catch (e: Exception) {
                        Log.e("QuoteView", "Error loading gallery image", e)
                    }
                }
            }
        }
    }

    override fun colorBackground(color: IntegerHSLColor, boolean: Boolean) {
        if (boolean) {
            binding.textbgLayout.setBackgroundColor(color.toColorInt())
        }
    }

    override fun textfont(font: Int) {
        try {
            val typeface = ResourcesCompat.getFont(this, font)
            binding.titleTv.typeface = typeface
        } catch (e: Exception) {
            Log.e("QuoteView", "Error setting font", e)
        }
    }

    override fun textsize(size: Float) {
        binding.titleTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
    }

    override fun galleryimage(image: Bitmap) {
        binding.backArrow.setImageBitmap(image)
    }

    override fun colortextBackground(color: Int) {
        binding.titleTv.setTextColor(color)
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scaleFactor *= detector.scaleFactor
            scaleFactor = scaleFactor.coerceIn(0.5f, 5.0f)
            binding.titleTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f * scaleFactor)
            return true
        }
    }
}
