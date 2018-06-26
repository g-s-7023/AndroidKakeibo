package g_s_org.androidapp.com.androidkakeibo.model

import android.app.Activity
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import android.widget.Toast
import g_s_org.androidapp.com.androidkakeibo.R
import g_s_org.androidapp.com.androidkakeibo.common.Constants

class KakeiboDBAccess(val a:Activity) {

    // insert entry
    fun insertKakeibo(cv: ContentValues) {
        execWrite { db: SQLiteDatabase ->
            db.insert(DBAccessHelper.TABLE_NAME, null, cv)
        }
    }

    // delete entry (logically)
    fun deleteKakeibo(id: Int) {
        val cv = ContentValues()
        cv.put("isDeleted", Constants.TRUE)
        execWrite { db: SQLiteDatabase ->
            db.update(DBAccessHelper.TABLE_NAME, cv, "_id = ?", arrayOf(id.toString()))
        }
    }

    // update entry
    fun updateKakeibo(id: Int, cv: ContentValues) {
        execWrite{ db: SQLiteDatabase ->
            db.update(DBAccessHelper.TABLE_NAME, cv, "_id = ?", arrayOf(id.toString()))
        }
    }

    // read kakeibo data of the specified month
    fun readKakeiboOfMonth(year: Int, month: Int): Cursor? {
        return execRead { db: SQLiteDatabase ->
            db.query(DBAccessHelper.TABLE_NAME,
                    arrayOf("_id", "year", "month", "day", "dayOfWeek", "category", "type", "price", "detail", "termsOfPayment", "isSynchronized"),
                    "year = ? AND month = ? AND isDeleted = ?",
                    arrayOf(year.toString(), month.toString(), Constants.FALSE.toString()),
                    null, null, "day ASC")
        }
    }

    // execute write function passed
    private fun execWrite(f: (SQLiteDatabase) -> Unit) {
        // create helper
        val helper = DBAccessHelper(a)
        // db
        var db: SQLiteDatabase? = null
        try {
            // open db
            db = helper.writableDatabase
            // begin transaction
            db.beginTransaction()
            // execution(insert, update, delete)
            f(db)
            // commit
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            // display toast if error
            Toast.makeText(a, R.string.msg_dbaccesserror, Toast.LENGTH_SHORT).show()
            // out put exception log
            Log.e("ERROR", e.toString())
        } finally {
            // end transaction
            db?.endTransaction()
            // close DB
            db?.close()
        }
    }

    // execute read function passed and return cursor
    private fun execRead(f: (SQLiteDatabase) -> Cursor?): Cursor? {
        // create helper
        val helper = DBAccessHelper(a)
        // DB
        var db: SQLiteDatabase? = null
        // cursor
        var cursor: Cursor? = null
        try {
            // get DB object
            db = helper.readableDatabase
            // get data
            cursor = f(db)
        } catch (e: Exception) {
            throw e
        } finally {
            return cursor
        }
    }
}
