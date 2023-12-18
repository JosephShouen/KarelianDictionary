package com.example.kareliandictionary

import android.content.Context
import android.content.SharedPreferences
import java.io.FileOutputStream
import java.io.InputStream
import android.util.Log

fun CreateDB(context: Context) {
    Log.d("CreateDB", "CreatedbStarted")
    val databasePath = context.getDatabasePath("tiny_big_vepkar.db")
    databasePath.delete()
    if (!databasePath.exists()) {
        val inputStream = context.assets.open("tiny_big_vepkar.db")
        val outputStream = FileOutputStream(databasePath.absolutePath)
        inputStream.use { input: InputStream ->
            outputStream.use { output: FileOutputStream ->
                input.copyTo(output)
            }
        }
    }
    Log.d("CreateDB", "CreatedbEnded")
}