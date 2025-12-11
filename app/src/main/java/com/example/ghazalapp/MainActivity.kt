package com.example.ghazalapp

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.ghazalapp.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.gson.JsonObject

class MainActivity : AppCompatActivity() {
    private var exitDialog: AlertDialog? = null
    lateinit var drawerLayout: DrawerLayout
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    lateinit var navigationView: NavigationView
    lateinit var jsonObject : JsonObject
    lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        FirebaseApp.initializeApp(this);
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navview)

        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {

                R.id.nav_favourites -> {

                    val intent = Intent(this, FavoriteQuoteActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_pdf -> {
                    val intent = Intent(this, PdfFolderActivity::class.java) // Replace with your target activity
                startActivity(intent)
                drawerLayout.closeDrawer(GravityCompat.START) // Close the drawer after selection
                true
                }
                R.id.nav_share -> {
                    // Handle the specific menu item
                val shareIntent = Intent(Intent.ACTION_SEND)

                // This specifies the type of data being shared, in this case, plain text ("text/plain").
                // It tells the receiving application what kind of data it should expect.
                shareIntent.type = "text/plain"

                // This line sets an extra piece of information
                // to be passed with the intent: the subject of the shared content.
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out this awesome app!")

                // Set the text to be shared (the body of the message)
                // (EXTRA_TEXT is the key used to pass the actual content you want to share.)
                shareIntent.putExtra(Intent.EXTRA_TEXT,
                    "Hey! Check out this great app: " +
                            "https://play.google.com/store/apps/details?id=${packageName}" // app package name will be shown
                )

                // This starts the share intent by creating a chooser. The chooser allows the user to pick an app to
                // send the content with (e.g., email, messaging, social media apps, etc.).
                startActivity(Intent.createChooser(shareIntent, "Share via"))
                drawerLayout.closeDrawer(GravityCompat.START)
                true
                }
                R.id.nav_rateus -> {
                    // Handle the specific menu item
                val rateIntent = Intent(
                    Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=${packageName}"))
                    // Start the activity to open the Google Play Store
                startActivity(rateIntent)
                drawerLayout.closeDrawer(GravityCompat.START)
                true
                }
                R.id.nav_privacy -> {
                    true
                }
                R.id.nav_find_app -> {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=Rubab"))
                    startActivity(intent)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                else -> false
            }
        }

        setSupportActionBar(binding.toolbar);
        actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            binding.toolbar,
            R.string.open,
            R.string.close
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        binding.toggle.setOnClickListener {
            toggleDrawer()
        }
        binding.nazamCv.setOnClickListener() {
            val intent = Intent(this, FetchDataCategoryActivity::class.java)
            intent.putExtra("mainObject", "نظمیں")
            startActivity(intent)

        }
        binding.ghzalCv.setOnClickListener() {
            val intent = Intent(this, FetchDataCategoryActivity::class.java)
            intent.putExtra("mainObject", "غزلیں")

            startActivity(intent)

        }
        binding.qatatCv.setOnClickListener() {
            val intent = Intent(this, FetchDataCategoryActivity::class.java)
            intent.putExtra("mainObject", "قطعات")

            startActivity(intent)

        }
        binding.qasaidCv.setOnClickListener() {
            val intent = Intent(this, FetchDataCategoryActivity::class.java)
            intent.putExtra("mainObject", "قصائد")

            startActivity(intent)

        }
        binding.mutafariqCv.setOnClickListener() {
            val intent = Intent(this, FetchDataCategoryActivity::class.java)
            intent.putExtra("mainObject", "متفرق")

            startActivity(intent)

        }
        binding.rabaytCv.setOnClickListener() {
            val intent = Intent(this, FetchDataCategoryActivity::class.java)
            intent.putExtra("mainObject", "رباعیات")

            startActivity(intent)

        }


        // to make the Navigation drawer icon always appear on the action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(null)
    }





    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
        }

    override fun onBackPressed() {
        // Create a custom dialog layout
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_exit, null)

        // Build the dialog
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)

        // Create and show the dialog
        val alertDialog = builder.create()

        // Set background color transparency (if needed)
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Show the dialog
        alertDialog.show()

        // Customize button colors and behavior
        val btnStay = dialogView.findViewById<TextView>(R.id.btnStay)
        val btnExit = dialogView.findViewById<TextView>(R.id.btnExit)

        // Set colors for the buttons
//        btnStay.setTextColor(resources.getColor(R.color.white))  // Custom color for "Stay"
        btnExit.setTextColor(resources.getColor(R.color.white))   // Custom color for "Exit"



        // Set button actions
        btnStay.setOnClickListener {
            alertDialog.dismiss()  // Close the dialog and stay in the app
        }

        btnExit.setOnClickListener {
            super.onBackPressed()  // Close the app
        }

        // Adjust the size of the dialog (optional)
        val width = (resources.displayMetrics.widthPixels * 0.8).toInt()  // Set width to 80% of screen width
        val height = (resources.displayMetrics.heightPixels * 0.5).toInt()  // Set height to 50% of screen height
        alertDialog.window?.setLayout(width, height)
    }
//    private fun showExitDialog() {
//        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_exit, null)
//
//        val builder = AlertDialog.Builder(this, R.style.CustomDialogStyle)
//        builder.setView(dialogView)
//
//        exitDialog = builder.create()
//        exitDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//
//        val btnStay = dialogView.findViewById<TextView>(R.id.btnStay)
//        val btnExit = dialogView.findViewById<TextView>(R.id.btnExit)
//        btnStay.setOnClickListener {
//            exitDialog?.dismiss()
//        }
//
//        btnExit.setOnClickListener {
//            finish()
//        }
//
//        exitDialog?.show()
//    }

    private fun toggleDrawer() {
        if (binding.drawerLayout.isOpen) {
            Log.d(TAG, "toggleDraweropen: " + binding.drawerLayout.isOpen)
            binding.drawerLayout.closeDrawer(GravityCompat.START)

        } else {

            Log.d(TAG, "toggleDrawerclose: " + binding.drawerLayout.isOpen)
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }}



