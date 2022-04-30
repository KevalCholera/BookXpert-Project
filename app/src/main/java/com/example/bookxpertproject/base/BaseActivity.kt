package com.example.bookxpertproject.base

import android.R
import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.content.IntentFilter
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.example.bookxpertproject.app.AppController
import com.example.bookxpertproject.service.RetrofitService
import com.example.bookxpertproject.utils.ConnectivityReceiver
import com.example.bookxpertproject.utils.NetworkHelper
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*


open class BaseActivity : DataBindingActivity(),
    ConnectivityReceiver.ConnectivityReceiverListener {
    val TAG = "ET--" + javaClass.simpleName

    lateinit var retrofitService: RetrofitService
    lateinit var networkHelper: NetworkHelper
    lateinit var connectivityReceiver: ConnectivityReceiver

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retrofitService = RetrofitService.getInstance()
        networkHelper = NetworkHelper(this)

        connectivityReceiver = ConnectivityReceiver()

        try {
            registerReceiver(
                ConnectivityReceiver(),
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            )
        } catch (e: Exception) {
            Log.e("Log", "Log==>" + e.printStackTrace())
        }

    }

    fun logI(tag: String, msg: String) {
        Log.i(TAG, "$tag: $msg")
    }

    fun logE(tag: String, msg: String) {
        Log.e(TAG, "$tag: $msg")
    }

    fun logD(tag: String, msg: String) {
        Log.d(TAG, "$tag: $msg")
    }

    fun logW(tag: String, msg: String) {
        Log.w(TAG, "$tag: $msg")
    }

    fun getImageUri(bitmap: Bitmap): Uri {
        val wrapper = ContextWrapper(this)

        var file = wrapper.getDir("Images", Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return Uri.parse(file.absolutePath)
    }

    fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap {
        var width = image.width
        var height = image.height
        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        val compressedBitmap = Bitmap.createScaledBitmap(image, width, height, true)
        return compressedBitmap ?: image
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {

    }

    override fun onResume() {
        super.onResume()
        AppController.instance.setConnectivityListener(this)
    }

    override fun onStop() {
        super.onStop()
        try {
            if (this::connectivityReceiver.isInitialized)
                unregisterReceiver(connectivityReceiver)
        } catch (e: Exception) {
            Log.e(TAG, "onStop: connectivityReceiver ${e.message}")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.getItemId()) {
            R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}