package santos.lucas.joao.watertracker

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ProgressBar
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
    private lateinit var waterProgressBar: ProgressBar

    private var dailyGoal = 2000 // Meta diária em ml

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        controller = WaterEntryController(this)

        waterProgressBar = binding.waterProgressBar

        val currentDate = getCurrentDate()
        var currentProgress = controller.getConsumptionForDate(currentDate)
        waterProgressBar.max = dailyGoal
        updateProgressBar(currentProgress)

        binding.addEntryBtn.setOnClickListener {
            val amountStr = binding.amountEditText.text.toString()
            if (amountStr.isNotEmpty()) {
                try {
                    val amount = amountStr.toInt()
                    if (amount > 0) {
                        val entry = WaterEntry(id = null, amount = amount, date = currentDate)
                        val result = controller.insertEntry(entry)
                        currentProgress+= amount
                        updateProgressBar(currentProgress)
                        showToast(result)

                        if (currentProgress >= dailyGoal) {
                            showToast("Parabéns! Você atingiu sua meta diária de água")
                            waterProgressBar.progress = 0
                        }
                        binding.amountEditText.text.clear()
                    } else {
                        showToast("Por favor, insira um valor maior que 0.")
                    }
                } catch (e: NumberFormatException) {
                    showToast("Entrada inválida. Por favor, insira um número.")
                }
            } else {
                showToast("Por favor, insira a quantidade de água consumida.")
            }
        }

        binding.historyBtn.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }

        scheduleReminder()
    }

    private fun updateProgressBar(currentProgress: Int) {
        waterProgressBar.progress = currentProgress
        val totalProgress = (currentProgress.toFloat() / dailyGoal) * 100
        binding.progressText.text = "Progresso de Consumo = ${totalProgress.toInt()}%"
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

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return dateFormat.format(Date())
    }
}

