package santos.lucas.joao.watertracker.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor

data class WaterEntry(val id: Int, val amount: Int, val date: Int)

class WaterEntryController private constructor(context: Context) {

    private val database: DbHelper = DbHelper(context)

    fun insertEntry(amount: Int): String {
        val db = database.writableDatabase
        val values = ContentValues()
        values.put(DbHelper.AMOUNT, amount)
        values.put(DbHelper.DATE, System.currentTimeMillis())

        val result = db.insert(DbHelper.TABLE_CONSUMPTION, null, values)
        db.close()
        return if (result == -1L) "Error inserting record" else "Record inserted successfully"
    }

    fun getAllEntries(): List<WaterEntry> {
        val entries = mutableListOf<WaterEntry>()
        val db = database.readableDatabase
        val cursor = db.query(
            DbHelper.TABLE_CONSUMPTION,
            null, null, null, null, null,
            "${DbHelper.DATE} DESC"
        )

        cursor.use {
            if (it.moveToFirst()) {
                do {
                    val entry = WaterEntry(
                        id = it.getInt(it.getColumnIndexOrThrow(DbHelper.ID)),
                        amount = it.getColumnIndex(DbHelper.AMOUNT),
                        date = it.getColumnIndex(DbHelper.DATE)
                    )
                    entries.add(entry)
                } while (it.moveToNext())
            }
        }

        db.close()
        return entries
    }

    fun getEntryById(id: Int): WaterEntry? {
        val db = database.readableDatabase
        val selectQuery = "SELECT * FROM ${DbHelper.TABLE_CONSUMPTION} WHERE ${DbHelper.ID} = ?"
        val cursor: Cursor = db.rawQuery(selectQuery, arrayOf(id.toString()))
        var entry: WaterEntry? = null

        cursor.use {
            if (it.moveToFirst()) {
                entry = WaterEntry(
                    id = it.getInt(it.getColumnIndexOrThrow(DbHelper.ID)),
                    amount = it.getInt(it.getColumnIndexOrThrow(DbHelper.AMOUNT)),
                    date = it.getInt(it.getColumnIndexOrThrow(DbHelper.DATE))
                )
            }
        }

        db.close()
        return entry
    }

    fun updateEntry(entry: WaterEntry): Boolean {
        val db = database.writableDatabase
        val values = ContentValues().apply {
            put(DbHelper.AMOUNT, entry.amount)
            put(DbHelper.DATE, System.currentTimeMillis())
        }
        val whereClause = "${DbHelper.ID} = ?"
        val whereArgs = arrayOf(entry.id.toString())
        val updatedRows = db.update(DbHelper.TABLE_CONSUMPTION, values, whereClause, whereArgs)
        db.close()
        return updatedRows > 0
    }

    fun deleteEntry(id: Int): Boolean {
        val db = database.writableDatabase
        val whereClause = "${DbHelper.ID} = ?"
        val whereArgs = arrayOf(id.toString())
        val deletedRows = db.delete(DbHelper.TABLE_CONSUMPTION, whereClause, whereArgs)
        db.close()
        return deletedRows > 0
    }
}