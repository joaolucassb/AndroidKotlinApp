package santos.lucas.joao.watertracker.adapter

import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import santos.lucas.joao.watertracker.R
import santos.lucas.joao.watertracker.sqlite.DbHelper
import santos.lucas.joao.watertracker.sqlite.WaterEntry

class WaterConsumptionAdapter(private val cursor: Cursor):
    RecyclerView.Adapter<WaterConsumptionAdapter.ViewHolder> () {

        class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
            val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
            val amountTextView: TextView = itemView.findViewById(R.id.amountTextView)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        cursor.moveToPosition(position)
        val amount = cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.AMOUNT))
        val date = cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.DATE))


        holder.dateTextView.text = date
        holder.amountTextView.text = "$amount ml"
    }

    override fun getItemCount(): Int {
        return cursor.count
    }
}