package com.example.bookxpertproject.design.main

import android.content.Intent
import android.os.Bundle
import com.example.bookxpertproject.R
import com.example.bookxpertproject.base.BaseActivity
import com.example.bookxpertproject.databinding.ActivityMainBinding
import com.example.bookxpertproject.design.cameratopdf.CameraToPdfActivity
import com.example.bookxpertproject.design.dashboard.DashboardActivity

class MainActivity : BaseActivity() {
    private val binding: ActivityMainBinding by binding(R.layout.activity_main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding.lifecycleOwner = this
        onClickFun()
    }

    private fun onClickFun() {
        binding.btMainActivityTask1Amount.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
        }
        binding.btMainActivityTask2Camera.setOnClickListener {
            startActivity(Intent(this, CameraToPdfActivity::class.java))
        }
    }
}