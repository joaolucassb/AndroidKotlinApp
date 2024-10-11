package santos.lucas.joao.watertracker.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import santos.lucas.joao.watertracker.adapter.WaterConsumptionAdapter
import santos.lucas.joao.watertracker.databinding.ActivityHistoryBinding
import santos.lucas.joao.watertracker.sqlite.WaterEntryController

class HistoryActivity: ComponentActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WaterConsumptionAdapter
    private lateinit var controller: WaterEntryController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.historyRecycleView
        binding.historyRecycleView.layoutManager = LinearLayoutManager(this)
        controller = WaterEntryController(this)

        loadData()
    }

    private fun loadData() {
        val consumptions = controller.getAllEntries()
        adapter = WaterConsumptionAdapter(consumptions)
        recyclerView.adapter = adapter
    }
}