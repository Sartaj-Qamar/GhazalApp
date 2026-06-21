package com.sapphire.ghazalapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.sapphire.ghazalapp.databinding.ActivitySplashScreenBinding

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Hide status bar and make fullscreen for consistent branding
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_FULLSCREEN
        )
        
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Use MainLooper for Handler to avoid potential crashes and leaks
        Handler(Looper.getMainLooper()).postDelayed({
            if (!isFinishing) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish() // Ensure splash is removed from backstack
            }
        }, 2000)
    }
}
