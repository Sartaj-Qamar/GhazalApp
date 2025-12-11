package com.example.ghazalapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ghazalapp.adapter.ImageViewBgAdapter
import com.example.ghazalapp.databinding.ActivityBackgroundBinding
import com.example.ghazalapp.interFace.BackgroundColorInterface
import com.example.ghazalapp.interFace.ImageInterface
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.github.dhaval2404.colorpicker.model.ColorSwatch

class BackgroundActivity : AppCompatActivity(), ImageInterface {
    lateinit var binding: ActivityBackgroundBinding
    lateinit var imageViewBgAdapter: ImageViewBgAdapter
    private lateinit var filteredList: ArrayList<Int>
    private lateinit var selectedImage: Bitmap

    //    private lateinit var mColor: String
    private lateinit var backgroundColorInterface: BackgroundColorInterface

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBackgroundBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomsheetColor.setOnClickListener() {
            MaterialColorPickerDialog
                .Builder(this)                            // Pass Activity Instance
                .setTitle("Choose Colour")                // Default "Choose Color"
                .setColorShape(ColorShape.SQAURE)    // Default ColorShape.CIRCLE
                .setColorSwatch(ColorSwatch._300)    // Default ColorSwatch._500
//                .setDefaultColor(mDefaultColor) 		// Pass Default Color
                .setColorListener { color, colorHex ->
                    val intent = Intent()
                    intent.putExtra("editTextValue", color)
                    intent.putExtra("boolean", false)
                    setResult(RESULT_OK, intent)
                    finish()
                }
                .show()
        }   // Initialize the filteredList before using it
        filteredList = ArrayList()
        var imageBg: ArrayList<Int> = ArrayList()
        imageBg.add(R.drawable.gallary_img)
        imageBg.add(R.drawable.bg1)
        imageBg.add(R.drawable.bg2)
        imageBg.add(R.drawable.bg3)
        imageBg.add(R.drawable.bg4)
        imageBg.add(R.drawable.bg5)
        imageBg.add(R.drawable.bg6)
        imageBg.add(R.drawable.bg7)
        imageBg.add(R.drawable.bg8)
        imageBg.add(R.drawable.bg9)
        imageBg.add(R.drawable.bg10)
        imageBg.add(R.drawable.bg11)
        imageBg.add(R.drawable.bg12)
        imageBg.add(R.drawable.bg13)
        imageBg.add(R.drawable.bg14)
        imageBg.add(R.drawable.bg15)
        imageBg.add(R.drawable.bg16)
        imageBg.add(R.drawable.bg17)
        imageBg.add(R.drawable.bg18)
        imageBg.add(R.drawable.bg19)
        imageBg.add(R.drawable.bg20)
        imageBg.add(R.drawable.bg21)
        imageBg.add(R.drawable.bg22)
        imageBg.add(R.drawable.bg23)
        imageBg.add(R.drawable.bg24)
        imageBg.add(R.drawable.bg25)
        imageBg.add(R.drawable.bg26)
        imageBg.add(R.drawable.bg27)
        imageBg.add(R.drawable.bg28)
        imageBg.add(R.drawable.bg29)
        imageBg.add(R.drawable.bg30)
        imageBg.add(R.drawable.bg31)
        imageBg.add(R.drawable.bg32)
        imageBg.add(R.drawable.bg33)
        imageBg.add(R.drawable.bg34)
        imageBg.add(R.drawable.bg35)
        imageBg.add(R.drawable.bg36)
        imageBg.add(R.drawable.bg37)
        imageBg.add(R.drawable.bg38)
        imageBg.add(R.drawable.bg39)
        imageBg.add(R.drawable.bg40)
        imageBg.add(R.drawable.bg41)
        imageBg.add(R.drawable.bg42)
        imageBg.add(R.drawable.bg43)
        imageBg.add(R.drawable.bg44)
        imageBg.add(R.drawable.bg45)
        imageBg.add(R.drawable.bg46)
        imageBg.add(R.drawable.bg47)
        imageBg.add(R.drawable.bg48)
        imageBg.add(R.drawable.bg49)
        imageBg.add(R.drawable.bg50)
        imageBg.add(R.drawable.bg51)
        imageBg.add(R.drawable.bg52)
        imageBg.add(R.drawable.bg53)
        imageBg.add(R.drawable.bg54)
        imageBg.add(R.drawable.bg55)
        imageBg.add(R.drawable.bg56)
        imageBg.add(R.drawable.bg57)
        imageBg.add(R.drawable.bg58)
        imageBg.add(R.drawable.bg59)
        imageBg.add(R.drawable.bg60)
        imageBg.add(R.drawable.bg61)
        imageBg.add(R.drawable.bg62)
        imageBg.add(R.drawable.bg63)
        imageBg.add(R.drawable.bg64)
        imageBg.add(R.drawable.bg65)
        imageBg.add(R.drawable.bg66)

        filteredList.addAll(imageBg)

        Log.e("sizehy", "mdd  " + filteredList.size)

        binding.bgcolorList.layoutManager = GridLayoutManager(this, 3)
        imageViewBgAdapter = ImageViewBgAdapter(filteredList, this, this)
        binding.bgcolorList.adapter = imageViewBgAdapter
        imageViewBgAdapter.notifyDataSetChanged()
        binding.back.setOnClickListener() {
            onBackPressed()
        }

    }

    override fun onActivityResult(reqCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(reqCode, resultCode, data)

        if (resultCode == RESULT_OK) {

            val imageUri = data?.data
            val imageStream = contentResolver.openInputStream(imageUri!!)
            selectedImage = BitmapFactory.decodeStream(imageStream)

            Toast.makeText(this, "Image picked", Toast.LENGTH_LONG).show()
            val intent = Intent()
            intent.putExtra("galleryimage", imageUri.toString())
            setResult(RESULT_OK, intent)
            this@BackgroundActivity.finish()


        } else {
            Toast.makeText(this, "You haven't picked an Image", Toast.LENGTH_LONG).show()
        }
    }


    override fun imageBackground(image: Int, boolean: Boolean, position: Int) {
        if (position == 0) {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"

            startActivityForResult(photoPickerIntent, 1)
        } else {
            val intent = Intent()
            intent.putExtra("editTextValue", image)
            intent.putExtra("boolean", boolean)
            setResult(RESULT_OK, intent)
            finish()
        }

    }

//    override fun galleryimage(image: Bitmap) {
//        TODO("Not yet implemented")
//    }


    override fun onBackPressed() {
        super.onBackPressed()
    }

}



