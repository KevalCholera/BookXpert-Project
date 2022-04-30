package com.example.bookxpertproject.design.dashboard

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookxpertproject.repository.DashboardBookXpressRepository
import com.example.bookxpertproject.utils.NetworkHelper

class DashboardModelFactory(
    private val context: Context,
    private val dashboardRepository: DashboardBookXpressRepository,
    private val networkHelper: NetworkHelper
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(DashboardActivityViewModel::class.java)) {
            DashboardActivityViewModel(
                context,
                this.dashboardRepository,
                networkHelper
            ) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}