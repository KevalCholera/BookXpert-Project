package com.example.bookxpertproject.design.dashboard

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookxpertproject.base.Status
import com.example.bookxpertproject.model.DashboardBookXpressViewState
import com.example.bookxpertproject.repository.DashboardBookXpressRepository
import com.example.bookxpertproject.utils.*
import kotlinx.coroutines.launch

class DashboardActivityViewModel(
    private val context: Context,
    private val dashboardRepository: DashboardBookXpressRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    private var _dashboardBookXpertResponse: MutableLiveData<DashboardBookXpressViewState> =
        MutableLiveData()

    val dashboardBookXpertResponse: LiveData<DashboardBookXpressViewState>
        get() {
            return _dashboardBookXpertResponse
        }

    init {
        if (networkHelper.isNetworkConnected())
            getBookXpressList()
        else {
            _dashboardBookXpertResponse.postValue(
                DashboardBookXpressViewState(
                    Status.NO_INTERNET,
                    "No Internet",
                    null
                )
            )
        }
    }

    private fun getBookXpressList() {

        _dashboardBookXpertResponse.postValue(
            DashboardBookXpressViewState(
                Status.LOADING,
                null,
                null
            )
        )
        viewModelScope.launch {
            val response = dashboardRepository.getBookXpressList()

            launch {

                when (response.code()) {
                    STATUS_CODE_SUCCESS -> {
                        _dashboardBookXpertResponse.postValue(
                            DashboardBookXpressViewState(
                                Status.SUCCESS,
                                null,
                                response.body()
                            )
                        )
                    }
                    STATUS_CODE_FAILURE -> {

                        _dashboardBookXpertResponse.postValue(
                            DashboardBookXpressViewState(
                                Status.FAIL,
                                "Something went wrong",
                                null
                            )
                        )
                    }
                    STATUS_CODE_INTERNAL_ERROR -> {

                        _dashboardBookXpertResponse.postValue(
                            DashboardBookXpressViewState(
                                Status.ERROR,
                                "Internal Error",
                                null
                            )
                        )
                    }
                    STATUS_CODE_TOKEN_EXPIRE -> {

                        _dashboardBookXpertResponse.postValue(
                            DashboardBookXpressViewState(
                                Status.UNAUTHORISED,
                                response.message(),
                                null
                            )
                        )
                    }
                    else -> {

                        _dashboardBookXpertResponse.postValue(
                            DashboardBookXpressViewState(
                                Status.ERROR,
                                "Something went wrong"
                            )
                        )
                    }
                }
            }
        }
    }
}