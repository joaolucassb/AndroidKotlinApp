package santos.lucas.joao.watertracker

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import santos.lucas.joao.watertracker.activities.HistoryActivity
import santos.lucas.joao.watertracker.databinding.ActivityMainBinding
import santos.lucas.joao.watertracker.sqlite.WaterEntry
import santos.lucas.joao.watertracker.sqlite.WaterEntryController
import santos.lucas.joao.watertracker.worker.ReminderWorker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var controller: WaterEntryController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        controller = WaterEntryController(this)

        binding.addEntryBtn.setOnClickListener {
            val amountStr = binding.amountEditText.text.toString()
            if (amountStr.isNotEmpty()) {
                val amount = amountStr.toInt()
                val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
                val entry = WaterEntry(id = 0, amount = amount, date = currentDate)
                val result = controller.insertEntry(entry)
                showToast(result)

                binding.amountEditText.text.clear()
            }
        }

        binding.historyBtn.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }

        scheduleReminder()
    }

    private fun scheduleReminder() {
        val workRequest = PeriodicWorkRequestBuilder<ReminderWorker>(30, TimeUnit.MINUTES)
            .build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "HydrationReminder",
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )

    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

