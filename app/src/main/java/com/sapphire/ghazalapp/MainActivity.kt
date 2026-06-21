package com.sapphire.ghazalapp

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.sapphire.ghazalapp.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import androidx.core.graphics.drawable.toDrawable

class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var navigationView: NavigationView
    private lateinit var binding: ActivityMainBinding

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
        
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = binding.drawerLayout
        navigationView = binding.navview

        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_favourites -> {
                    startActivity(Intent(this, FavoriteQuoteActivity::class.java))
                    true
                }
                R.id.nav_pdf -> {
                    startActivity(Intent(this, PdfFolderActivity::class.java))
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_share -> {
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_SUBJECT, "Check out this awesome app!")
                        putExtra(Intent.EXTRA_TEXT, "Hey! Check out this great app: https://play.google.com/store/apps/details?id=${packageName}")
                    }
                    startActivity(Intent.createChooser(shareIntent, "Share via"))
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_rateus -> {
                    val rateIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=${packageName}"))
                    startActivity(rateIntent)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_find_app -> {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=Rubab")))
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                else -> false
            }
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.open,
            R.string.close
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        
        binding.toggle.setOnClickListener {
            toggleDrawer()
        }
        
        binding.nazamCv.setOnClickListener { startCategory("نظمیں") }
        binding.ghzalCv.setOnClickListener { startCategory("غزلیں") }
        binding.qatatCv.setOnClickListener { startCategory("قطعات") }
        binding.qasaidCv.setOnClickListener { startCategory("قصائد") }
        binding.mutafariqCv.setOnClickListener { startCategory("متفرق") }
        binding.rabaytCv.setOnClickListener { startCategory("رباعیات") }
    }

    private fun startCategory(name: String) {
        val intent = Intent(this, FetchDataCategoryActivity::class.java)
        intent.putExtra("mainObject", name)
        startActivity(intent)
    }

    private fun toggleDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
            return
        }
        
        // Show Exit Dialog and DO NOT call super.onBackPressed() here
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_exit, null)
        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false) // User must click a button
            .create()
        
        alertDialog.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        alertDialog.show()

        dialogView.findViewById<TextView>(R.id.btnStay).setOnClickListener { 
            alertDialog.dismiss() 
        }
        dialogView.findViewById<TextView>(R.id.btnExit).setOnClickListener {
            alertDialog.dismiss()
            finishAffinity() // Cleanly close all activities and exit
        }

        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        alertDialog.window?.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
    }
}
