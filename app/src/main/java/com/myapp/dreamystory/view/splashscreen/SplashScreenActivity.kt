package com.myapp.dreamystory.view.splashscreen

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.myapp.dreamystory.databinding.ActivitySplashScreenBinding
import com.myapp.dreamystory.view.main.MainActivity

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val splashScreen = binding.ivSplashStory
        splashScreen.alpha = 0.2f
        splashScreen.animate().setDuration(1500).alpha(1f).withEndAction {
            val splashScreenGithub = Intent(this, MainActivity::class.java)
            startActivity(splashScreenGithub)
            finish()
        }
    }
}