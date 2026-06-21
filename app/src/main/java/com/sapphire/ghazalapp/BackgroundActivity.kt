package com.sapphire.ghazalapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.sapphire.ghazalapp.adapter.ImageViewBgAdapter
import com.sapphire.ghazalapp.databinding.ActivityBackgroundBinding
import com.sapphire.ghazalapp.interFace.ImageInterface
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.github.dhaval2404.colorpicker.model.ColorSwatch

class BackgroundActivity : AppCompatActivity(), ImageInterface {
    private lateinit var binding: ActivityBackgroundBinding
    private lateinit var imageViewBgAdapter: ImageViewBgAdapter
    private var filteredList: ArrayList<Int> = ArrayList()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBackgroundBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomsheetColor.setOnClickListener {
            MaterialColorPickerDialog
                .Builder(this)
                .setTitle("Choose Colour")
                .setColorShape(ColorShape.SQAURE)
                .setColorSwatch(ColorSwatch._300)
                .setColorListener { color, _ ->
                    val intent = Intent().apply {
                        putExtra("editTextValue", color)
                        putExtra("boolean", false) // false means Color
                    }
                    setResult(RESULT_OK, intent)
                    finish()
                }
                .show()
        }

        setupImageGrid()

        binding.back.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupImageGrid() {
        val imageBg = arrayListOf(
            R.drawable.gallary_img, R.drawable.bg1, R.drawable.bg2, R.drawable.bg3,
            R.drawable.bg4, R.drawable.bg5, R.drawable.bg6, R.drawable.bg7,
            R.drawable.bg8, R.drawable.bg9, R.drawable.bg10, R.drawable.bg11,
            R.drawable.bg12, R.drawable.bg13, R.drawable.bg14, R.drawable.bg15,
            R.drawable.bg16, R.drawable.bg17, R.drawable.bg18, R.drawable.bg19,
            R.drawable.bg20, R.drawable.bg21, R.drawable.bg22, R.drawable.bg23,
            R.drawable.bg24, R.drawable.bg25, R.drawable.bg26, R.drawable.bg27,
            R.drawable.bg28, R.drawable.bg29, R.drawable.bg30, R.drawable.bg31,
            R.drawable.bg32, R.drawable.bg33, R.drawable.bg34, R.drawable.bg35,
            R.drawable.bg36, R.drawable.bg37, R.drawable.bg38, R.drawable.bg39,
            R.drawable.bg40, R.drawable.bg41, R.drawable.bg42, R.drawable.bg43,
            R.drawable.bg44, R.drawable.bg45, R.drawable.bg46, R.drawable.bg47,
            R.drawable.bg48, R.drawable.bg49, R.drawable.bg50, R.drawable.bg51,
            R.drawable.bg52, R.drawable.bg53, R.drawable.bg54, R.drawable.bg55,
            R.drawable.bg56, R.drawable.bg57, R.drawable.bg58, R.drawable.bg59,
            R.drawable.bg60, R.drawable.bg61, R.drawable.bg62, R.drawable.bg63,
            R.drawable.bg64, R.drawable.bg65, R.drawable.bg66
        )

        filteredList.clear()
        filteredList.addAll(imageBg)

        binding.bgcolorList.layoutManager = GridLayoutManager(this, 3)
        imageViewBgAdapter = ImageViewBgAdapter(filteredList, this, this)
        binding.bgcolorList.adapter = imageViewBgAdapter
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            val imageUri = data.data
            if (imageUri != null) {
                val intent = Intent().apply {
                    putExtra("galleryimage", imageUri.toString())
                }
                setResult(RESULT_OK, intent)
                finish()
            }
        } else if (resultCode != RESULT_CANCELED) {
            Toast.makeText(this, "Image not selected", Toast.LENGTH_SHORT).show()
        }
    }

    override fun imageBackground(image: Int, boolean: Boolean, position: Int) {
        if (position == 0) {
            val photoPickerIntent = Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
            }
            startActivityForResult(photoPickerIntent, 1)
        } else {
            val intent = Intent().apply {
                putExtra("editTextValue", image)
                putExtra("boolean", boolean) // true means Drawable Resource
            }
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}
