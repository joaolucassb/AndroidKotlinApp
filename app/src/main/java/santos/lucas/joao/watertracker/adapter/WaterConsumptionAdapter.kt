package santos.lucas.joao.watertracker.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import santos.lucas.joao.watertracker.R
import santos.lucas.joao.watertracker.sqlite.WaterEntry

class WaterConsumptionAdapter(private val consumptions: List<WaterEntry>):
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
        val consumption = consumptions[position]
        holder.dateTextView.text = consumption.date
        holder.amountTextView.text = "${consumption.amount} ml"
    }

    override fun getItemCount(): Int {
        return consumptions.size
    }
}