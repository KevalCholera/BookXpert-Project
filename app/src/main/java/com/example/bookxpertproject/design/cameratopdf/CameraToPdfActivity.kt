package com.example.bookxpertproject.design.cameratopdf

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.bookxpertproject.R
import com.example.bookxpertproject.base.BaseActivity
import com.example.bookxpertproject.databinding.ActivityCameraToPdfBinding
import com.rajat.pdfviewer.PdfViewerActivity
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class CameraToPdfActivity : BaseActivity() {
    private val binding: ActivityCameraToPdfBinding by binding(R.layout.activity_camera_to_pdf)

    private val requiredPermissionList = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    private val PERMISSION_CODE = 4040
    private val MY_CAMERA_PERMISSION_CODE = 4041
    lateinit var pdfDocument: PdfDocument
    var pdfPath = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.task_2_camera_to_pdf)
        onClickFun()
    }

    private fun onClickFun() {
        binding.btCameraView.setOnClickListener {
            if (pdfPath != "") {
                if (checkAndRequestPermission())
                    launchPdf()
            } else
                Toast.makeText(this, "Please Capture Image", Toast.LENGTH_SHORT)
                    .show()
        }
        binding.btCameraDownload.setOnClickListener {
            if (pdfPath != "")
                Toast.makeText(this, "Downloaded at this path:- $pdfPath", Toast.LENGTH_SHORT)
                    .show()
            else
                Toast.makeText(this, "Please Capture Image", Toast.LENGTH_SHORT)
                    .show()
        }
        binding.btCameraToPdfCapture.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkAndRequestPermission())
                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(
                            arrayOf(Manifest.permission.CAMERA),
                            MY_CAMERA_PERMISSION_CODE
                        )
                    } else {

                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        someActivityResultLauncher.launch(intent)

                    }
            }
        }
    }

    private var someActivityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data

                if (data != null) {
                    val image = data.extras!!.get("data") as Bitmap
                    binding.ivCameraShow.setImageBitmap(image)
                    makePDF(image)
                }
            }
        }

    private fun checkAndRequestPermission(): Boolean {
        val permissionsNeeded = ArrayList<String>()

        for (permission in requiredPermissionList) {
            if (ContextCompat.checkSelfPermission(this, permission) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                permissionsNeeded.add(permission)
            }
        }

        if (permissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsNeeded.toTypedArray(),
                PERMISSION_CODE
            )
            return false
        }

        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_CODE -> if (grantResults.isNotEmpty()) {
                val readPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val writePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED
                if (readPermission && writePermission)
                    launchPdf()
                else {
                    Toast.makeText(this, " Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun launchPdf() {
        Log.i(TAG, "launchPdf: $pdfPath")
        startActivity(
            PdfViewerActivity.launchPdfFromPath(           //PdfViewerActivity.Companion.launchPdfFromUrl(..   :: incase of JAVA
                this,
                pdfPath,                                // PDF URL in String format
                getString(R.string.app_name),                        // PDF Name/Title in String format
                "",                 // If nothing specific, Put "" it will save to Downloads
                enableDownload = false                    // This param is true by defualt.
            )
        )
    }

    private fun makePDF(bitmap: Bitmap) {
        pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas
        val paint = Paint()
        paint.color = Color.parseColor("#FFFFFF")
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        pdfDocument.finishPage(page)
        saveFile()
    }

    private fun saveFile() {
        binding.progressCircular.visibility = View.VISIBLE

        lifecycleScope.launch() {
            val root = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                getString(R.string.app_name)
            )
            var isDirectoryCreated = root.exists()
            if (!isDirectoryCreated) {
                isDirectoryCreated = root.mkdir()
            }
            val fileName = Calendar.getInstance().time.time.toString() + ".pdf"
            val file = File(root, fileName)

            try {
                val fileOutputStream = FileOutputStream(file)
                pdfDocument.writeTo(fileOutputStream)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            pdfDocument.close()
            launch {
                Log.i(TAG, "saveFile: ${file.absolutePath}")
                pdfPath = file.absolutePath
                binding.progressCircular.visibility = View.GONE
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}