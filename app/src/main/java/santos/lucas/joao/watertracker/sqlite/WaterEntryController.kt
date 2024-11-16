package santos.lucas.joao.watertracker.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor

data class WaterEntry(val id: Int?, val amount: Int, val date: String)

class WaterEntryController constructor(context: Context) {

    private val database: DbHelper = DbHelper(context)
    private val writableDatabase = database.writableDatabase
    private val readableDatabase  = database.readableDatabase

    fun insertEntry(entry: WaterEntry): String {
        val values = ContentValues()
        values.put(DbHelper.AMOUNT, entry.amount)
        values.put(DbHelper.DATE, entry.date)

        val result = writableDatabase.insert(DbHelper.TABLE_CONSUMPTION, null, values)
        return if (result == -1L) "Erro ao inserir consumdo!" else "Consumo de ${values.get(DbHelper.AMOUNT)} adicionado com sucesso!"
    }

    fun getAllEntries(): Cursor {
        val fields = arrayOf(DbHelper.ID, DbHelper.AMOUNT, DbHelper.DATE)
        val cursor = readableDatabase.query(
            DbHelper.TABLE_CONSUMPTION,
            fields, null, null, null, null,
            "${DbHelper.DATE} DESC"
        )
        cursor.moveToFirst()

        return cursor
    }

    fun getEntryById(id: Int): Cursor {
        val fields = arrayOf(DbHelper.ID, DbHelper.AMOUNT, DbHelper.DATE)
        val where = "${DbHelper.ID} = ?"
        val whereArgs = arrayOf(id.toString())
        val cursor = readableDatabase.query(DbHelper.TABLE_CONSUMPTION, fields, where, whereArgs, null,
            null, null, null)

        cursor.moveToFirst()

        return cursor
    }

    fun getConsumptionForDate(date: String): Int {
        var totalConsumption = 0
        val cursor = readableDatabase.rawQuery(
            "SELECT SUM(${DbHelper.AMOUNT}) FROM ${DbHelper.TABLE_CONSUMPTION} WHERE ${DbHelper.DATE} = ?",
            arrayOf(date)
        )

        if (cursor.moveToFirst()) {
            totalConsumption = cursor.getInt(0)
        }
        cursor.close()

        return totalConsumption
    }

    fun updateEntry(entry: WaterEntry): String {
        val values = ContentValues().apply {
            put(DbHelper.AMOUNT, entry.amount)
            put(DbHelper.DATE, System.currentTimeMillis())
        }
        val whereClause = "${DbHelper.ID} = ?"
        val whereArgs = arrayOf(entry.id.toString())
        val updatedRows = writableDatabase.update(DbHelper.TABLE_CONSUMPTION, values, whereClause, whereArgs)

        return if (updatedRows > 0) "Consumo Atualizaco com sucesso!" else "Erro ao atualizar consumo."
    }

    fun deleteEntry(id: Int): String {
        val whereClause = "${DbHelper.ID} = ?"
        val whereArgs = arrayOf(id.toString())
        val deletedRows = writableDatabase.delete(DbHelper.TABLE_CONSUMPTION, whereClause, whereArgs)

        return if (deletedRows > 0) "Consumo Deletado com sucesso!" else "Erro ao deletar consumo."
    }
}