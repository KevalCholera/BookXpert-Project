package com.example.bookxpertproject.model

import com.example.bookxpertproject.base.BaseViewState
import com.example.bookxpertproject.base.Status

class DashboardBookXpressViewState(
    val status: Status,
    val error: String? = null,
    val response: DashboardBookXpressResponse? = null
) : BaseViewState(status, error)