package com.example.ghazalapp.bottomSheet


import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import codes.side.andcolorpicker.group.PickerGroup
import codes.side.andcolorpicker.group.registerPickers
import codes.side.andcolorpicker.hsl.HSLColorPickerSeekBar
import codes.side.andcolorpicker.model.IntegerHSLColor
import codes.side.andcolorpicker.view.picker.ColorSeekBar
import codes.side.andcolorpicker.view.picker.OnIntegerHSLColorPickListener
import com.example.ghazalapp.R
import com.example.ghazalapp.adapter.FontchangeAdapter
import com.example.ghazalapp.databinding.FragmentBottomSheetEditDialogListDialogBinding
import com.example.ghazalapp.interFace.BackgroundColorInterface
import com.example.ghazalapp.quotesData.AppFont
import com.github.antonpopoff.colorwheel.gradientseekbar.setAlphaChangeListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetEditDialogFragment(var textsize: Float): BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetEditDialogListDialogBinding? = null
    private lateinit var backgroundColorInterface: BackgroundColorInterface
    private val diff: Float = 1.0f
    var isSelected=true
    private var selectedPosition: Int = -1
    private lateinit var fontchangeAdapter: FontchangeAdapter
    private val binding get() = _binding!!
    private lateinit var filteredList: ArrayList<AppFont>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        _binding =
            FragmentBottomSheetEditDialogListDialogBinding.inflate(inflater, container, false)
        return binding.root


    }

    @SuppressLint("CutPasteId", "Range", "SuspiciousIndentation", "ResourceType",
        "NotifyDataSetChanged", "SetTextI18n"
    )
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        backgroundColorInterface = requireActivity() as BackgroundColorInterface
        binding.numberDisplay.text = (textsize.toInt()).toString()

        binding.colorWheel.colorChangeListener = { rgb: Int ->
            backgroundColorInterface.colortextBackground(rgb)

        }


        binding.gradientSeekBar.colorChangeListener = { offset: Float, argb: Int ->

            backgroundColorInterface.colortextBackground(argb)

            val endColor = Color.argb(0, 0, 0, 100)

            binding.gradientSeekBar.startColor = argb
            binding.gradientSeekBar.endColor = endColor
            binding.gradientSeekBar.setColors(argb, endColor)
        }



        binding.gradientSeekBar.setAlphaChangeListener { offset: Float, color: Int, alpha: Int ->
            backgroundColorInterface.colortextBackground(color)

        }
        binding.hueSeekBar.coloringMode =
            HSLColorPickerSeekBar.ColoringMode.PURE_COLOR // ColoringMode.OUTPUT_COLOR
        // Group pickers with PickerGroup to automatically synchronize color across them
        val group = PickerGroup<IntegerHSLColor>().apply {
            registerPickers(
                binding.hueSeekBar,
                binding.alphaSeekBar

            )
            binding.alphaSeekBar.progress = 60
        }

        group.addListener(
            object : OnIntegerHSLColorPickListener() {
                override fun onColorChanged(
                    picker: ColorSeekBar<IntegerHSLColor>,
                    color: IntegerHSLColor,
                    value: Int,
                ) {
                    Log.d(
                        TAG,
                        "$color picked"
                    )
                    backgroundColorInterface.colorBackground(color, true)
//                    backgroundColorInterface.textbg(color)
                }
            }

        )


        binding.fontlist.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        filteredList = ArrayList()
        var fontchangelist: ArrayList<AppFont> = ArrayList()
        fontchangelist.add(AppFont(R.font.alqalam, "ال قلم تاج نستعلیق"))
        fontchangelist.add(AppFont(R.font.nabla, " نبلا"))
        fontchangelist.add(AppFont(R.font.nafees, "نفیس"))
        fontchangelist.add(AppFont(R.font.noto_nastaliq, "نوٹو نسخہ اردو"))
        fontchangelist.add(AppFont(R.font.almarai, "المراعی"))
        fontchangelist.add(AppFont(R.font.rakkas, " رقاص"))
        fontchangelist.add(AppFont(R.font.qahiri, "قاہری"))
        fontchangelist.add(AppFont(R.font.fustat, "فسطاط"))
        fontchangelist.add(AppFont(R.font.el_messiri, "المسیری"))
        fontchangelist.add(AppFont(R.font.arabic_typesetting, "عربی ٹائپ سیٹنگ"))
        fontchangelist.add(AppFont(R.font.urdu_type, "اردو ٹائپ سیٹنگ"))
        fontchangelist.add(AppFont(R.font.scheherazade_new, "شہرازاد نیو"))
        fontchangelist.add(AppFont(R.font.times_new_roman, "ٹائمز نیو رومن"))
            filteredList.addAll(fontchangelist)

        fontchangeAdapter = FontchangeAdapter(filteredList,requireContext(),backgroundColorInterface)

        binding.fontlist.adapter = fontchangeAdapter
//        fontchangeAdapter.notifyDataSetChanged()


        binding.increaseBtn.setOnClickListener() {
//.textSize retrieves the current size of the text in the TextView (in pixels).

            //diff seems to be a predefined value (possibly a constant or variable) that determines
            // how much the text size should be increased each time the button is clicked.
            textsize += diff
            binding.numberDisplay.text = (textsize.toInt()).toString()
            backgroundColorInterface.textsize(textsize)
//
        }







        binding.decreaseBtn.setOnClickListener() {


            textsize -= diff
            binding.numberDisplay.text = (textsize.toInt()).toString()
//            binding.titleTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)   var textSize = binding.titleTv.textSize
  backgroundColorInterface.textsize(textsize)



        }
binding.crossBtn.setOnClickListener(){
    dismiss()
}

        binding.textbgLayout.setOnClickListener() {

            visibilityFun(binding.backgroundLayout,binding.textBackground)

        }

        binding.textsizeLayout.setOnClickListener() {

            visibilityFun(binding.cardTextSize,binding.sizeBg)

        }

        binding.textcolorLayout.setOnClickListener() {
            visibilityFun(binding.textcolordialogChange,binding.colorBg)
        }


        binding.textfontLayout.setOnClickListener() {
            visibilityFun(binding.textfontRlayout,binding.fontBg)
        }


        }
fun visibilityFun(view1: View,view2: View){
    binding.textfontRlayout.visibility = View.GONE
    binding.fontBg.visibility=View.GONE
    binding.cardTextSize.visibility = View.GONE
    binding.textBackground.visibility = View.GONE
    binding.textcolordialogChange.visibility = View.GONE
    binding.colorBg.visibility=View.GONE
    binding.sizeBg.visibility=View.GONE
    binding.backgroundLayout.visibility= View.GONE
    view1.visibility= View.VISIBLE
    view2.visibility= View.VISIBLE

}


    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            backgroundColorInterface = activity as BackgroundColorInterface
        } catch (e: ClassCastException) {
            Log.e(TAG, "onAttach: ClassCastException: ${e.message}")
        }
    }
}
