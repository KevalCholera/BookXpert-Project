package com.example.bookxpertproject.design.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.bookxpertproject.R
import com.example.bookxpertproject.base.BaseActivity
import com.example.bookxpertproject.databinding.ActivitySplashBinding
import com.example.bookxpertproject.design.main.MainActivity

class SplashActivity : BaseActivity() {
    private val binding: ActivitySplashBinding by binding(R.layout.activity_splash)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finishAffinity()

        }, 2000)
    }
}