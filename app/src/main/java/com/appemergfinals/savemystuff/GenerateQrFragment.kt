package com.appemergfinals.savemystuff

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import kotlinx.android.synthetic.main.activity_generate_qr_fragment.*
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


class GenerateQrFragment : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generate_qr_fragment)

        btSave.setOnClickListener{


            val text = etAddInfo.text
            if(text.isNotBlank()){
                val bitmap = generateQRCode(text.toString())
                val toast = Toast.makeText(applicationContext,"Image has been saved to device",Toast.LENGTH_SHORT)
                toast.show()
                Toast.makeText(applicationContext,"We recommend turning it into a key chain or printing it as a sticker. Thank you!",Toast.LENGTH_LONG).show()
                saveImage(bitmap)

            }
        }



        btGenerate.setOnClickListener{
            val text = etAddInfo.text
            if(text.isNotBlank()){
                val bitmap = generateQRCode(text.toString())
                imageView3.setImageBitmap(bitmap)

            }
        }
    }


    private fun saveImage(bitmap:Bitmap?):Boolean{
        val saved:Boolean
        val fos: OutputStream

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues =  ContentValues()
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, System.currentTimeMillis())
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/" + "QR")
            val imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            fos = contentResolver.openOutputStream(imageUri!!)!!
        } else {
            val imagesDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM).toString() + File.separator + "QR"

            val file = File(imagesDir)

            if (!file.exists()) {
                file.mkdir()
            }

            val image = File(imagesDir, "${System.currentTimeMillis()}.png")
            fos = FileOutputStream(image)

        }

        saved = bitmap!!.compress(Bitmap.CompressFormat.PNG, 100, fos)
        fos.flush()
        fos.close()
        return saved
    }




    private fun generateQRCode(text: String): Bitmap? {

        val width = 500
        val height = 500
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val codeWriter = MultiFormatWriter()
        try{
            val bitMatrix = codeWriter.encode(text, BarcodeFormat.QR_CODE, width, height)
            for(x in 0 until width){
                for(y in 0 until height){
                    bitmap.setPixel(x,y, if(bitMatrix[x,y]) Color.BLACK else Color.WHITE)
                }
            }
        }catch (e: WriterException){
            Log.d("", "generateQRCode: ${e.message}")
        }
        return bitmap


    }



























}