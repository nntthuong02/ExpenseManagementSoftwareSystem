package com.example.expensemanagement.presentation.setting

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensemanagement.data.AppDatabase
import com.example.expensemanagement.domain.usecase.read_database.GetAllTransactions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val appDatabase: AppDatabase
) : ViewModel() {
    /**
     * Backup the database
     */
    fun backupDatabase(context: Context, uri: Uri?): Int {
        if (uri == null) return -1
        var result = -99
        val dbFile = context.getDatabasePath("transactionDB")
        Log.d("dbFile.path", dbFile.path + "-wal")
        checkpoint()
        try {
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                dbFile.inputStream().copyTo(outputStream)
            }
            result = 0
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return result
    }

    /**
     *  Restore the database and then restart the App
     */

    fun restoreDatabase(context: Context, uri: Uri?, restart: Boolean = true) {
        if (uri == null) {
            return
        }
        appDatabase.close()
        var filePath: String? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                filePath = it.getString(columnIndex)
            }
        }
        Log.d("filePath", filePath.toString())
        val dbpath = appDatabase.openHelper.readableDatabase.path
        Log.d("dbpath", dbpath.toString())
        val dbFile = File(dbpath)
        val bkpFile = File(dbFile.path + "-bkp")
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            inputStream?.use { sourceStream ->
                dbFile.outputStream().use { destStream ->
                    Log.d("destStream", destStream.toString())
                    sourceStream.copyTo(destStream)
                    Log.d("dbFile",dbFile.toString())
                }
            }

            checkpoint()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        if (restart) {
            val i = context.packageManager.getLaunchIntentForPackage(context.packageName)
            i!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            context.startActivity(i)
            System.exit(0)
        }
    }

    fun shareDatabaseFile(context: Context) {
        val dbFile = File(context.filesDir, "shared_transactionDB")
        val originalDbFile = context.getDatabasePath("transactionDB")
        originalDbFile.copyTo(dbFile, overwrite = true)

        val dbUri: Uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            dbFile
        )

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "application/octet-stream"
            putExtra(Intent.EXTRA_STREAM, dbUri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(Intent.createChooser(shareIntent, "Share database"))
    }
    private fun checkpoint() {
        var db = appDatabase.openHelper.writableDatabase
        db.query("PRAGMA wal_checkpoint(FULL);", arrayOf<Any>()).use { it.moveToFirst() }
        db.query("PRAGMA wal_checkpoint(TRUNCATE);", arrayOf<Any>()).use { it.moveToFirst() }
    }
}