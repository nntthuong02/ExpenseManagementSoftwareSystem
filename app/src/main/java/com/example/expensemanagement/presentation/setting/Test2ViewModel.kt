package com.example.expensemanagement.presentation.setting

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
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
class Test2ViewModel @Inject constructor(
    private val getAllTransactions: GetAllTransactions,
    private val appDatabase: AppDatabase
): ViewModel(){

//    val csr = appDatabase.openHelper.writableDatabase.query(
//        "SELECT * FROM group_table",
//        arrayOf<Any>()
//    )
//    private val _shareIntent = MutableStateFlow<Intent?>(null)
//    val shareIntent: MutableStateFlow<Intent?> = _shareIntent
//    val sendIntent: Intent = Intent().apply {
//        action = Intent.ACTION_SEND
//        putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
//        type = "text/plain"
//    }
//    fun exportDataToSQLiteFile(context: Context, fileName: String){
//        val db = writableDatabase
//        val cursor: Cursor =
//    }
//    fun createShareIntent() {
//        val sendIntent = Intent().apply {
//            action = Intent.ACTION_SEND
//            putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
//            type = "text/plain"
//        }
//        val shareIntent = Intent.createChooser(sendIntent, null)
//
//        _shareIntent.value = shareIntent
//    }
//
//    val shareIntent = Intent.createChooser(sendIntent, null)
    /**
     * Backup the database
     */
//    fun backupDatabase(context: Context): Int {
//        var result = -99
//        val dbFile = context.getDatabasePath("transactionDB")
//        val dbWalFile = File(dbFile.path + "-wal")
//        val dbShmFile = File(dbFile.path + "-shm")
//        val bkpFile = File(dbFile.path + "-bkp")
//        val bkpWalFile = File(bkpFile.path + "-wal")
//        val bkpShmFile = File(bkpFile.path + "-shm")
//        if (bkpFile.exists()) bkpFile.delete()
//        if (bkpWalFile.exists()) bkpWalFile.delete()
//        if (bkpShmFile.exists()) bkpShmFile.delete()
//        checkpoint()
//        try {
//            dbFile.copyTo(bkpFile,true)
//            if (dbWalFile.exists()) dbWalFile.copyTo(bkpWalFile,true)
//            if (dbShmFile.exists()) dbShmFile.copyTo(bkpShmFile, true)
//            result = 0
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//        return result
//    }

    /**
     *  Restore the database and then restart the App
     */
//    fun restoreDatabase(context: Context,restart: Boolean = true) {
//        if(!File(context.getDatabasePath("transactionDB").path + "-bkp").exists()) {
//            return
//        }
//        val dbpath = appDatabase.openHelper.readableDatabase.path
//        val dbFile = File(dbpath)
//        val dbWalFile = File(dbFile.path + "-wal")
//        val dbShmFile = File(dbFile.path + "-shm")
//        val bkpFile = File(dbFile.path + "-bkp")
//        val bkpWalFile = File(bkpFile.path + "-wal")
//        val bkpShmFile = File(bkpFile.path + "-shm")
//        try {
//            bkpFile.copyTo(dbFile, true)
//            if (bkpWalFile.exists()) bkpWalFile.copyTo(dbWalFile, true)
//            if (bkpShmFile.exists()) bkpShmFile.copyTo(dbShmFile,true)
//            checkpoint()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//        if (restart) {
//            val i = context.packageManager.getLaunchIntentForPackage(context.packageName)
//            i!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            context.startActivity(i)
//            System.exit(0)
//        }
//    }
    fun backupDatabase(context: Context, uri: Uri?): Int {
        if (uri == null) return -1 // Kiểm tra nếu người dùng chưa chọn vị trí lưu

        var result = -99
        val dbFile = context.getDatabasePath("transactionDB")
        val dbWalFile = File(dbFile.path + "-wal")
        val dbShmFile = File(dbFile.path + "-shm")

        checkpoint()

        try {
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                dbFile.inputStream().copyTo(outputStream)
            }
            // Sao chép các file WAL và SHM nếu tồn tại
            if (dbWalFile.exists()) {
                context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                    dbWalFile.inputStream().copyTo(outputStream)
                }
            }
            if (dbShmFile.exists()) {
                context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                    dbShmFile.inputStream().copyTo(outputStream)
                }
            }
            result = 0
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return result
    }


    private fun checkpoint() {
        var db = appDatabase.openHelper.writableDatabase
        db.query("PRAGMA wal_checkpoint(FULL);", arrayOf<Any>()).use { it.moveToFirst() }
        db.query("PRAGMA wal_checkpoint(TRUNCATE);",arrayOf<Any>()).use { it.moveToFirst()}
    }

    fun restoreDatabase(context: Context, uri: Uri) {
        viewModelScope.launch {
            try {
                val contentResolver: ContentResolver = context.contentResolver
                val inputStream: InputStream? = contentResolver.openInputStream(uri)
                val dbpath = context.getDatabasePath("transactionDB").path
                val dbFile = File(dbpath)
                val dbWalFile = File(dbFile.path + "-wal")
                val dbShmFile = File(dbFile.path + "-shm")

                inputStream?.use { input ->
                    FileOutputStream(dbFile).use { output ->
                        copyStream(input, output)
                    }

                    // Handle WAL file
                    val walUri = Uri.parse("$uri-wal")
                    val walInputStream = contentResolver.openInputStream(walUri)
                    walInputStream?.use { walStream ->
                        FileOutputStream(dbWalFile).use { walOutput ->
                            copyStream(walStream, walOutput)
                        }
                    }

                    // Handle SHM file
                    val shmUri = Uri.parse("$uri-shm")
                    val shmInputStream = contentResolver.openInputStream(shmUri)
                    shmInputStream?.use { shmStream ->
                        FileOutputStream(dbShmFile).use { shmOutput ->
                            copyStream(shmStream, shmOutput)
                        }
                    }
                }
                checkpoint()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun copyStream(input: InputStream, output: OutputStream) {
        val buffer = ByteArray(1024)
        var length: Int
        while (input.read(buffer).also { length = it } > 0) {
            output.write(buffer, 0, length)
        }
    }






    fun queryData(): Cursor {
        return appDatabase.openHelper.writableDatabase.query(
            "SELECT _id, fundName, groupId FROM fund_table",
            emptyArray()
        )
    }
}



