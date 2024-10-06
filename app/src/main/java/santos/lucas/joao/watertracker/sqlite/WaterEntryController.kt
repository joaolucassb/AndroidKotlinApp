package santos.lucas.joao.watertracker.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

data class WaterEntry(val id: Int, val amount: Int, val timestamp: Int)

class WaterEntryController private constructor(context: Context) {

    private val database: DbHelper = DbHelper(context)
    private val writableDatabase: SQLiteDatabase = database.writableDatabase
    private val readableDatabase: SQLiteDatabase = database.readableDatabase

    fun insertEntry(amount: Int): String {
        val db = database.writableDatabase
        val values = ContentValues()
        values.put(DbHelper.AMOUNT, amount)
        values.put(DbHelper.TIMESTAMP, System.currentTimeMillis())

        val result = db.insert(DbHelper.TABLE_ENTRIES, null, values)
        db.close()
        return if (result == -1L) "Error inserting record" else "Record inserted successfully"
    }

    fun getAllEntries(): List<WaterEntry> {
        val entries = mutableListOf<WaterEntry>()
        val sql = "SELECT * FROM ${DbHelper.TABLE_ENTRIES} ORDER BY ${DbHelper.TIMESTAMP} DESC"
        val db = database.readableDatabase
        val cursor: Cursor = db.rawQuery(sql, null)

        cursor.use {
            if (it.moveToFirst()) {
                do {
                    val entry = WaterEntry(
                        id = it.getInt(it.getColumnIndexOrThrow(DbHelper.ID)),
                        amount = it.getColumnIndex(DbHelper.AMOUNT.toInt().toString()),
                        timestamp = it.getColumnIndex(DbHelper.TIMESTAMP.toString())
                    )
                    entries.add(entry)
                } while (it.moveToNext())
            }
        }

        db.close()
        return entries
    }
}