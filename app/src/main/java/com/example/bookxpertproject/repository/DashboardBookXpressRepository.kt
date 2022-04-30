package com.example.bookxpertproject.repository

import android.util.Log
import com.example.bookxpertproject.model.DashboardBookXpressResponse
import com.example.bookxpertproject.service.RetrofitService
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Response

class DashboardBookXpressRepository(
    private val retrofitService: RetrofitService
) {
    suspend fun getBookXpressList(): Response<DashboardBookXpressResponse> {
        val response = retrofitService.getBookXpressList().body()
        var totalAmount = 0.00
        val jsonTotalResponse = JSONObject()
        val jsonBookXpertResponse = JSONArray(response)

        jsonTotalResponse.put("data", jsonBookXpertResponse)

        for (i in 0 until jsonBookXpertResponse.length()) {
            val data = jsonBookXpertResponse.optJSONObject(i)
            totalAmount += data.optString("amount").toString().toDouble()
        }
        jsonTotalResponse.put("totalAmount", totalAmount.toString())

        val newData =
            Gson().fromJson(
                jsonTotalResponse.toString(),
                DashboardBookXpressResponse::class.java
            )

        return Response.success(newData)
    }
}