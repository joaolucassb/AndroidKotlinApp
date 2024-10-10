package santos.lucas.joao.watertracker.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor

data class WaterEntry(val id: Int, val amount: Int, val date: String)

class WaterEntryController public constructor(context: Context) {

    private val database: DbHelper = DbHelper(context)
    private val writableDatabase = database.writableDatabase
    private val readableDatabase  = database.readableDatabase

    fun insertEntry(entry: WaterEntry): String {
        val values = ContentValues()
        values.put(DbHelper.AMOUNT, entry.amount)
        values.put(DbHelper.DATE, entry.date)

        val result = writableDatabase.insert(DbHelper.TABLE_CONSUMPTION, null, values)
        writableDatabase.close()
        return if (result == -1L) "Error inserting record" else "Record inserted successfully"
    }

    fun getAllEntries(): List<WaterEntry> {
        val entries = mutableListOf<WaterEntry>()
        val cursor = readableDatabase.query(
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
                        date = it.getColumnIndex(DbHelper.DATE).toString()
                    )
                    entries.add(entry)
                } while (it.moveToNext())
            }
        }

        readableDatabase.close()
        return entries
    }

    fun getEntryById(id: Int): WaterEntry? {
        val selectQuery = "SELECT * FROM ${DbHelper.TABLE_CONSUMPTION} WHERE ${DbHelper.ID} = ?"
        val cursor: Cursor = readableDatabase.rawQuery(selectQuery, arrayOf(id.toString()))
        var entry: WaterEntry? = null

        cursor.use {
            if (it.moveToFirst()) {
                entry = WaterEntry(
                    id = it.getInt(it.getColumnIndexOrThrow(DbHelper.ID)),
                    amount = it.getInt(it.getColumnIndexOrThrow(DbHelper.AMOUNT)),
                    date = it.getString(it.getColumnIndexOrThrow(DbHelper.DATE))
                )
            }
        }

        readableDatabase.close()
        return entry
    }

    fun updateEntry(entry: WaterEntry): Boolean {
        val values = ContentValues().apply {
            put(DbHelper.AMOUNT, entry.amount)
            put(DbHelper.DATE, System.currentTimeMillis())
        }
        val whereClause = "${DbHelper.ID} = ?"
        val whereArgs = arrayOf(entry.id.toString())
        val updatedRows = writableDatabase.update(DbHelper.TABLE_CONSUMPTION, values, whereClause, whereArgs)
        writableDatabase.close()
        return updatedRows > 0
    }

    fun deleteEntry(id: Int): Boolean {
        val whereClause = "${DbHelper.ID} = ?"
        val whereArgs = arrayOf(id.toString())
        val deletedRows = writableDatabase.delete(DbHelper.TABLE_CONSUMPTION, whereClause, whereArgs)
        writableDatabase.close()
        return deletedRows > 0
    }
}