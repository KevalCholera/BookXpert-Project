package com.example.bookxpertproject.design

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.bookxpertproject.R
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class MainActivity : AppCompatActivity() {
    lateinit var imageView: ImageView
    lateinit var fileName: EditText
    lateinit var pdfDocument: PdfDocument

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        imageView = findViewById(R.id.ImageView)
        fileName = findViewById(R.id.fileName)

        // granting permission
        val galleryPermissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (EasyPermissions.hasPermissions(this, *galleryPermissions)) {
            Log.i("local-dev", "permissions are granted")
        } else {
            EasyPermissions.requestPermissions(
                this, "Access for storage",
                101, *galleryPermissions
            )
        }

        // TextWatcher is going to track fileName which is user input for pdf file name.
        // User will be informed always while EditText (filename) is changing.
        fileName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                checkFileName()
            }

            override fun afterTextChanged(s: Editable) {
                checkFileName()
            }
        })
    }

    fun checkFileName(): Boolean {
        val validity = findViewById<TextView>(R.id.validity)
        val saveButton = findViewById<Button>(R.id.saveButton)
        return if (fileName!!.text.toString().endsWith(".pdf")) {
            validity.setTextColor(Color.GREEN)
            validity.text = "Valid"
            saveButton.visibility = View.VISIBLE
            true
        } else {
            validity.setTextColor(Color.RED)
            validity.text = "INVALID!"
            saveButton.visibility = View.INVISIBLE
            false
        }
    }

    fun selectImage(v: View?) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CODE && resultCode == RESULT_OK && data != null) {
            val selectedImageURI = data.data
            val filePath = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = contentResolver.query(selectedImageURI!!, filePath, null, null, null)
            if (cursor == null) {
                Toast.makeText(this, "Cursor Error", Toast.LENGTH_SHORT)
                return
            }
            cursor.moveToFirst()
            val columnIndex = cursor.getColumnIndex(filePath[0])
            val path = cursor.getString(columnIndex)
            val bitmap = BitmapFactory.decodeFile(path)
            Toast.makeText(this, path.toString(), Toast.LENGTH_SHORT).show()
            imageView!!.setImageBitmap(bitmap)
            makePDF(bitmap)
        }
        if (requestCode == 123 && resultCode == RESULT_OK && data != null) {
            val uri = data.data
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun makePDF(bitmap: Bitmap) {
        pdfDocument = PdfDocument()
        val pageInfo = PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
        val page = pdfDocument!!.startPage(pageInfo)
        val canvas = page.canvas
        val paint = Paint()
        paint.color = Color.parseColor("#FFFFFF")
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        pdfDocument!!.finishPage(page)
        if (fileName!!.text.toString().isEmpty()) {
            Toast.makeText(
                this,
                "You need to enter file name as follow\nyour_fileName.pdf",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun saveFile(v: View?) {
        if (pdfDocument == null) {
            Log.i("local-dev", "pdfDocument in 'saveFile' function is null")
            return
        }
        val root = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "ImgToPDF"
        )
        var isDirectoryCreated = root.exists()
        if (!isDirectoryCreated) {
            isDirectoryCreated = root.mkdir()
        }
        if (checkFileName()) {
            val userInputFileName = fileName!!.text.toString()
            val file = File(root, userInputFileName)
            try {
                val fileOutputStream = FileOutputStream(file)
                pdfDocument!!.writeTo(fileOutputStream)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            pdfDocument!!.close()
        }
        Log.i("local-dev", "'saveFile' function successfully done")
        Toast.makeText(
            this, """
     Successful! PATH:
     Internal Storage/${Environment.DIRECTORY_DOWNLOADS}
     """.trimIndent(), Toast.LENGTH_SHORT
        ).show()
    }

    fun showPDF(view: View?) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "application/pdf"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(intent, 123)
    }

    companion object {
        private const val CODE = 1 // CODE for onActivityResult
    }
}