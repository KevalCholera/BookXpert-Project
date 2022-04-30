package com.example.bookxpertproject.app

import android.app.Application
import com.example.bookxpertproject.utils.ConnectivityReceiver

class AppController : Application(){
    override fun onCreate() {
        super.onCreate()
        instance = this
    }
    companion object {
        lateinit var instance: AppController

    }

    fun setConnectivityListener(listener: ConnectivityReceiver.ConnectivityReceiverListener) {
        ConnectivityReceiver.connectivityReceiverListener = listener
    }
}