package com.example.bookxpertproject.design.dashboard

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookxpertproject.R
import com.example.bookxpertproject.base.BaseActivity
import com.example.bookxpertproject.base.Status
import com.example.bookxpertproject.databinding.ActivityDashboardBinding
import com.example.bookxpertproject.design.dashboard.adapter.DashboardBookXpertAdapter
import com.example.bookxpertproject.model.DashboardBookXpressResponse
import com.example.bookxpertproject.repository.DashboardBookXpressRepository
import com.google.gson.Gson

class DashboardActivity : BaseActivity() {
    private val binding: ActivityDashboardBinding by binding(R.layout.activity_dashboard)
    private lateinit var viewModel: DashboardActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialFun()
        observerFun()
    }

    private fun initialFun() {
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(
            this,
            DashboardModelFactory(
                this,
                DashboardBookXpressRepository(
                    retrofitService
                ),
                networkHelper
            )
        )[DashboardActivityViewModel::class.java]
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.task_1_amount)
    }

    private fun observerFun() {
        viewModel.dashboardBookXpertResponse.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.progressCircular.visibility = View.GONE
                    if (it.response != null) {
                        Log.i(TAG, "observerFun: ${Gson().toJson(it.response)}")
                        setAlbumAdapter(it.response)
                    }
                }
                Status.ERROR -> {
                    binding.progressCircular.visibility = View.GONE
                }
                Status.LOADING -> {
                    binding.progressCircular.visibility = View.VISIBLE
                }
                Status.FAIL -> {
                    binding.progressCircular.visibility = View.GONE
                }
                Status.NO_INTERNET -> {
                    binding.progressCircular.visibility = View.GONE
                    Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    binding.progressCircular.visibility = View.GONE
                }
            }
        })
    }

    private fun setAlbumAdapter(response: DashboardBookXpressResponse) {
        binding.tvTotalAmount.text = response.totalAmount.toString()

        if (response.data != null) {
            val bookXpertAdapter = DashboardBookXpertAdapter(response.data)
            binding.rvDashboard.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            binding.rvDashboard.setHasFixedSize(true)
            binding.rvDashboard.adapter = bookXpertAdapter
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}