package santos.lucas.joao.watertracker.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper(context: Context): SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        const val DB_NAME = "water_tracker.db"
        const val DB_VERSION = 1
        const val TABLE_CONSUMPTION = "consumption"
        const val ID = "_id"
        const val AMOUNT = "amount"
        const val DATE = "date"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val sql = String.format("CREATE TABLE %s (" +
                "%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                "%s INTEGER" +
                "%s INTEGER)",
            TABLE_CONSUMPTION, ID, AMOUNT, DATE)
        db?.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_CONSUMPTION")
        onCreate(db)
    }
}