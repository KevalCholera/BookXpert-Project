package com.example.bookxpertproject.model

import com.google.gson.annotations.SerializedName

data class DashboardBookXpressResponse(
    @SerializedName("totalAmount")
    val totalAmount: String?,
    @SerializedName("data")
    val data: List<Data>?

) {
    data class Data(
        @SerializedName("ActID")
        val actID: String?,
        @SerializedName("ActName")
        val actName: String?,
        @SerializedName("amount")
        val amount: String?
    )
}