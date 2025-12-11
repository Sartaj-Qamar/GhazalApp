package com.example.ghazalapp

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.ContentResolver
import android.content.ContentValues
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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.drawToBitmap
import codes.side.andcolorpicker.converter.toColorInt
import codes.side.andcolorpicker.model.IntegerHSLColor
import com.example.ghazalapp.R.font.croissant_one
import com.example.ghazalapp.bottomSheet.BottomSheetEditDialogFragment
import com.example.ghazalapp.databinding.ActivityQuoteViewBinding
import com.example.ghazalapp.interFace.BackgroundColorInterface
import com.example.ghazalapp.quotesData.Quote
import io.paperdb.Paper

class QuoteViewActivity : AppCompatActivity(), BackgroundColorInterface {
    lateinit var binding: ActivityQuoteViewBinding
    private var quotes: ArrayList<Quote> = ArrayList()
    private var favList: ArrayList<Quote> = ArrayList()
    private var xCoOrdinate = 0f
    private var yCoOrdinate = 0f
    private lateinit var filteredList: ArrayList<Int>
    var isSelected = false
    //    private var textSize = 20f
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private var scaleFactor = 1f
    //diff is a constant floating-point number (2.0f),
    // which will be used in the application to increase the text size by 2 pixels
//    private val diff: Float = 2.0f

    @SuppressLint("ClickableViewAccessibility", "WrongThread", "ServiceCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuoteViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val quote = intent.getStringExtra("quote")
        val id: Long = intent.getLongExtra("id", 0)
        val author = intent.getStringExtra("author")
        val title = intent.getStringExtra("title")
        binding.titleTv.setText(quote)
        binding.textTitle.text = title
        // Initialize Paper with the context of the activity
binding.titleTv.typeface= ResourcesCompat.getFont(this, croissant_one)
        Paper.init(this)

        // Initialize the ScaleGestureDetector
        scaleGestureDetector = ScaleGestureDetector(this, ScaleListener())
//
        binding.backArrow.setOnClickListener() {
            onBackPressed()
        }
        binding.titleTv.clearFocus()

        val typeface = ResourcesCompat.getFont(this, croissant_one)
        binding.titleTv.typeface = typeface
//        DrawableCompat.setTint(  binding.textbgLayout.background, ContextCompat.getColor(this, R.color.background_color))
        binding.textbgLayout.background.alpha = 10
        binding.textbgLayout.setOnTouchListener { v, event ->
            // Pass the event to the ScaleGestureDetector to detect zoom gestures
            scaleGestureDetector.onTouchEvent(event)
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // Capture initial touch coordinates
                    xCoOrdinate = event.rawX - v.x
                    yCoOrdinate = event.rawY - v.y
                }

                MotionEvent.ACTION_MOVE -> {
                    // Move the TextView while dragging
                    v.animate()
                        .x(event.rawX - xCoOrdinate)
                        .y(event.rawY - yCoOrdinate)
                        .setDuration(0)
                        .start()
                }

                MotionEvent.ACTION_UP -> {
                    // Handle drop action here if needed (you can implement functionality for drop here)
                }

                else -> return@setOnTouchListener false
            }
            return@setOnTouchListener true
        }
        binding.themeLayout.setOnClickListener {
            val intent = Intent(this, BackgroundActivity::class.java)
            Log.d("TAG", "onCreatehyyh: $intent")
            startActivityForResult(intent, 1)
        }

        binding.editIv.setOnClickListener {
            val bottomSheetEditDialogFragment =
                BottomSheetEditDialogFragment(binding.titleTv.textSize)
            bottomSheetEditDialogFragment.show(
                supportFragmentManager,
                bottomSheetEditDialogFragment.tag
            )
        }

        binding.share.setOnClickListener {
            // Capture the bitmap of your view (or scroll view, etc.)
            val bitmap = binding.textScroll.drawToBitmap()

            // Save the bitmap to the gallery or a temporary file
            val imageUri = shareImageToGallery(bitmap)

            // Create an Intent to share the content
            val shareIntent = Intent(Intent.ACTION_SEND)

            // Set the MIME type for the content to be shared (image + text)
            shareIntent.type = "image/*"

            // Add the image URI to the Intent
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)




            // Start the sharing activity
            startActivity(Intent.createChooser(shareIntent, "Share Via"))
        }




        binding.copy.setOnClickListener() {

            // Get system clipboard service
            val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val getstring = ClipData.newPlainText("", binding.titleTv.text.toString())
            clipboard.setPrimaryClip(getstring)
        }

        binding.downloadimg.setOnClickListener {
            // Capture the Bitmap of the view using drawToBitmap()
            binding.titleTv.clearFocus()
            val bitmap = binding.textScroll.drawToBitmap()
            saveImageToGallery(bitmap)
            Toast.makeText(this, "Image saved to gallery", Toast.LENGTH_SHORT).show()
        }
    }
    // Function to save image to gallery and return its URI
    private fun shareImageToGallery(bitmap: Bitmap): Uri? {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "shared_image_${System.currentTimeMillis()}.png")
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES) // Save to Pictures
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

    //The Bitmap is the image that you want to save to the gallery

    private fun saveImageToGallery(bitmap: Bitmap) {
        // Generating a File Name
//ContentValues is a key-value pair object used to insert data
// into a content provider (in this case, the media store).
        val contentValues = ContentValues().apply {
//                put(MediaStore.Images.Media.TITLE, fileName)
//                put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
//                put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
//                put(MediaStore.Images.Media.DATE_MODIFIED, System.currentTimeMillis() / 1000)
        }

        val imageUri =
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        imageUri?.let { uri ->

            contentResolver.openOutputStream(uri)?.use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            }
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            Log.d("TAG", "onActivityResulthyy: $requestCode")
            if (resultCode == RESULT_OK) {
                if (data?.hasExtra("editTextValue")!!) {
                    val imageBg = data.getIntExtra("editTextValue", 0)
//
//                    Log.d("colorhy", "onActivityResult: $color")
                    val colorbgboolean = data.getBooleanExtra("boolean", false)
                    Log.d("color", "onActivityResult: $colorbgboolean")
                    if (colorbgboolean) {

                        binding.textScroll.setBackgroundResource(imageBg)

//
                    } else {
                        Log.d("TAjjGG", "onActivityResult: $imageBg")
                        binding.textScroll.setBackgroundColor(imageBg)


                    }
                } else if (data.hasExtra("galleryimage")) {
                    val image = data.getStringExtra("galleryimage")
                    image?.let { imageString ->
//                        val imageStream = contentResolver.openInputStream(imageUri!!)
                        val imageStream = contentResolver.openInputStream(Uri.parse(imageString))
                       val selectedImage = BitmapFactory.decodeStream(imageStream)
                        binding.textScroll.background=selectedImage.toDrawable(resources)
                    }

                }
            }
        }




     }
    override fun colorBackground(color: IntegerHSLColor, boolean: Boolean) {
        if (boolean) {
            Log.d("colorhy", "colorBackground: $color")
            binding.textbgLayout.setBackgroundColor(color.toColorInt())

        }
    }

    override fun textfont(font: Int) {
        // Load the font using ResourcesCompat
Log.d("TAGgtgtgt", "textfont: $font")
        // Set the font to the TextView
        val typeface = ResourcesCompat.getFont(this, font.toInt())
//        binding.titleTv.typeface = typeface
 binding.titleTv.typeface=typeface
   }

    override fun textsize(size: Float) {

//            textSize -= 2f // Decrease text size by 2 pixels
            binding.titleTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)

    }

    override fun galleryimage(image: Bitmap) {
        Log.d("TAhygG", "galleryimage: $image")
        binding.backArrow.setImageBitmap(image)
    }




    override fun colortextBackground(color: Int) {
            binding.titleTv.setTextColor(color)
    }
    override fun onResume() {
        super.onResume()

    }
    override fun onBackPressed() {
        super.onBackPressed()
    }
    // ScaleGestureDetector listener to detect pinch zoom
     private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {

                detector.let {
                    scaleFactor *= it.scaleFactor
                    // Limit the scale factor to avoid excessive zooming
                    scaleFactor = scaleFactor.coerceIn(0.5f, 5.0f)

                    // Apply the scale to the TextView's text size
                    binding.titleTv.textSize = 16f * scaleFactor // 16f is the base text size, adjust accordingly
                }
                return true
            }

        }
    }



